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
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
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

    @Autowired
    private GeneratorMetaPodataka generatorMetaPodataka;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

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
            String rez = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (rez.isEmpty()) {
                String ime = dokument.substring(0, dokument.length() - 1);
                throw new ValidacioniIzuzetak("Ne postoji ni jedan " + ime + " u bazi!");
            } else {
                return rez;
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
            String rezultat = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (rezultat.isEmpty()) {
                String ime = dokument.substring(0, dokument.length() - 1);
                throw new ValidacioniIzuzetak("Trazeni " + ime + " ne postoji u bazi!");
            } else {
                return rezultat;
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

            rdfRepozitorijum.obrisi(maper.dobaviGraf(dokument), id);

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

    public String sacuvaj(Node cvor, String dokument, String prefiksDokumenta) {
        String sadrzajEntiteta = validator.procesirajAkciju(cvor, maper.dobaviSemu(prefiksDokumenta));

        String prefiks = maper.konvertujUDokument(sadrzajEntiteta).getFirstChild().getNodeName().split(":")[0];
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            Long id = sekvencer.dobaviId();

            String noviId = maper.dobaviURI(prefiksDokumenta) + id.toString();
            Document dodatId = this.umetniId(maper.konvertujUDokument(sadrzajEntiteta).getFirstChild(), id, prefiksDokumenta);
            Document cvoroviSaMeta = dodajMetaPodatke(prefiks, dodatId, noviId);

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), maper.dobaviPutanju(dokument), maper.konvertujUString(cvoroviSaMeta),
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom kreiranja " + prefiksDokumenta + "a!");
            }

            konekcija.oslobodiResurse(resursi);
            return maper.konvertujUString(cvoroviSaMeta);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String izmeni(Akcija akcija, String dokument, String prefiksDokumenta) {

        String entitet = validator.procesirajAkciju(akcija, maper.dobaviSemu(prefiksDokumenta));
        Node cvor = maper.konvertujUDokument(entitet).getFirstChild();
        Element el = (Element) cvor;
        String id = el.getAttribute("id");


        String prefiks = maper.konvertujUDokument(entitet).getFirstChild().getNodeName().split(":")[0];

        NodeList cvoroviEntiteta = maper.konvertujUDokument(entitet).getFirstChild().getChildNodes();
        NodeList cvoroviSaMeta = dodajMetaPodatkeIzmena(prefiks, cvoroviEntiteta, id);
        String sadrzajEnteita;
        ResursiBaze resursi = null;
        try {
            sadrzajEnteita = maper.kreirajXmlOdCvorova(cvoroviSaMeta);
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument(dokument));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("izmena")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks(prefiksDokumenta), pronadjiRecept(id, dokument), sadrzajEnteita,
                    maper.dobaviPrefiks(dokument));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument(dokument), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            String ime = dokument.substring(0, dokument.length() - 1);

            String rezultat = "<" + el.getTagName() + " id=\"" + id + "\" about=\"" + id + "\" "
                    + "xmlns:" + prefiks + "=\"" + maper.dobaviPrefiks(prefiksDokumenta) + "\">" +
                    sadrzajEnteita
                    + "</" + el.getNodeName() + ">";

            String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                            + maper.dobaviPrefiks("xmlSema"));
            rdfRepozitorijum.izmeni(noviRezultat, maper.dobaviGraf(dokument), false);

            return "Uspesno izmenjen " + ime + "!";
        } catch (Exception e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    /**
     * @param cvor kojeg treba izmeniti, id koji treba ubaciti i prefiks namespace
     * @return izmenjena reprezentacija recepta
     */
    private Document umetniId(Node cvor, Long id, String dokument) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(cvor, true);
            dok.appendChild(importovan);

            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI(dokument) + id);

            return dok;
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (ValidacioniIzuzetak e) {
            throw new ValidacioniIzuzetak(e.getMessage());
        }
    }

    /**
     * @param id trazenog entiteta
     * @return xpath putanju do pronadjenog entiteta
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


    private NodeList dodajMetaPodatkeIzmena(String entitet, NodeList doktument, String id) {
        switch (entitet) {
            case "izvestaj":
                generatorMetaPodataka.dodajMetaPodatkeIzvestaju(doktument, id);
                break;
            case "uput":
                generatorMetaPodataka.dodajMetaPodatkeUputu(doktument, id);
                break;
            case "recept":
                generatorMetaPodataka.dodajMetaPodatkeReceptu(doktument, id);
                break;
            case "lek":
                generatorMetaPodataka.dodajMetaPodatkeLeku(doktument, id);
        }
        return doktument;
    }

    private Document dodajMetaPodatke(String entitet, Document doktument, String id) {
        switch (entitet) {
            case "izvestaj":
                generatorMetaPodataka.dodajMetaPodatkeIzvestaju(doktument.getFirstChild().getChildNodes(), id);
                break;
            case "uput":
                generatorMetaPodataka.dodajMetaPodatkeUputu(doktument.getFirstChild().getChildNodes(), id);
                break;
            case "recept":
                generatorMetaPodataka.dodajMetaPodatkeReceptu(doktument.getFirstChild().getChildNodes(), id);
                break;
            case "lek":
                generatorMetaPodataka.dodajMetaPodatkeLeku(doktument.getFirstChild().getChildNodes(), id);
            case "izbor":
                generatorMetaPodataka.dodajMetaPodatkeIzboru(doktument.getFirstChild().getChildNodes(), id);
        }
        return doktument;
    }

    public String dobaviLekZaDijagnozu(String dijagnoza, String pacijentId) {

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekari"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjeLekovaZaDijagnozu")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), dijagnoza, pacijentId);
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
                throw new ValidacioniIzuzetak("Ne postoji lek sa dijagnozom: " + dijagnoza
                        + "na koga pacijent sa id: " + pacijentId + " nije alergican");
            } else {
                return recepti;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

}
