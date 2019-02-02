package zis.rs.zis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sekvencer extends IOStrimer {

    private long brojac;

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    public Sekvencer() {
        this.brojac = 10L;
        this.inicijalizacija();
    }

    private void inicijalizacija() {
//        ResursiBaze resursi = null;
//        try {
//            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("korisnici"));
//            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("prebrojavanje")).getPath();
//            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
//            upitServis.setProperty("indent", "yes");
//            String sadrzajUpita = this.ucitajSadrzajFajla(putanjaDoUpita);
//            CompiledExpression compiledXquery = upitServis.compile(sadrzajUpita);
//            ResourceSet result = upitServis.execute(compiledXquery);
//            ResourceIterator i = result.getIterator();
//            Resource res = null;
//
//            StringBuilder sb = new StringBuilder();
//
//            while (i.hasMoreResources()) {
//
//                try {
//                    res = i.nextResource();
//                    sb.append(res.getContent().toString());
//                } finally {
//                    if (res != null)
//                        ((EXistResource) res).freeResources();
//
//                }
//            }
//            String lekovi = sb.toString();
//            konekcija.oslobodiResurse(resursi);
////            if (lekovi.isEmpty()) {
////                throw new ValidacioniIzuzetak("Trazeni lek ne postoji u bazi!");
////            } else {
////                //return lekovi;
////            }
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
//                XMLDBException | IOException e) {
//            konekcija.oslobodiResurse(resursi);
//            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
//        }
    }

    public long dobaviId() {
        return ++brojac;
    }
}
