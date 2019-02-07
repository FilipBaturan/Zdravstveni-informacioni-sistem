package zis.rs.zis.repository.xml;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Repository
public class LekXMLRepozitorijum extends IOStrimer {

    @Autowired
    private Operacije operacije;

    @Autowired
    private Maper maper;

    private String dokument = "lekovi";
    private String prefiksDokumenta = "lek";

    public String dobaviSve() {
        return operacije.dobaviSve(dokument, "dobaviSveLekove");
    }

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, dokument, "pretragaPoIdLeka");
    }

    public String sacuvaj(Akcija akcija) {
        return operacije.sacuvaj(maper.dobaviDokument(akcija, "lek"), dokument, prefiksDokumenta);
    }

    public String obrisi(Akcija akcija) {
        return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdLeka");
    }

    public String izmeni(Akcija akcija) {
        return operacije.izmeni(akcija, dokument, prefiksDokumenta);
    }

    public String dobaviLekZaDijagnozu(String dijagnoza, String pacijentId) {
        return operacije.dobaviLekZaDijagnozu(dijagnoza, pacijentId);
    }
}
