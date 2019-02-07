package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.domain.DTO.Prijava;
import zis.rs.zis.domain.enums.TipKorisnika;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;


@Repository
public class KorisnikXMLRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(KorisnikXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum zdravstveniKartonXMLRepozitorijum;

    @Autowired
    private OgranicenjaRepozitorijum ogranicenjaRepozitorijum;

    @Autowired
    private GeneratorMetaPodataka generatorMetaPodataka;

    @Autowired
    private Maper maper;

    @Autowired
    private Sekvencer sekvencer;

    public Prijava prijava(String korisnickoIme, String lozinka) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("prijava")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), korisnickoIme, lozinka);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {

                try {
                    res = i.nextResource();
                    sb.append(res.getContent().toString());
                    sb.append("-");
                } finally {
                    if (res != null)
                        ((EXistResource) res).freeResources();

                }
            }
            konekcija.oslobodiResurse(resursi);
            if (sb.toString().isEmpty()) {
                throw new ValidacioniIzuzetak("Pogresno korisnicko ime ili lozinka!");
            } else {
                return new Prijava(sb.toString());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String[] registruj(Akcija akcija) {
        Document doc = maper.konvertujUDokument(akcija);
        if (doc == null) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        return this.registruj(doc);
    }

    public String obrisi(String id) {
        String putanja = ogranicenjaRepozitorijum.pronalazenjePutanje(id, "korisnici",
                "Trazeni korisnik ne postoji!");
        String prefiks = putanja.split("/")[2].split(":")[0];
        String kartonId = this.dobaviKarton(id);
        if (!kartonId.isEmpty()) {
            zdravstveniKartonXMLRepozitorijum.obrisi(kartonId);
        }

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
     * @param id korisnika ciji zdravstveni karton treba dobaviti
     * @return id zdravstvenog kartona
     */
    private String dobaviKarton(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjeKartonaPrekoKorisnika"))
                    .getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), id);
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
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param dokument sa korisnikom i osobom koju treba registovati
     */
    private String[] registruj(Document dokument) {
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
        return new String[]{kor, osb};
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
            try {
                element = lista.item(i);
                if (element.getLocalName().equals("korisnik")) {
                    return element;
                }
            } catch (Exception ignore) {}
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
            try {
                element = lista.item(i);
                if (element.getLocalName().equals("lekar") ||
                        element.getLocalName().equals("medicinska_sestra") ||
                        element.getLocalName().equals("zdravstveni_karton")) {
                    return element;
                }
            } catch (Exception ignored) {}

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

            ogranicenjaRepozitorijum.proveriOgranicenjaKorisnika(dok, prefiks);
            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI("korisnik") + id);
            ((Element) dok.getFirstChild()).setAttribute("aktivan", "true");
            generatorMetaPodataka.dodajMetaPodatkeKorisniku(dok);

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

            if (tipKorisnika == TipKorisnika.PACIJENT) {
                ((Element) dok.getFirstChild()).setAttribute("aktivan", "true");
                ogranicenjaRepozitorijum.proveriOgranicenjaPacijenta(dok);
            }
            generatorMetaPodataka.dodajMetaPodatkeOsobi(dok, tipKorisnika, id);

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
}
