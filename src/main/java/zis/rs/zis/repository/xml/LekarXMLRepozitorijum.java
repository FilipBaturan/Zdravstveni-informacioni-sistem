package zis.rs.zis.repository.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.KonfiguracijaKonekcija;
import zis.rs.zis.util.Maper;

@Repository
public class LekarXMLRepozitorijum extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Operacije operacije;

    @Autowired
    private Maper maper;

    private String dokument = "lekari";
    private String prefiksDokumenta = "lekar";

    public String dobaviSve() {
        return operacije.dobaviSve(dokument, "dobaviSveLekare");
    }

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, dokument, "pretragaPoIdLekara");
    }
}

