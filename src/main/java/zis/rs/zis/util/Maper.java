package zis.rs.zis.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
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

    public Maper() {
        this.xmlBaza = new HashMap<>();
        this.xmlUpiti = new HashMap<>();
        this.xmlSeme = new HashMap<>();
        this.xmlPrefiksi = new HashMap<>();
        this.xmlPutanje = new HashMap<>();
        this.uriPrefiks = new HashMap<>();
        this.inicijalizujMapu();
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

    private void inicijalizujMapu() {
        this.xmlBaza.put("kolekcija", "/db/rs/zis/");
        this.xmlBaza.put("lekari", "lekari.xml");
        this.xmlBaza.put("korisnici", "korisnici.xml");
        this.xmlBaza.put("medicinske_sestre", "medicinske_sestre.xml");
        this.xmlBaza.put("pacijenti", "pacijenti.xml");
        this.xmlBaza.put("pregledi", "pregledi.xml");

        this.xmlUpiti.put("dobaviSveLekare", "classpath:templates/xquery/lekari/dobavljanjeSvihLekara.xqy");
        this.xmlUpiti.put("pretragaPoId", "classpath:templates/xquery/lekari/pretragaPoIdLekara.xqy");
        this.xmlUpiti.put("dodavanje", "classpath:templates/xquery/azuriranje/dodavanje.xml");
        this.xmlUpiti.put("prebrojavanje", "classpath:templates/xquery/azuriranje/prebrojavanjeSvihEntiteta.xq");
        this.xmlUpiti.put("ogranicenjaKorisnika",
                "classpath:templates/xquery/korisnici/proveraJedinstvenihPoljaKorisnika.xq");
        this.xmlUpiti.put("izmena", "classpath:templates/xquery/azuriranje/izmena.xml");
        this.xmlUpiti.put("dobavljanjePutanje", "classpath:templates/xquery/azuriranje/dobavljanjePutanje.xq");
        this.xmlUpiti.put("brisanje", "classpath:templates/xquery/azuriranje/brisanje.xml");
        this.xmlUpiti.put("pretragaPoIdPregleda", "classpath:templates/xquery/pregledi/pretragaPoIdPregleda.xqy");

        this.xmlSeme.put("akcija", "classpath:static/seme/akcija.xsd");
        this.xmlSeme.put("korisnik", "classpath:static/seme/korisnik.xsd");
        this.xmlSeme.put("lekar", "classpath:static/seme/lekar.xsd");
        this.xmlSeme.put("medicinska_sestra", "classpath:static/seme/medicinska_sestra.xsd");
        this.xmlSeme.put("zdravstveni_karton", "classpath:static/seme/zdravstveni_karton.xsd");

        this.xmlPrefiksi.put("korisnik", "http://www.zis.rs/seme/korisnik");
        this.xmlPrefiksi.put("korisnici", "xmlns:ko=\"http://www.zis.rs/seme/korisnici\"");
        this.xmlPrefiksi.put("lekar", "http://www.zis.rs/seme/lekar");
        this.xmlPrefiksi.put("lekari", "xmlns:lekari=\"http://www.zis.rs/seme/lekari\"");
        this.xmlPrefiksi.put("medicinska_sestra", "http://www.zis.rs/seme/medicinska_sestra");
        this.xmlPrefiksi.put("medicinske_sestre",
                "xmlns:medicinske_sestre=\"http://www.zis.rs/seme/medicinske_sestre\"");

        this.xmlPrefiksi.put("pregled", "http://www.zis.rs/seme/pregled");
        this.xmlPrefiksi.put("pregledi", "xmlns:pr =\"http://www.zis.rs/seme/pregledi\"");

        this.xmlPutanje.put("korisnici", "/ko:korisnici");
        this.xmlPutanje.put("lekari", "/lekari:lekari");
        this.xmlPutanje.put("medicinske_sestre", "/medicinske_sestre:medicinske_sestre");

        this.xmlPutanje.put("pregledi", "/pr:pregledi");

        this.uriPrefiks.put("korisnik", "http://www.zis.rs/korisnici/id");
        this.uriPrefiks.put("lekar", "http://www.zis.rs/lekari/id");
        this.uriPrefiks.put("medicinska_sestra", "http://www.zis.rs/medicinska_sestra/id");
        this.uriPrefiks.put("pacijenti", "http://www.zis.rs/pacijenti/id");

    }

    public Document koverturjUDokument(String xmlSadrzaj)
    {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlSadrzaj));

            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
           throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }
    }


}
