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
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Repository
public class IzvestajXMLRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(IzvestajXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private Validator validator;

    @Autowired
    private Sekvencer sekvencer;

    private String dokument = "izvestaji";
    private String prefiksDokumenta = "izvestaj";


    public String pretragaPoId(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("pretragaPoIdIzvestaja")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), id);
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
            String izvestaj = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (izvestaj.isEmpty()) {
                throw new ValidacioniIzuzetak("Ne postoji ni jedan izvestaj u bazi!");
            } else {
                return izvestaj;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String sacuvaj(Node sadrzaj) {
        String izvestaj = validator.procesirajAkciju(sadrzaj, maper.dobaviSemu(prefiksDokumenta));


        String prefiks = maper.konvertujUDokument(izvestaj).getFirstChild().getNodeName().split(":")[0];
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            Long id = sekvencer.dobaviId();

            String dodatId = this.umetniId(maper.konvertujUDokument(izvestaj).getFirstChild(), id, prefiks);

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), maper.dobaviPutanju(dokument), dodatId,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom kreiranja izvestaja!");
            }

            konekcija.oslobodiResurse(resursi);
            return "Uspesno sacuvan izvestaj!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String obrisi(Akcija akcija) {

        String id = akcija.getKontekst();
        String izvestaj = pretragaPoId(id);
        String prefiks = maper.konvertujUDokument(izvestaj).getFirstChild().getNodeName().split(":")[0];

        String putanjaDoIzvestaja = pronadjiIzvestaj(id);
        ResursiBaze resursi = null;

        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("brisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), putanjaDoIzvestaja,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
            return "Izvestaj uspesno obrisan!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String izmeni(Akcija akcija) {

        String izvestaj = validator.procesirajAkciju(akcija, maper.dobaviSemu(prefiksDokumenta));
        Node cvor = maper.konvertujUDokument(izvestaj).getFirstChild();
        Element el = (Element) cvor;
        String id = el.getAttribute("id");


        String prefiks = maper.konvertujUDokument(izvestaj).getFirstChild().getNodeName().split(":")[0];
        NodeList cvoroviIzvestaja = maper.konvertujUDokument(izvestaj).getFirstChild().getChildNodes();

        String sadrzajIzvestaja = null;
        try {
            sadrzajIzvestaja = maper.kreirajXmlOdCvorova(cvoroviIzvestaja);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("izmena")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), pronadjiIzvestaj(id), sadrzajIzvestaja,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            return "Uspesno izmenjen izvestaj!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    /**
     * @param izvestaj kojeg treba izmeniti, id koji treba ubaciti i prefiks namespace
     * @return izmenjena reprezentacija pregleda
     */
    private String umetniId(Node izvestaj, Long id, String prefiks) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(izvestaj, true);
            dok.appendChild(importovan);

            this.proveriOgranicenjaPregleda(dok, prefiks);
            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI("izvestaj") + id);

            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (ValidacioniIzuzetak e) {
            throw new ValidacioniIzuzetak(e.getMessage());
        }
    }

    /**
     * @param id trazenog pregleda
     * @return xpath putanju do pronadjenog pregleda
     */
    private String pronadjiIzvestaj(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjePutanje")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
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
                throw new ValidacioniIzuzetak("Trazeni izvestaj ne postoji!");
            }
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    private void proveriOgranicenjaPregleda(Document dokument, String prefiks) {
//        String lekar = dokument.getElementsByTagName(prefiks + ":lekar")
//                .item(0).getAttributes().item(0).getNodeValue();
//
//        String datum = dokument.getElementsByTagName(prefiks + ":datum").item(0).getTextContent();
//
//        ResursiBaze resursi = null;
//        try {
//            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("pregledi"));
//            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("ogranicenjaPregleda")).getPath();
//            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
//            upitServis.setProperty("indent", "yes");
//            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), lekar, datum);
//            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
//            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
//            ResourceIterator i = rezultat.getIterator();
//            Resource res = null;
//
//            StringBuilder sb = new StringBuilder();
//
//            while (i.hasMoreResources()) {
//                try {
//                    res = i.nextResource();
//                    sb.append(DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                            .parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//                                    + res.getContent().toString()))).getFirstChild().getTextContent());
//                } finally {
//                    if (res != null)
//                        ((EXistResource) res).freeResources();
//                }
//            }
//            String greska = sb.toString();
//            konekcija.oslobodiResurse(resursi);
//            if (!greska.isEmpty()) {
//                throw new ValidacioniIzuzetak(greska);
//            }
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
//                IOException | ParserConfigurationException | SAXException e) {
//            konekcija.oslobodiResurse(resursi);
//            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
//        }

    }

}
