package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;

@Repository
public class StanjaPregledaXMLRepozitorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(PregledXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    public void dodajNoviProces(Akcija akcija) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("stanja_pregleda"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "stp", maper.dobaviPrefiks("stanje_pregleda"),
                    maper.dobaviPutanju("stanja_pregleda"),
                    this.konverturjUString(akcija, "cekanje"), maper.dobaviPrefiks("stanja_pregleda"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("stanja_pregleda"), sadrzajUpita);
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

    public void izmeniProces(Akcija akcija, String stanje) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("stanja_pregleda"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("izmena")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");

            String putanja = this.pronadjiPutanjuProcesa(maper.dobaviPacijentaIzPregleda(akcija));
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "stp", maper.dobaviPrefiks("stanje_pregleda") , putanja + "/@datum",
                    LocalDateTime.now().toString(), maper.dobaviPrefiks("stanja_pregleda"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("stanja_pregleda"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }

            sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "stp", maper.dobaviPrefiks("stanje_pregleda") , putanja + "/@stanje",
                    stanje, maper.dobaviPrefiks("stanja_pregleda"));
            logger.info(sadrzajUpita);
            mods = xupdateService.updateResource(maper.dobaviDokument("stanja_pregleda"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }

            konekcija.oslobodiResurse(resursi);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param id trazenog korisnika
     * @return xpath putanju do pronadjenog korisnika
     */
    private String pronadjiPutanjuProcesa(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("stanja_pregleda"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobavljanjePutanjeStanja")).getPath();
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
            if (rez.equals("/")) {
                throw new ValidacioniIzuzetak("Trazeno stanje pregleda ne postoji!");
            }
            return rez;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return string reprezentaciju nove stavke poslovnog procesa
     */
    private String konverturjUString(Akcija akcija, String stanje) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            Document dok = dbf.newDocumentBuilder().newDocument();

            Element pregled = dok.createElementNS(maper.dobaviPrefiks("stanje_pregleda"), "stanje_pregleda");
            pregled.setPrefix("stp");
            pregled.setAttribute("pacijent", maper.dobaviPacijentaIzPregleda(akcija));
            pregled.setAttribute("stanje", stanje);
            pregled.setAttribute("datum", LocalDateTime.now().toString());
            dok.appendChild(pregled);
            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Greska pri obradi podataka!");
        }
    }
}
