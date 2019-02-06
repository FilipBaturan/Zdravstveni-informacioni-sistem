package zis.rs.zis.util;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class Maper {

    private Map<String, String> xmlBaza;
    private Map<String, String> xmlUpiti;
    private Map<String, String> xmlSeme;
    private Map<String, String> xmlPrefiksi;
    private Map<String, String> xmlPutanje;
    private Map<String, String> uriPrefiks;
    private Map<String, String> transformacije;
    private Map<String, String> grafovi;

    public Maper() {
        this.xmlBaza = new HashMap<>();
        this.xmlUpiti = new HashMap<>();
        this.xmlSeme = new HashMap<>();
        this.xmlPrefiksi = new HashMap<>();
        this.xmlPutanje = new HashMap<>();
        this.uriPrefiks = new HashMap<>();
        this.transformacije = new HashMap<>();
        this.grafovi = new HashMap<>();
        this.inicijalizujMapu();
    }

    private void inicijalizujMapu() {
        this.xmlBaza.put("kolekcija", "/db/rs/zis/");
        this.xmlBaza.put("lekari", "lekari.xml");
        this.xmlBaza.put("korisnici", "korisnici.xml");
        this.xmlBaza.put("medicinske_sestre", "medicinske_sestre.xml");
        this.xmlBaza.put("pacijenti", "pacijenti.xml");
        this.xmlBaza.put("pregledi", "pregledi.xml");
        this.xmlBaza.put("stanja_pregleda", "stanja_pregleda.xml");
        this.xmlBaza.put("izvestaji", "izvestaji.xml");
        this.xmlBaza.put("zdravstveni_kartoni", "zdravstveni_kartoni.xml");
        this.xmlBaza.put("lekovi", "lekovi.xml");
        this.xmlBaza.put("recepti", "recepti.xml");
        this.xmlBaza.put("uputi", "uputi.xml");
        this.xmlBaza.put("izbori", "izbori.xml");

        this.xmlUpiti.put("dobaviSveLekare", "classpath:templates/xquery/lekari/dobavljanjeSvihLekara.xqy");
        this.xmlUpiti.put("pretragaPoIdLekara", "classpath:templates/xquery/lekari/pretragaPoIdLekara.xqy");
        this.xmlUpiti.put("dodavanje", "classpath:templates/xquery/azuriranje/dodavanje.xml");
        this.xmlUpiti.put("prebrojavanje", "classpath:templates/xquery/azuriranje/prebrojavanjeSvihEntiteta.xq");
        this.xmlUpiti.put("ogranicenjaKorisnika",
                "classpath:templates/xquery/korisnici/proveraJedinstvenihPoljaKorisnika.xq");
        this.xmlUpiti.put("izmena", "classpath:templates/xquery/azuriranje/izmena.xml");
        this.xmlUpiti.put("dobavljanjePutanje", "classpath:templates/xquery/azuriranje/dobavljanjePutanje.xq");
        this.xmlUpiti.put("brisanje", "classpath:templates/xquery/azuriranje/brisanje.xml");
        this.xmlUpiti.put("pretragaPoIdPregleda", "classpath:templates/xquery/pregledi/pretragaPoIdPregleda.xqy");
        this.xmlUpiti.put("ogranicenjaPregleda",
                "classpath:templates/xquery/pregledi/proveraJedinstvenihPoljaPregleda.xq");
        this.xmlUpiti.put("dobavljanjePutanjeStanja",
                "classpath:templates/xquery/azuriranje/dobavljanjePutanjeStanjaPacijenta.xq");
        this.xmlUpiti.put("ogranicenjaKartona",
                "classpath:templates/xquery/zdravstveni_kartoni/proveraJedinstvenostiPoljaKartona.xq");
        this.xmlUpiti.put("dobavljanjeKartonaPrekoKorisnika",
                "classpath:templates/xquery/zdravstveni_kartoni/dobavljanjeKartonaPrekoKorisnika.xq");
        this.xmlUpiti.put("fizickoBrisanje", "classpath:templates/xquery/azuriranje/fizickoBrisanje.xml");
        this.xmlUpiti.put("pretragaPoIdKartona",
                "classpath:templates/xquery/zdravstveni_kartoni/pretragaPoIdKartona.xq");
        this.xmlUpiti.put("pretragaPoIdKorisnika",
                "classpath:templates/xquery/korisnici/pretragaPoIdKorisnika.xqy");
        this.xmlUpiti.put("dobavljanjeKorisnikaPrekoKartona",
                "classpath:templates/xquery/korisnici/dobavljanjeKorisnikaPrekoKartona.xq");
        this.xmlUpiti.put("dobaviSveLekove", "classpath:templates/xquery/lekovi/dobavljanjeSvihLekova.xqy");
        this.xmlUpiti.put("pretragaPoIdLeka", "classpath:templates/xquery/lekovi/pretragaPoIdLeka.xqy");
        this.xmlUpiti.put("pretragaPoIdIzvestaja", "classpath:templates/xquery/izvestaji/pretragaPoIdIzvestaja.xqy");
        this.xmlUpiti.put("dobaviSveIzvestaje", "classpath:templates/xquery/izvestaji/dobavljanjeSvihIzvestaja.xqy");
        this.xmlUpiti.put("dobaviSveRecepte", "classpath:templates/xquery/recepti/dobavljanjeSvihRecepata.xqy");
        this.xmlUpiti.put("pretragaPoIdRecepta", "classpath:templates/xquery/recepti/pretragaPoIdRecepta.xqy");
        this.xmlUpiti.put("dobaviSveUpute", "classpath:templates/xquery/uputi/dobavljanjeSvihUputa.xqy");
        this.xmlUpiti.put("pretragaPoIdUputa", "classpath:templates/xquery/uputi/pretragaPoIdUputa.xqy");
        this.xmlUpiti.put("pretragaPoIdIzbora", "classpath:templates/xquery/izbori/pretragaPoIdIzbora.xqy");
        this.xmlUpiti.put("dobaviSveIzbore", "classpath:templates/xquery/izbori/dobavljanjeSvihIzbora.xqy");
        this.xmlUpiti.put("dobavljanjeSvihKartona",
                "classpath:templates/xquery/zdravstveni_kartoni/dobavljanjeSvihKartona.xq");
        this.xmlUpiti.put("dobavljanjeSvega", "classpath:templates/xquery/sekvencer/dobavljaneBrojaSvihEntiteta.xqy");
        this.xmlUpiti.put("dobavljanjeLekovaZaDijagnozu", "classpath:templates/xquery/lekovi/dobaviLekoveZaDijagnozu.xqy");
        this.xmlUpiti.put("opstaPretragaKartona", "classpath:templates/xquery/zdravstveni_kartoni/pretragaPoSadrzajuKartona.xqy");


        this.xmlSeme.put("akcija", "classpath:static/seme/akcija.xsd");
        this.xmlSeme.put("korisnik", "classpath:static/seme/korisnik.xsd");
        this.xmlSeme.put("lekar", "classpath:static/seme/lekar.xsd");
        this.xmlSeme.put("medicinska_sestra", "classpath:static/seme/medicinska_sestra.xsd");
        this.xmlSeme.put("zdravstveni_karton", "classpath:static/seme/zdravstveni_karton.xsd");
        this.xmlSeme.put("pregled", "classpath:static/seme/pregled.xsd");
        this.xmlSeme.put("izvestaj", "classpath:static/seme/izvestaj.xsd");
        this.xmlSeme.put("lek", "classpath:static/seme/lek.xsd");
        this.xmlSeme.put("recept", "classpath:static/seme/recept.xsd");
        this.xmlSeme.put("uput", "classpath:static/seme/uput.xsd");
        this.xmlSeme.put("izbor", "classpath:static/seme/izbor.xsd");


        this.xmlPrefiksi.put("korisnik", "http://www.zis.rs/seme/korisnik");
        this.xmlPrefiksi.put("korisnici", "xmlns:ko=\"http://www.zis.rs/seme/korisnici\"");
        this.xmlPrefiksi.put("lekar", "http://www.zis.rs/seme/lekar");
        this.xmlPrefiksi.put("lekari", "xmlns:lekari=\"http://www.zis.rs/seme/lekari\"");
        this.xmlPrefiksi.put("medicinska_sestra", "http://www.zis.rs/seme/medicinska_sestra");
        this.xmlPrefiksi.put("medicinske_sestre",
                "xmlns:medicinske_sestre=\"http://www.zis.rs/seme/medicinske_sestre\"");
        this.xmlPrefiksi.put("pregled", "http://www.zis.rs/seme/pregled");
        this.xmlPrefiksi.put("pregledi", "xmlns:pr =\"http://www.zis.rs/seme/pregledi\"");
        this.xmlPrefiksi.put("stanje_pregleda", "http://www.zis.rs/seme/stanje_pregleda");
        this.xmlPrefiksi.put("stanja_pregleda", "xmlns:sp=\"http://www.zis.rs/seme/stanja_pregleda\"");
        this.xmlPrefiksi.put("izvestaj", "http://www.zis.rs/seme/izvestaj");
        this.xmlPrefiksi.put("izvestaji", "xmlns:izvestaji =\"http://www.zis.rs/seme/izvestaji\"");
        this.xmlPrefiksi.put("zdravstveni_karton", "http://www.zis.rs/seme/zdravstveni_karton");
        this.xmlPrefiksi.put("zdravstveni_kartoni",
                "xmlns:zdravstveni_kartoni =\"http://www.zis.rs/seme/zdravstveni_kartoni\"");
        this.xmlPrefiksi.put("pacijent", "http://www.zis.rs/seme/pacijent");
        this.xmlPrefiksi.put("pacijenti", "xmlns:pacijenti =\"http://www.zis.rs/seme/pacijenti\"");
        this.xmlPrefiksi.put("lek", "http://www.zis.rs/seme/lek");
        this.xmlPrefiksi.put("lekovi", "xmlns:lekovi =\"http://www.zis.rs/seme/lekovi\"");
        this.xmlPrefiksi.put("recept", "http://www.zis.rs/seme/recept");
        this.xmlPrefiksi.put("recepti", "xmlns:recepti =\"http://www.zis.rs/seme/recepti\"");
        this.xmlPrefiksi.put("uput", "http://www.zis.rs/seme/uput");
        this.xmlPrefiksi.put("uputi", "xmlns:uputi =\"http://www.zis.rs/seme/uputi\"");
        this.xmlPrefiksi.put("izbor", "http://www.zis.rs/seme/izbor");
        this.xmlPrefiksi.put("izbori", "xmlns:izbori =\"http://www.zis.rs/seme/izbori\"");
        this.xmlPrefiksi.put("vokabular", "xmlns:voc=\"http://www.zis.rs/rdf/voc#\"");
        this.xmlPrefiksi.put("xmlSema", "  xmlns:xs=\"http://www.w3.org/2001/XMLSchema#\"  ");

        this.xmlPutanje.put("korisnici", "/ko:korisnici");
        this.xmlPutanje.put("lekari", "/lekari:lekari");
        this.xmlPutanje.put("medicinske_sestre", "/medicinske_sestre:medicinske_sestre");
        this.xmlPutanje.put("pregledi", "/pr:pregledi");
        this.xmlPutanje.put("stanja_pregleda", "/sp:stanja_pregleda");
        this.xmlPutanje.put("izvestaji", "/izvestaji:izvestaji");
        this.xmlPutanje.put("zdravstveni_kartoni", "/zdravstveni_kartoni:zdravstveni_kartoni");
        this.xmlPutanje.put("pacijenti", "/pacijenti:pacijenti");
        this.xmlPutanje.put("lekovi", "/lekovi:lekovi");
        this.xmlPutanje.put("recepti", "/recepti:recepti");
        this.xmlPutanje.put("uputi", "/uputi:uputi");
        this.xmlPutanje.put("izbori", "/izbori:izbori");
        this.xmlPutanje.put("brisanjePovezanihRecepata",
                "/recepti:recepti/recept:recept[recept:osigurano_lice/@recept:identifikator = '%1$s']");
        this.xmlPutanje.put("brisanjePovezanihUputa",
                "/uputi:uputi/uput:uput[uput:osigurano_lice/@uput:identifikator = '%1$s']");
        this.xmlPutanje.put("brisanjePovezanihIzvestaja",
                "/izvestaji:izvestaji/izvestaj:izvestaj[izvestaj:osigurano_lice/@izvestaj:identifikator = '%1$s']");
        this.xmlPutanje.put("brisanjePovezanihIzbora",
                "/izbori:izbori/izbor:izbor[izbor:osigurano_lice/@izbor:identifikator = '%1$s']");

        this.uriPrefiks.put("korisnik", "http://www.zis.rs/korisnici/id");
        this.uriPrefiks.put("lekar", "http://www.zis.rs/lekari/id");
        this.uriPrefiks.put("medicinska_sestra", "http://www.zis.rs/medicinska_sestra/id");
        this.uriPrefiks.put("pacijent", "http://www.zis.rs/pacijenti/id");
        this.uriPrefiks.put("pregled", "http://www.zis.rs/pregledi/id");
        this.uriPrefiks.put("izvestaj", "http://www.zis.rs/izvestaji/id");
        this.uriPrefiks.put("zdravstveni_karton", "http://www.zis.rs/zdravstveni_kartoni/id");
        this.uriPrefiks.put("lek", "http://www.zis.rs/lekovi/id");
        this.uriPrefiks.put("recept", "http://www.zis.rs/recepti/id");
        this.uriPrefiks.put("uput", "http://www.zis.rs/uputi/id");
        this.uriPrefiks.put("izbor", "http://www.zis.rs/izbori/id");
        this.uriPrefiks.put("vokabular", "http://www.zis.rs/rdf/voc#");

        this.transformacije.put("metapodaci", "classpath:xsl/grddl.xsl");

        this.grafovi.put("lekari", "lekari");
        this.grafovi.put("zdravstveni_kartoni", "zdravstveni_kartoni");
        this.grafovi.put("uputi", "uputi");
        this.grafovi.put("izvestaji", "izvestaji");
        this.grafovi.put("recepti", "recepti");
        this.grafovi.put("lekovi", "lekovi");
        this.grafovi.put("izbori", "izbori");
    }

    /**
     * @param xmlSadrzaj koji treba konvertovati u dokument
     * @return kreirani dokument
     */
    public Document konvertujUDokument(String xmlSadrzaj) {

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlSadrzaj));

            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }
    }

    /**
     * @param cvorovi od kojih je potrebno napraviti xml dokument
     * @return xml dokument
     * @throws Exception izuzetak
     */
    public String kreirajXmlOdCvorova(NodeList cvorovi) throws Exception {
        StringWriter buf = new StringWriter();
        for (int i = 0; i < cvorovi.getLength(); i++) {
            Node elem = cvorovi.item(i);//Your Node

            Transformer xform = TransformerFactory.newInstance().newTransformer();
            xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            xform.setOutputProperty(OutputKeys.INDENT, "yes");
            xform.transform(new DOMSource(elem), new StreamResult(buf));
        }
        return buf.toString();
    }

    /**
     * @param akcija koju treba konvertovati u dokument
     * @return kreirani dokument
     */
    public Document konvertujUDokument(Akcija akcija) {
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
     * @param dokument koji treba konvertovati u string
     * @return string reprezentacija dokumenta
     */
    public String konvertujUString(Document dokument) {
        StringWriter w = new StringWriter();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
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
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }
    }

    /**
     * @param element koji treba konvertovati u string
     * @return string reprezentacija elementa
     */
    public String konvertujUString(Node element) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(element, true);
            dok.appendChild(importovan);

            StringWriter w = new StringWriter();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(dok), new StreamResult(w));

            String sadrzaj = w.toString();
            if (sadrzaj.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                sadrzaj = sadrzaj.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            }
            return sadrzaj;

        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (TransformerException e) {
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return id pacijenta
     */
    public String dobaviPacijentaIzPregleda(Akcija akcija) {
        return this.konvertujUDokument(akcija).getFirstChild().getLastChild().getFirstChild().
                getChildNodes().item(2).getAttributes().item(0).getNodeValue();
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return tip lekara kod koga se zakazuje pregled
     */
    public String dobaviTipLekaraIzPregleda(Akcija akcija) {
        return this.konvertujUDokument(akcija).getFirstChild().getLastChild().getFirstChild()
                .getAttributes().getNamedItem("tip").getNodeValue();
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return id pacijenta
     */
    public String dobaviPacijentaIzIzvestaja(Akcija akcija) {
        return this.konvertujUDokument(akcija).getFirstChild().getLastChild().getFirstChild().getChildNodes().item(4)
                .getAttributes().item(0).getNodeValue();
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return id pacijenta
     */
    public String dobaviPacijentaIzDokumentacije(Akcija akcija) {
        NodeList lista = konvertujUDokument(akcija).getFirstChild().getLastChild().getFirstChild().getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals("izvestaj")) {
                return element.getChildNodes().item(4)
                        .getAttributes().item(0).getNodeValue();
            }
        }
        throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije!");
    }

    public String dobaviPacijentaIzPromeneLekara(Akcija akcija) {
        NodeList lista = konvertujUDokument(akcija).getFirstChild().getLastChild().getFirstChild().getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals("osigurano_lice")) {
                return element.getAttributes().item(0).getNodeValue();
            }
        }
        throw new ValidacioniIzuzetak("Nevalidan sadrzaj akcije!");
    }

    /**
     * @param akcija         koja se prosledjuje
     * @param nazivDokumenta koji treba da se dobavi
     * @return dokument u obliku cvora
     */
    public Node dobaviDokument(Akcija akcija, String nazivDokumenta) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        NodeList lista = dok.getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals(nazivDokumenta)) {
                return element;
            }
        }
        return null;
    }

    public String dobaviKolekciju() {
        return this.xmlBaza.get("kolekcija");
    }

    public String dobaviDokument(String naziv) {
        return this.xmlBaza.get(naziv);
    }

    public String dobaviUpit(String naziv) {
        return this.xmlUpiti.get(naziv);
    }

    public String dobaviSemu(String naziv) {
        return this.xmlSeme.get(naziv);
    }

    public String dobaviPrefiks(String naziv) {
        return this.xmlPrefiksi.get(naziv);
    }

    public String dobaviPutanju(String naziv) {
        return this.xmlPutanje.get(naziv);
    }

    public String dobaviURI(String naziv) {
        return this.uriPrefiks.get(naziv);
    }

    public String dobaviTransformaciju(String naziv) {
        return this.transformacije.get(naziv);
    }

    public String dobaviGraf(String naziv) { return this.grafovi.get(naziv); }

}