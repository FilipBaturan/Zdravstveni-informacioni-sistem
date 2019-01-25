package zis.rs.zis.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.domain.ObjectFactory;
import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.entities.collections.Korisnici;
import zis.rs.zis.domain.enums.TipKorisnika;
import zis.rs.zis.service.definition.KorisnikServis;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringWriter;


@Service
public class KorisnikServisImpl extends IOStrimer implements KorisnikServis {

    private static final Logger logger = LoggerFactory.getLogger(LekServisImpl.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Maper maper;

    @Autowired
    private Sekvencer sekvencer;

    @Override
    public String dobaviSve() {
        return null;
    }

    @Override
    public String pretragaPoId(String id) {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/korisnici",
                    "korisnici.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Korisnici korisnici = (Korisnici) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            for (Korisnik k : korisnici.getKorisnik()) {
                if (k.getId().equals(id)) {
                    konekcija.oslobodiResurse(resursi);
                    if (k.isAktivan()) {
                        return k.toString();
                    } else {
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        throw new ValidacioniIzuzetak("Trazeni korisnik ne postoji u bazi!");
    }

    @Override
    public String sacuvaj(String korisnik) {
        return null;
    }

    @Override
    public String registruj(Akcija akcija) {
        Document doc = this.konvertujUDokument(akcija);
        if (doc == null) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        this.registruj(doc);
        return "Registracija uspesna!";
    }

    @Override
    public void obrisi(String id) {

    }


    /**
     * @param akcija koju treba procesirati
     * @return sadrzaj akcije
     */
    private Document konvertujUDokument(Akcija akcija) {
        try {
            Marshaller marshaller = JAXBContext.newInstance(zis.rs.zis.util.akcije.ObjectFactory.class)
                    .createMarshaller();
            Document dok = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            marshaller.marshal(akcija, dok);
            return dok;
        } catch (JAXBException | ParserConfigurationException e) {
            return null;
        }
    }

    /**
     * @param dokument sa korisnikom i osobom koju treba registovati
     */
    private void registruj(Document dokument) {
        Node sadrzaj = dokument.getElementsByTagName("sadrzaj").item(0);

        if (!sadrzaj.hasChildNodes()) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije, sadzaj nesme biti prazan!");
        }
        Node korisnik = this.dobaviKorisnika(sadrzaj.getFirstChild().getChildNodes());
        Node osoba = this.dobaviOsobu(sadrzaj.getFirstChild().getChildNodes());
        if (korisnik == null || osoba == null) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije, sadrzi neodgovarajuce entitete!");
        }
        String korisnikPrefiks = korisnik.getNodeName().split(":")[0];
        String osobaPrefiks = osoba.getNodeName().split(":")[0];
        Long korisnikId = sekvencer.dobaviId();
        String kor = this.validirajKorisnika(korisnik, korisnikId);
        String osb;
        if (osoba.getLocalName().equals("lekar")) {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("lekar"),
                    maper.dobaviURI("lekar"), osobaPrefiks, korisnikId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.LEKAR);
        } else if (osoba.getLocalName().equals("medicinska_sestra")) {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("medicinska_sestra"),
                    maper.dobaviURI("medicinska_sestra"), osobaPrefiks, korisnikId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.MEDICINSKA_SESTRA);
        } else {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("zdravstveni_karton"),
                    maper.dobaviURI("pacijenti"), osobaPrefiks, korisnikId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.PACIJENT);
        }
    }

    /**
     * @param korisnik koji treba sacuvati u bazi
     * @param prefiks  za prostor imena
     */
    private void sacuvajKorisnika(String korisnik, String prefiks) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils
                    .getFile(maper.dobaviUpit("dodavanje"))
                    .getPath();

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("korisnik"), maper.dobaviPutanju("korisnici"), korisnik,
                    maper.dobaviPrefiks("korisnici"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("korisnici"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param osoba        koju treba sacuvati u bazi
     * @param prefiks      prostora imena
     * @param tipKorisnika tip korisnika
     */
    private void sacuvajOsobu(String osoba, String prefiks, TipKorisnika tipKorisnika) {
        ResursiBaze resursi = null;
        try {
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            String sadrzajUpita = "";
            String dokument;
            if (tipKorisnika == TipKorisnika.LEKAR) {
                resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                        maper.dobaviDokument("lekari"));
                sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                        prefiks, maper.dobaviPrefiks("lekar"), maper.dobaviPutanju("lekari"), osoba,
                        maper.dobaviPrefiks("lekari"));
                dokument = maper.dobaviDokument("lekari");
                logger.info(sadrzajUpita);
            } else if (tipKorisnika == TipKorisnika.MEDICINSKA_SESTRA) {
                resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                        maper.dobaviDokument("medicinske_sestre"));
                sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                        prefiks, maper.dobaviPrefiks("medicinska_sestra"),
                        maper.dobaviPutanju("medicinske_sestre"), osoba,
                        maper.dobaviPrefiks("medicinske_sestre"));
                dokument = maper.dobaviDokument("medicinske_sestre");
            } else {
                resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                        maper.dobaviDokument("zdrastveni_kartoni"));
                dokument = maper.dobaviDokument("zdrastveni_kartoni");
            }

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(dokument, sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param lista elemenata
     * @return pronadjeni element
     */
    private Node dobaviKorisnika(NodeList lista) {
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals("korisnik")) {
                return element;
            }
        }
        return null;
    }

    /**
     * @param lista elemenata
     * @return pronadjeni element
     */
    private Node dobaviOsobu(NodeList lista) {
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals("lekar") ||
                    element.getLocalName().equals("medicinska_sestra") ||
                    element.getLocalName().equals("zdravstveni_karton")) {
                return element;
            }
        }
        return null;
    }

    /**
     * @param korisnik kojeg treba validirati
     * @return xml reprezentacija korisnika
     */
    private String validirajKorisnika(Node korisnik, Long id) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(korisnik, true);
            dok.appendChild(importovan);

            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI("korisnik") + id);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(maper.dobaviSemu("korisnik")))
                    .newValidator().validate(new DOMSource(dok));
            return this.konvertujUString(dok);
        } catch (ParserConfigurationException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj korisnika!");
        }
    }

    /**
     * @param osoba koju treba validirati
     * @param sema  po kojoj treba validirati osobu
     * @return xml reprezentacija osobe
     */
    private String validirajOsobu(Node osoba, String sema, String uriPrefiks, String prefiks, Long korisnikId) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(osoba, true);
            dok.appendChild(importovan);

            ((Element) dok.getFirstChild()).setAttribute("id", uriPrefiks + sekvencer.dobaviId());
            ((Element) dok.getFirstChild().getFirstChild())
                    .setAttribute(prefiks + ":identifikator", maper.dobaviURI("korisnik") + korisnikId);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(sema))
                    .newValidator().validate(new DOMSource(dok));
            return this.konvertujUString(dok);
        } catch (ParserConfigurationException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj osobe!");
        }
    }

    /**
     * @param dokument koji treba konvertuvati u string
     * @return xml reprezentacija dokumenta
     */
    private String konvertujUString(Document dokument) {
        StringWriter w = new StringWriter();

        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(dokument), new StreamResult(w));

            String sadrzaj = w.toString();
            if (sadrzaj.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                sadrzaj = sadrzaj.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            }
            return sadrzaj;
        } catch (TransformerException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
    }
}
