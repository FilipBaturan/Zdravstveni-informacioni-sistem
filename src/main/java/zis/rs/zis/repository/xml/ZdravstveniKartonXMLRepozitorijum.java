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
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.domain.enums.TipKorisnika;
import zis.rs.zis.util.*;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Repository
public class ZdravstveniKartonXMLRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(ZdravstveniKartonXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private Operacije operacije;

    @Autowired
    private OgranicenjaRepozitorijum ogranicenjaRepozitorijum;

    @Autowired
    private GeneratorMetaPodataka generatorMetaPodataka;

    @Autowired
    private Maper maper;

    @Autowired
    private Validator validator;

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, "zdravstveni_kartoni", "pretragaPoIdKartona");
    }

    public void obrisi(String id) {
        String putanja = ogranicenjaRepozitorijum.pronalazenjePutanje(id, "zdravstveni_kartoni",
                "Trazeni zdravstveni karton ne postoji!");
        String prefiks = putanja.split("/")[2].split(":")[0];
        String prePrefiks = putanja.split("/")[1].split(":")[0];

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("brisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("zdravstveni_karton"), putanja,
                    maper.dobaviPrefiks("zdravstveni_kartoni").replace(":zdravstveni_kartoni",
                            ":" + prePrefiks));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("zdravstveni_kartoni"), sadrzajUpita);
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
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String izmeniKarton(Akcija akcija) {
        validator.procesirajAkciju(akcija, maper.dobaviSemu("zdravstveni_karton"));
        Document dok = maper.konvertujUDokument(akcija);
        String id = dok.getFirstChild().getLastChild().getFirstChild().getAttributes()
                .getNamedItem("id").getNodeValue();
        String lekarId = dok.getFirstChild().getLastChild().getFirstChild().getChildNodes().item(1)
                .getAttributes().item(0).getNodeValue();
        lekarXMLRepozitorijum.pretragaPoId(lekarId);
        ResursiBaze resursi = null;
        String rezerva = this.pretragaPoId(id);
        String putanja = ogranicenjaRepozitorijum.pronalazenjePutanje(id, "zdravstveni_kartoni",
                "Trazeni zdravstveni karton ne postoji!");
        String prefiks = putanja.split("/")[2].split(":")[0];
        String prePrefiks = putanja.split("/")[1].split(":")[0];
        try {
            DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
            fabrika.setNamespaceAware(true);
            Node karton = dok.getFirstChild().getLastChild().getFirstChild();
            dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(karton, true);
            dok.appendChild(importovan);

            generatorMetaPodataka.dodajMetaPodatkeOsobi(dok, TipKorisnika.PACIJENT, id);

            this.fizickoBrisanje(prefiks, prePrefiks, putanja);
            ogranicenjaRepozitorijum.proveriOgranicenjaPacijenta(dok);
            String izmena = maper.konvertujUString(dok);

            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("zdravstveni_karton"),
                    maper.dobaviPutanju("zdravstveni_kartoni"), izmena,
                    maper.dobaviPrefiks("zdravstveni_kartoni"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("zdravstveni_kartoni"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka");
        } catch (ValidacioniIzuzetak e) {
            this.dodajKarton(rezerva, prefiks);
            return e.getMessage();
        }
        this.izmeniKorisnika(dok, id);
        return "Zdravstveni karton uspesno izmenjen!";
    }


    /**
     * @param dokument zdravstvenog kartona
     * @param id zdravstvenog kartona
     */
    private void izmeniKorisnika(Document dokument, String id) {
        String jmbg = dokument.getFirstChild().getAttributes().getNamedItem("jmbg").getNodeValue();
        String ime = dokument.getFirstChild().getFirstChild().getChildNodes().item(0).getTextContent();
        String prezime = dokument.getFirstChild().getFirstChild().getChildNodes().item(1).getTextContent();

        Document korisnik = maper.konvertujUDokument(dobaviKorisnika(id));
        String korisnikId = korisnik.getFirstChild().getAttributes().getNamedItem("id").getNodeValue();
        NodeList elementi = korisnik.getFirstChild().getChildNodes();
        Element element;
        for (int i = 0; i < elementi.getLength(); i++) {
            try {
            element = (Element) elementi.item(i);
                switch (element.getTagName()) {
                    case "korisnik:jmbg":
                        element.setTextContent(jmbg);
                        break;
                    case "korisnik:ime":
                        element.setTextContent(ime);
                        break;
                    case "korisnik:prezime":
                        element.setTextContent(prezime);
                        break;
                }
            } catch (ClassCastException | NullPointerException e) {
            }

        }

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("izmena")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "korisnik", maper.dobaviPrefiks("korisnik"),
                    ogranicenjaRepozitorijum.pronalazenjePutanje(korisnikId, "korisnici",
                            "Trazeni korisnik ne postoji"),
                    maper.kreirajXmlOdCvorova(korisnik.getFirstChild().getChildNodes()), maper.dobaviPrefiks("korisnici"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("korisnici"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
        } catch (Exception e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param prefiks    zdravstvenog kartona
     * @param prePrefiks zdravstvenih kartona
     * @param putanja    na kojoj se nalazi zdravsteni karton
     */
    private void fizickoBrisanje(String prefiks, String prePrefiks, String putanja) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("fizickoBrisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("zdravstveni_karton"), putanja,
                    maper.dobaviPrefiks("zdravstveni_kartoni").replace(":zdravstveni_kartoni",
                            ":" + prePrefiks));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("zdravstveni_kartoni"), sadrzajUpita);
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
     * @param karton koji treba dodati u bazu
     * @param prefiks naziv prostora imena
     */
    private void dodajKarton(String karton, String prefiks) {
        ResursiBaze resursi = null;
        try {
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            String sadrzajUpita;
            String dokument;
                resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                        maper.dobaviDokument("zdravstveni_kartoni"));
                sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                        prefiks, maper.dobaviPrefiks("zdravstveni_karton"),
                        maper.dobaviPutanju("zdravstveni_kartoni"), karton,
                        maper.dobaviPrefiks("zdravstveni_kartoni"));
                dokument = maper.dobaviDokument("zdravstveni_kartoni");

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

    /**
     * @param kartonId id kartona
     * @return pronadjen korisnik
     */
    private String dobaviKorisnika(String kartonId) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("zdravstveni_kartoni"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjeKorisnikaPrekoKartona"))
                    .getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), kartonId);
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
            String korisnik = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (korisnik.isEmpty()) {
                throw new ValidacioniIzuzetak("Trazeni korisnik ne postoji!");
            } else {
                return korisnik;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }
}
