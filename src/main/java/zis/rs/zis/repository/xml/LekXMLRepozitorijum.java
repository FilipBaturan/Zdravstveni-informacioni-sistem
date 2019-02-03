package zis.rs.zis.repository.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.akcije.Akcija;

@Repository
public class LekXMLRepozitorijum extends IOStrimer {

    @Autowired
    Operacije operacije;

    private String dokument = "lekovi";
    private String prefiksDokumenta = "lek";

    public String dobaviSve() {return operacije.dobaviSve(dokument, "dobaviSveLekove");}

    public String pretragaPoId(String id) {return operacije.pretragaPoId(id, dokument, "pretragaPoIdLeka");}

    public String sacuvaj(Akcija akcija) {
        return operacije.sacuvaj(akcija, dokument, prefiksDokumenta);
    }

    public String obrisi(Akcija akcija) { return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdLeka");}

    public String izmeni(Akcija akcija) {
        return operacije.izmeni(akcija, dokument, prefiksDokumenta);
    }

}
