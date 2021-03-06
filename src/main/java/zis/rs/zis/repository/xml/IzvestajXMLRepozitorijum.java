package zis.rs.zis.repository.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Repository
public class IzvestajXMLRepozitorijum extends IOStrimer {

    @Autowired
    private Maper maper;

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum kartonXMLRepozitorijum;

    @Autowired
    private Operacije operacije;

    private String dokument = "izvestaji";
    private String prefiksDokumenta = "izvestaj";

    public String dobaviSve() {
        return operacije.dobaviSve(dokument, "dobaviSveIzvestaje");
    }

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, dokument, "pretragaPoIdIzvestaja");
    }

    public String obrisi(Akcija akcija) {
        return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdIzvestaja");
    }

    public String izmeni(Akcija akcija) {
        proveriIzvestaj(maper.dobaviDokument(akcija, "izvestaj"));
        return operacije.izmeni(akcija, dokument, prefiksDokumenta);
    }


    public void proveriIzvestaj(Node sadrzaj) {
        String lekarId = "";
        String korisnikId = "";
        NodeList lista = sadrzaj.getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            try {
                element = lista.item(i);
                if (element.getLocalName().equals("osigurano_lice")) {
                    korisnikId = element.getAttributes().item(0).getNodeValue();
                }
                if (element.getLocalName().equals("lekar")) {
                    lekarId = element.getAttributes().item(0).getNodeValue();
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        lekarXMLRepozitorijum.pretragaPoId(lekarId);
        kartonXMLRepozitorijum.pretragaPoId(korisnikId);
    }
}
