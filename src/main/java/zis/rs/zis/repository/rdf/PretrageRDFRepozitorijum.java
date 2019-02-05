package zis.rs.zis.repository.rdf;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.util.*;

import java.io.IOException;

@Repository
public class PretrageRDFRepozitorijum extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired Maper maper;

    @Autowired
    SPARQLMaper sparqlMaper;

    public PretrageRDFRepozitorijum() {
    }

    public String opstiUpit(UpitPretrage upitPretrage)
    {
        sparqlMaper.selectData()



        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("lekari"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("pretragaMetaPodataka")).getPath();
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
