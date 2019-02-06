package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Repository
public class OgranicenjaRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(OgranicenjaRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    /**
     * @param dokument nad kojim se vrsi provera ogranicenja
     * @param prefiks  za prostor imena
     */
    public void proveriOgranicenjaKorisnika(Document dokument, String prefiks) {
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
    public void proveriOgranicenjaPacijenta(Document document) {
        NodeList elementi = document.getFirstChild().getChildNodes();
        for (int i = 0; i < elementi.getLength(); i++) {
            if (elementi.item(i).getLocalName().equals("odabrani_lekar")) {
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

    public void proveriOgranicenjaIzboraLekara(String karton, String prosliLekar, String lekar) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("izbori"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("ogranicenjaIzbora")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), karton,
                    prosliLekar, lekar);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {
                try {
                    res = i.nextResource();
                    sb.append(res.getContent().toString());
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
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param id trazenog korisnika
     * @return xpath putanju do pronadjenog korisnika
     */
    public String pronalazenjePutanje(String id, String dokument, String greska) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjePutanje")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    maper.dobaviKolekciju() + maper.dobaviDokument(dokument), id);
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
                throw new ValidacioniIzuzetak(greska);
            }
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }
}
