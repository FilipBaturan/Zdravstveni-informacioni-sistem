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
public class IzborPromenaXMLRepozitorijum extends IOStrimer {

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private Maper maper;

    @Autowired
    private Operacije operacije;

    private String dokument = "izbori";
    private String prefiksDokumenta = "izbor";

    public String dobaviSve() {
        return operacije.dobaviSve(dokument, "dobaviSveIzbore");
    }

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, dokument, "pretragaPoIdIzbora");
    }

    public String sacuvaj(Akcija akcija) {
        proveriIzbor(maper.dobaviDokument(akcija, prefiksDokumenta));
        return operacije.sacuvaj(dobaviDokument(akcija, "izbor"), dokument, prefiksDokumenta);
    }

    public String obrisi(Akcija akcija) {
        return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdIzbora");
    }

    public String izmeni(Akcija akcija) {
        proveriIzbor(maper.dobaviDokument(akcija, prefiksDokumenta));
        return operacije.izmeni(akcija, dokument, prefiksDokumenta);
    }

    private void proveriIzbor(Node sadrzaj) {
//        String lekarId = "";
//        String korisnikId = "";
//        String specijalistaId = "";
//        NodeList lista = sadrzaj.getChildNodes();
//        Node element;
//        for (int i = 0; i < lista.getLength(); i++) {
//            element = lista.item(i);
//            if (element.getLocalName().equals("osigurano_lice")) {
//                korisnikId = element.getAttributes().item(0).getNodeValue();
//            } else if (element.getLocalName().equals("lekar")) {
//                lekarId = element.getAttributes().item(0).getNodeValue();
//            }
//            else if (element.getLocalName().equals("specialista")) {
//                specijalistaId = element.getAttributes().item(0).getNodeValue();
//                break;
//            }
//        }
//
//        lekarXMLRepozitorijum.pretragaPoId(lekarId);
//        lekarXMLRepozitorijum.pretragaPoId(specijalistaId);
        //korisnikXMLRepozitorijum.pretragaPoId(korisnikId);

    }

    private Node dobaviDokument(Akcija akcija, String nazivDokumenta) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        NodeList lista = dok.getFirstChild().getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals(nazivDokumenta)) {
                return element;
            }
        }
        return null;
    }


}
