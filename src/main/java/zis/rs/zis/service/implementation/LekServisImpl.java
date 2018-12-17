package zis.rs.zis.service.implementation;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.domain.entities.Lek;
import zis.rs.zis.service.definition.LekServis;
import zis.rs.zis.util.*;

import java.io.IOException;

@Service
public class LekServisImpl extends IOStrimer implements LekServis {

    @Autowired
    private XMLDBKonekcija konekcija;

    @Override
    public String dobaviSve() {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekovi",
                    "lekovi.xml");
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
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    @Override
    public String pretragaPoId(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekovi",
                    "lekovi.xml");
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
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    @Override
    public String sacuvaj(Lek lek) {
        return null;
    }

    @Override
    public void obrisi(String id) {

    }
}
