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
public class IzborPromenaXMLRepozitorijum extends IOStrimer {

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum kartonXMLRepozitorijum;

    @Autowired
    private OgranicenjaRepozitorijum ogranicenjaRepozitorijum;

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
        return operacije.sacuvaj(maper.dobaviDokument(akcija, "izbor"), dokument, prefiksDokumenta);
    }

    public String obrisi(Akcija akcija) {
        return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdIzbora");
    }

    public String izmeni(Akcija akcija) {
        proveriIzbor(maper.dobaviDokument(akcija, prefiksDokumenta));
        return operacije.izmeni(akcija, dokument, prefiksDokumenta);
    }

    private void proveriIzbor(Node sadrzaj) {
        String lekarId = "";
        String kartonId = "";
        String prosliLekar = "";
        NodeList lista = sadrzaj.getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            try {
                element = lista.item(i);
                switch (element.getLocalName()) {
                    case "osigurano_lice":
                        kartonId = element.getAttributes().item(0).getNodeValue();
                        break;
                    case "lekar":
                        lekarId = element.getAttributes().item(0).getNodeValue();
                        break;
                    case "prosli_lekar":
                        prosliLekar = element.getAttributes().item(0).getNodeValue();
                        break;
                }
            } catch (Exception e) {
            }
        }
        lekarXMLRepozitorijum.pretragaPoId(lekarId);
        lekarXMLRepozitorijum.pretragaPoId(prosliLekar);
        kartonXMLRepozitorijum.pretragaPoId(kartonId);
        ogranicenjaRepozitorijum.proveriOgranicenjaIzboraLekara(kartonId, prosliLekar, lekarId);

    }

}
