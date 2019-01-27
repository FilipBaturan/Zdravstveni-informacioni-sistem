package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.util.*;

import java.io.IOException;

@Service
public class LekXMLRepozertorijum extends IOStrimer {

    private static final Logger logger = LoggerFactory.getLogger(LekXMLRepozertorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    public String dobaviSve() {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekovi"));
            String putanjaDoUpita = ResourceUtils
                    .getFile("classpath:templates/xquery/lekovi/dobavljanjeSvihLekova.xqy")
                    .getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = this.ucitajSadrzajFajla(putanjaDoUpita);
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
            String lekovi = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (lekovi.isEmpty()) {
                throw new ValidacioniIzuzetak("Ne postoji ni jedan lek u bazi!");
            } else {
                return lekovi;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException | NullPointerException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String pretragaPoId(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekovi"));
            String putanjaDoUpita = ResourceUtils
                    .getFile("classpath:templates/xquery/lekovi/pretragaPoIdLeka.xqy")
                    .getPath();
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
            String lekovi = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (lekovi.isEmpty()) {
                throw new ValidacioniIzuzetak("Trazeni lek ne postoji u bazi!");
            } else {
                return lekovi;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    public String sacuvaj(String lek) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekovi",
                    "lekovi.xml");
            String putanjaDoUpita = ResourceUtils
                    .getFile("classpath:templates/xquery/azuriranje/dodavanje.xml")
                    .getPath();

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "lek", "http://zis.rs/zis/seme/lek", "/lekovi:lekovi", lek,
                    "xmlns:lekovi=\"http://zis.rs/zis/seme/lekovi\"");
            long mods = xupdateService.updateResource("lekovi.xml", sadrzajUpita);
            logger.info("[INFO] " + mods + " modifications processed.");

            konekcija.oslobodiResurse(resursi);
            return "Akcije je uspesno izvrsena";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public void obrisi(String id) {

    }
}
