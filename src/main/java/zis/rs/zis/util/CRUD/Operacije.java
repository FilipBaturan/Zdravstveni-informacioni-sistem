package zis.rs.zis.util.CRUD;

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
import zis.rs.zis.repository.xml.ReceptXMLRepozitorijum;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Repository
public class Operacije extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private Validator validator;

    @Autowired
    private Sekvencer sekvencer;

    private static final Logger logger = LoggerFactory.getLogger(Operacije.class);

    public String dobaviSve(String dokument, String putanjaUpita) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit(putanjaUpita)).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = this.ucitajSadrzajFajla(putanjaDoUpita);
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
            String recepti = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (recepti.isEmpty()) {
                String ime = dokument.substring(0, dokument.length() - 1);
                throw new ValidacioniIzuzetak("Ne postoji ni jedan " + ime + " u bazi!");
            } else {
                return recepti;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException | NullPointerException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    public String pretragaPoId(String id, String dokument, String putanjaUpita) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit(putanjaUpita)).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), id);
            CompiledExpression compiledXquery = upitServis.compile(sadrzajUpita);
            ResourceSet result = upitServis.execute(compiledXquery);
            ResourceIterator i = result.getIterator();
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
            String recepti = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (recepti.isEmpty()) {
                String ime = dokument.substring(0, dokument.length() - 1);
                throw new ValidacioniIzuzetak("Trazeni " + ime + " ne postoji u bazi!");
            } else {
                return recepti;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String obrisi(Akcija akcija, String dokument, String prefiksDokumenta, String putanjaUpita) {
        String id = akcija.getKontekst();
        String recept = pretragaPoId(id, dokument, putanjaUpita);
        String prefiks = maper.konvertujUDokument(recept).getFirstChild().getNodeName().split(":")[0];

        String putanjaDoLeka = pronadjiRecept(id, dokument);
        ResursiBaze resursi = null;

        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("brisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), putanjaDoLeka,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
            return prefiksDokumenta + " uspesno obrisan!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public void obrisi(String dokument, String prefiks, String prefiksDokumenta, String putanja) {
        ResursiBaze resursi = null;

        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("brisanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), putanja,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
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

    public String sacuvaj(Akcija akcija,String dokument, String prefiksDokumenta) {
        String lek = validator.procesirajAkciju(akcija, maper.dobaviSemu(prefiksDokumenta));

        String prefiks = maper.konvertujUDokument(lek).getFirstChild().getNodeName().split(":")[0];
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            Long id = sekvencer.dobaviId();

            String dodatId = this.umetniId(maper.konvertujUDokument(lek).getFirstChild(), id, prefiksDokumenta);

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), maper.dobaviPutanju(dokument), dodatId,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom kreiranja " + prefiksDokumenta + "a!");
            }

            konekcija.oslobodiResurse(resursi);
            return "Uspesno sacuvan " + prefiksDokumenta + "!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String izmeni(Akcija akcija, String dokument, String prefiksDokumenta) {

        String lek = validator.procesirajAkciju(akcija, maper.dobaviSemu(prefiksDokumenta));
        Node cvor = maper.konvertujUDokument(lek).getFirstChild();
        Element el = (Element) cvor;
        String id = el.getAttribute("id");


        String prefiks = maper.konvertujUDokument(lek).getFirstChild().getNodeName().split(":")[0];
        NodeList cvoroviLeka = maper.konvertujUDokument(lek).getFirstChild().getChildNodes();

        String sadrzajLeka = null;
        try {
            sadrzajLeka = maper.kreirajXmlOdCvorova(cvoroviLeka);
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
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), pronadjiRecept(id, dokument), sadrzajLeka,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            String ime = dokument.substring(0, dokument.length() - 1);
            return "Uspesno izmenjen " + ime + "!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }





    /**
     * @param recept kojeg treba izmeniti, id koji treba ubaciti i prefiks namespace
     * @return izmenjena reprezentacija recepta
     */
    private String umetniId(Node recept, Long id, String dokument) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(recept, true);
            dok.appendChild(importovan);

            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI(dokument) + id);

            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (ValidacioniIzuzetak e) {
            throw new ValidacioniIzuzetak(e.getMessage());
        }
    }

    /**
     * @param id trazenog recepta
     * @return xpath putanju do pronadjenog recepta
     */
    private String pronadjiRecept(String id, String dokument) {
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
                throw new ValidacioniIzuzetak("Trazeni " + dokument.substring(0, dokument.length() - 1)
                        + " ne postoji!");
            }
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }
}