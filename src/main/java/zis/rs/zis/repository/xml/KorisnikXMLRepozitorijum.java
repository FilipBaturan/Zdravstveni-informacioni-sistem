package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.domain.ObjectFactory;
import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.entities.collections.Korisnici;
import zis.rs.zis.domain.enums.TipKorisnika;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;


@Repository
public class KorisnikXMLRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(KorisnikXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Maper maper;

    @Autowired
    private Sekvencer sekvencer;

    public String dobaviSve() {
        return null;
    }

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

    public String[] registruj(Akcija akcija) {
        Document doc = maper.konvertujUDokument(akcija);
        if (doc == null) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        this.registruj(doc);
        return new String[]{"Registracija uspesna!"};
    }

    public String obrisi(String id) {
        String putanja = this.pronadjiKorisnika(id);
        String prefiks = putanja.split("/")[2].split(":")[0];

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("brisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("korisnik"), putanja,
                    maper.dobaviPrefiks("korisnici"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("korisnici"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
            return "Korisnik uspesno obrisan!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    /**
     * @param dokument sa korisnikom i osobom koju treba registovati
     */
    private void registruj(Document dokument) {
        Node sadrzaj = dokument.getElementsByTagName("sadrzaj").item(0);

        if (!sadrzaj.hasChildNodes()) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije, sadzaj akcije je prazan!");
        }
        Node korisnik = this.dobaviKorisnika(sadrzaj.getFirstChild().getChildNodes());
        Node osoba = this.dobaviOsobu(sadrzaj.getFirstChild().getChildNodes());
        if (korisnik == null || osoba == null) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije, sadrzi neodgovarajuce entitete!");
        }
        String korisnikPrefiks = korisnik.getNodeName().split(":")[0];
        String osobaPrefiks = osoba.getNodeName().split(":")[0];
        Long korisnikId = sekvencer.dobaviId();
        Long osobaId = sekvencer.dobaviId();
        String kor = this.validirajKorisnika(korisnik, korisnikId, korisnikPrefiks);
        String osb;
        if (osoba.getLocalName().equals("lekar")) {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("lekar"),
                    maper.dobaviURI("lekar"), osobaPrefiks, korisnikId, TipKorisnika.LEKAR, osobaId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.LEKAR);
        } else if (osoba.getLocalName().equals("medicinska_sestra")) {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("medicinska_sestra"),
                    maper.dobaviURI("medicinska_sestra"), osobaPrefiks, korisnikId,
                    TipKorisnika.MEDICINSKA_SESTRA, osobaId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.MEDICINSKA_SESTRA);
        } else {
            osb = this.validirajOsobu(osoba, maper.dobaviSemu("zdravstveni_karton"),
                    maper.dobaviURI("zdravstveni_karton"), osobaPrefiks, korisnikId,
                    TipKorisnika.PACIJENT, osobaId);
            this.sacuvajKorisnika(kor, korisnikPrefiks);
            this.sacuvajOsobu(osb, osobaPrefiks, TipKorisnika.PACIJENT);
            this.sacuvajPacijenta(maper.dobaviURI("zdravstveni_karton") + osobaId,
                    maper.dobaviURI("korisnik") + korisnikId);
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
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("korisnik"), maper.dobaviPutanju("korisnici"), korisnik,
                    maper.dobaviPrefiks("korisnici"));
            long mods = xupdateService.updateResource(maper.dobaviDokument("korisnici"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
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
            String sadrzajUpita;
            String dokument;
            if (tipKorisnika == TipKorisnika.LEKAR) {
                resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                        maper.dobaviDokument("lekari"));
                sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                        prefiks, maper.dobaviPrefiks("lekar"), maper.dobaviPutanju("lekari"), osoba,
                        maper.dobaviPrefiks("lekari"));
                dokument = maper.dobaviDokument("lekari");
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
                        maper.dobaviDokument("zdravstveni_kartoni"));
                sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                        prefiks, maper.dobaviPrefiks("zdravstveni_karton"),
                        maper.dobaviPutanju("zdravstveni_kartoni"), osoba,
                        maper.dobaviPrefiks("zdravstveni_kartoni"));
                dokument = maper.dobaviDokument("zdravstveni_kartoni");
            }

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(dokument, sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    private void sacuvajPacijenta(String kartonId, String korisnikId) {
        ResursiBaze resursi = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            Document dok = dbf.newDocumentBuilder().newDocument();

            final String PREFIX = "pacijent";
            final String SUFIX = "identifikator";

            Element pacijent = dok.createElementNS(maper.dobaviPrefiks("pacijent"), "pacijent");
            pacijent.setPrefix(PREFIX);
            pacijent.setAttribute("id", maper.dobaviURI("pacijent") + sekvencer.dobaviId());

            Element korisnik = dok.createElement(PREFIX + ":korisnik");
            korisnik.setAttribute(PREFIX + ":" + SUFIX, korisnikId);

            Element karton = dok.createElement(PREFIX + ":zdravstveni_karton");
            karton.setAttribute(PREFIX + ":" + SUFIX, kartonId);

            Element obavestenja = dok.createElement(PREFIX + ":obavestenja");

            pacijent.appendChild(korisnik);
            pacijent.appendChild(karton);
            pacijent.appendChild(obavestenja);

            dok.appendChild(pacijent);

            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("pacijenti"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    PREFIX, maper.dobaviPrefiks("pacijent"), maper.dobaviPutanju("pacijenti"),
                    maper.konvertujUString(dok), maper.dobaviPrefiks("pacijenti"));
            long mods = xupdateService.updateResource(maper.dobaviDokument("pacijenti"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Greska pri obradi podataka!");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
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
    private String validirajKorisnika(Node korisnik, Long id, String prefiks) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(korisnik, true);
            dok.appendChild(importovan);

            this.proveriOgranicenjaKorisnika(dok, prefiks);
            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI("korisnik") + id);
            ((Element) dok.getFirstChild()).setAttribute("aktivan", "true");
            this.dodajMetaPodatkeKorisniku(dok);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(maper.dobaviSemu("korisnik")))
                    .newValidator().validate(new DOMSource(dok));
            return maper.konvertujUString(dok);
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
    private String validirajOsobu(Node osoba, String sema, String uriPrefiks, String prefiks,
                                  Long korisnikId, TipKorisnika tipKorisnika, Long osobaId) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(osoba, true);
            dok.appendChild(importovan);

            String id = uriPrefiks + osobaId;
            ((Element) dok.getFirstChild()).setAttribute("id", id);
            ((Element) dok.getFirstChild().getFirstChild())
                    .setAttribute(prefiks + ":identifikator", maper.dobaviURI("korisnik") + korisnikId);
            if (tipKorisnika == TipKorisnika.LEKAR) {
                dok.getFirstChild().getChildNodes().item(3).setTextContent("0");
            } else if (tipKorisnika == TipKorisnika.PACIJENT) {
                ((Element) dok.getFirstChild()).setAttribute("aktivan", "true");
                this.proveriOgranicenjaPacijenta(dok);
            }
            this.dodajMetaPodatkeOsobi(dok, tipKorisnika, id);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(sema))
                    .newValidator().validate(new DOMSource(dok));
            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj osobe!");
        }
    }


    /**
     * @param dokument nad kojem se dodaju metapodaci korisnika
     */
    private void dodajMetaPodatkeKorisniku(Document dokument) {
        NodeList elementi = dokument.getFirstChild().getChildNodes();
        Element element;
        int count = 0;
        for (int i = 0; i < elementi.getLength() && count < 3; i++) {
            element = (Element) elementi.item(i);
            switch (element.getLocalName()) {
                case "ime":
                    element.setAttribute("property", "voc:ime");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
                case "prezime":
                    element.setAttribute("property", "voc:prezime");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
                case "jmbg":
                    element.setAttribute("property", "voc:jmbg");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
            }
        }
    }

    /**
     * @param dokument     nad kojem se dodaju metapodaci osobe
     * @param tipKorisnika tip korisnika
     */
    private void dodajMetaPodatkeOsobi(Document dokument, TipKorisnika tipKorisnika, String id) {
        NodeList elementi = dokument.getFirstChild().getChildNodes();
        Element element;
        int count = 0;
        switch (tipKorisnika) {
            case LEKAR:
                for (int i = 0; i < elementi.getLength() && count < 2; i++) {
                    element = (Element) elementi.item(i);
                    switch (element.getLocalName()) {
                        case "tip":
                            element.setAttribute("property", "voc:tipLekara");
                            element.setAttribute("datatype", "xs:string");
                            ++count;
                            break;
                        case "oblast_zastite":
                            element.setAttribute("property", "voc:oblastZastite");
                            element.setAttribute("datatype", "xs:string");
                            ++count;
                            break;
                        case "broj_pacijenata":
                            element.setAttribute("property", "voc:brojPacijenata");
                            element.setAttribute("datatype", "xs:integer");
                            ++count;
                            break;
                    }
                }
                break;
            case PACIJENT:
                Element koren = ((Element) dokument.getFirstChild());
                koren.setAttribute("about", id);
                koren.setAttribute("typeof", "voc:ZdrastveniKarton");
                for (int i = 0; i < elementi.getLength() && count < 2; i++) {
                    element = (Element) elementi.item(i);
                    switch (element.getLocalName()) {
                        case "osigurano_lice":
                            this.dodajMetaPodatkePacijentu(element.getChildNodes(), id);
                            ++count;
                            break;
                        case "odabrani_lekar":
                            String lekarId = element.getAttributes().item(0).getNodeValue();
                            element.setAttribute("rel", "voc:odabraniLekar");
                            element.setAttribute("href", lekarId);
                            ++count;
                            break;
                    }
                }
                break;
        }
    }


    /**
     * @param elementi lista elemenata kojoj je potrebno izgenerisati
     *                 URL da ne bi bili prazni cvorovi
     * @return niz generisanih URL
     */
    private String[] generisiURL(NodeList elementi) {
        StringBuilder sba = new StringBuilder("http://www.zis.rs/adresa/");
        StringBuilder sbo = new StringBuilder("http://www.zis.rs/opstina/");
        for (int i = 0; i < elementi.getLength(); i++) {
            Element element;
            try {
                element = (Element) elementi.item(i);
            } catch (ClassCastException e) {
                break;
            }
            if (element.getLocalName().equals("adresa")) {
                NodeList adresa = element.getChildNodes();
                int adresaBrojac = 0;
                for (int j = 0; j < adresa.getLength() && adresaBrojac < 5; j++) {
                    switch (adresa.item(j).getLocalName()) {
                        case "ulica":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "broj":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "broj_stana":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "mesto":
                            sba.append(adresa.item(j).getTextContent());
                            ++adresaBrojac;
                            break;
                        case "opstina":
                            ++adresaBrojac;
                            NodeList opstina = adresa.item(j).getChildNodes();
                            int opstinaBrojac = 0;
                            for (int k = 0; k < opstina.getLength() && opstinaBrojac < 2; k++) {
                                switch (opstina.item(k).getLocalName()) {
                                    case "naziv":
                                        sbo.append(opstina.item(k).getTextContent());
                                        sbo.append("/");
                                        ++opstinaBrojac;
                                        break;
                                    case "postanski_broj":
                                        sbo.append(opstina.item(k).getTextContent());
                                        ++opstinaBrojac;
                                        break;
                                }
                            }
                            break;
                    }
                }
            }
        }
        return new String[]{sba.toString(), sbo.toString()};
    }

    private void dodajMetaPodatkePacijentu(NodeList elementi, String id) {
        String[] urls = this.generisiURL(elementi);
        String adresaURL = urls[0];
        String opstinaURL = urls[1];
        int brojac = 0;
        Element element;
        for (int i = 0; i < elementi.getLength() && brojac < 6; i++) {
            try {
                element = (Element) elementi.item(i);
            } catch (ClassCastException e) {
                break;
            }
            switch (element.getLocalName()) {
                case "ime":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:ime");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "prezime":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:prezime");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "jmbg":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:jmbg");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "pol":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:pol");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "datum_rodjenja":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:jmbg");
                    element.setAttribute("datatype", "xs:date");
                    ++brojac;
                    break;
                case "adresa":
                    element.setAttribute("about", id);
                    element.setAttribute("rel", "voc:stanuje");
                    element.setAttribute("href", adresaURL);
                    ++brojac;
                    NodeList adresa = element.getChildNodes();
                    Element el;
                    int adresaBrojac = 0;
                    for (int j = 0; j < adresa.getLength() && adresaBrojac < 5; j++) {
                        el = (Element) adresa.item(j);
                        switch (el.getLocalName()) {
                            case "ulica":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:ulica");
                                el.setAttribute("datatype", "xs:string");
                                ++adresaBrojac;
                                break;
                            case "broj":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:broj");
                                el.setAttribute("datatype", "xs:integer");
                                ++adresaBrojac;
                                break;
                            case "broj_stana":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:brojStana");
                                el.setAttribute("datatype", "xs:integer");
                                ++adresaBrojac;
                                break;
                            case "mesto":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:mesto");
                                el.setAttribute("datatype", "xs:string");
                                ++adresaBrojac;
                                break;
                            case "opstina":
                                ++adresaBrojac;
                                element.setAttribute("about", adresaURL);
                                element.setAttribute("rel", "voc:opstina");
                                element.setAttribute("href", opstinaURL);
                                NodeList opstina = el.getChildNodes();
                                Element e;
                                int opstinaBrojac = 0;
                                for (int k = 0; k < opstina.getLength() && opstinaBrojac < 2; k++) {
                                    e = (Element) opstina.item(k);
                                    switch (e.getLocalName()) {
                                        case "naziv":
                                            e.setAttribute("about", opstinaURL);
                                            e.setAttribute("property", "voc:nazivOpstine");
                                            e.setAttribute("datatype", "xs:string");
                                            ++opstinaBrojac;
                                            break;
                                        case "postanski_broj":
                                            e.setAttribute("about", opstinaURL);
                                            e.setAttribute("property", "voc:postanskiBroj");
                                            e.setAttribute("datatype", "xs:integer");
                                            ++opstinaBrojac;
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * @param dokument nad kojim se vrsi provera ogranicenja
     * @param prefiks  za prostor imena
     */
    private void proveriOgranicenjaKorisnika(Document dokument, String prefiks) {
        String korisnickoIme = dokument.getElementsByTagName(prefiks + ":korisnicko_ime")
                .item(0).getTextContent();
        String jmbg = dokument.getElementsByTagName(prefiks + ":jmbg").item(0).getTextContent();

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("ogranicenjaKorisnika")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), jmbg, korisnickoIme);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {
                try {
                    res = i.nextResource();
                    sb.append(DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    + res.getContent().toString()))).getFirstChild().getTextContent());
                } finally {
                    if (res != null)
                        ((EXistResource) res).freeResources();
                }
            }
            String greska = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (!greska.isEmpty()) {
                throw new ValidacioniIzuzetak(greska);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException | ParserConfigurationException | SAXException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param document nad kojim se vrsi provera ogranicenja
     */
    private void proveriOgranicenjaPacijenta(Document document) {
        NodeList elementi = document.getFirstChild().getChildNodes();
        for (int i = 0; i < elementi.getLength(); i++) {
            if (elementi.item(i).getLocalName().equals("odabrani_lekar")){
                lekarXMLRepozitorijum.pretragaPoId(elementi.item(i).getAttributes().item(0).getNodeValue());
                break;
            }
        }
        String jmbg = document.getFirstChild().getAttributes().getNamedItem("jmbg").getNodeValue();
        String lbo = document.getFirstChild().getAttributes().getNamedItem("lbo").getNodeValue();
        String broj_kartona = document.getFirstChild().getAttributes().getNamedItem("broj_kartona")
                .getNodeValue();
        String broj_knjizice = document.getFirstChild().getAttributes().getNamedItem("broj_zdr_knjizice")
                .getNodeValue();

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("ogranicenjaKartona")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    jmbg, broj_knjizice, broj_kartona, lbo);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {
                try {
                    res = i.nextResource();
                    sb.append(DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    + res.getContent().toString()))).getFirstChild().getTextContent());
                } finally {
                    if (res != null)
                        ((EXistResource) res).freeResources();
                }
            }
            String greska = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (!greska.isEmpty()) {
                throw new ValidacioniIzuzetak(greska);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException | ParserConfigurationException | SAXException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param id trazenog korisnika
     * @return xpath putanju do pronadjenog korisnika
     */
    private String pronadjiKorisnika(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjePutanje")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    maper.dobaviKolekciju() + maper.dobaviDokument("korisnici"), id);
            logger.info(sadrzajUpita);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;
            String rez;

            try {
                res = i.nextResource();
                rez = (String) res.getContent();
            } finally {
                if (res != null)
                    ((EXistResource) res).freeResources();
            }

            konekcija.oslobodiResurse(resursi);
            if (rez.equals("/")) {
                throw new ValidacioniIzuzetak("Trazeni korisnik ne postoji!");
            }
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    private void izmeniKorisnika(String korisnik, String prefiks) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("izmena")).getPath();
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
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }
}
