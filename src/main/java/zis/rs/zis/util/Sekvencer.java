package zis.rs.zis.util;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class Sekvencer extends IOStrimer {

    private long brojac;

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    public Sekvencer() {
        this.brojac = 10L;
    }

    @PostConstruct
    public void init(){
        this.inicijalizacija();
    }

    private void inicijalizacija() {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekari"));
            String putanjaDoUpita = ResourceUtils.getFile("classpath:templates/xquery/sekvencer/dobavljaneBrojaSvihEntiteta.xqy").getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
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
            String broj = sb.toString();
            konekcija.oslobodiResurse(resursi);
            this.brojac = Long.parseLong(broj);
        } catch (XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public long dobaviId() {
        if(brojac == -1)
        {
            this.inicijalizacija();
        }
        return ++brojac;
    }
}
