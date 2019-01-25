package zis.rs.zis.service.implementation;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.domain.entities.Lekar;
import zis.rs.zis.service.definition.KorisnikServis;
import zis.rs.zis.service.definition.LekarServis;
import zis.rs.zis.util.*;

import java.io.IOException;

@Service
public class LekarServisImpl extends IOStrimer implements LekarServis {

    private static final Logger logger = LoggerFactory.getLogger(LekarServisImpl.class);

    @Autowired
    private KorisnikServis korisnikServis;

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Override
    public String dobaviSve() {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekari"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dobaviSveLekare")).getPath();
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
            String lekari = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (lekari.isEmpty()) {
                throw new ValidacioniIzuzetak("Ne postoji ni jedan lekar u bazi!");
            } else {
                return lekari;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    @Override
    public String pretragaPoId(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekari"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("pretragaPoId")).getPath();
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
            String lekari = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (lekari.isEmpty()) {
                throw new ValidacioniIzuzetak("Ne postoji ni jedan lekar u bazi!");
            } else {
                return lekari;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }


    @Override
    public String sacuvaj(Lekar lekar) {
        return null;
    }

    @Override
    public void obrisi(String id) {

    }
}
