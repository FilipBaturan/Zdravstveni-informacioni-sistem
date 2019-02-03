package zis.rs.zis.repository.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Repository
public class UputXMLRepozitorijum extends IOStrimer{

        @Autowired
        private LekarXMLRepozitorijum lekarXMLRepozitorijum;

        @Autowired
        private KorisnikXMLRepozitorijum korisnikXMLRepozitorijum;

        @Autowired
        private Maper maper;

        @Autowired
        Operacije operacije;

        private String dokument = "uputi";
        private String prefiksDokumenta = "uput";

        public String dobaviSve() {return operacije.dobaviSve(dokument, "dobaviSveUpute");}

        public String pretragaPoId(String id) {
            return operacije.pretragaPoId(id, dokument, "pretragaPoIdUputa");
        }

        public String sacuvaj(Akcija akcija) {
            proveriUput(maper.dobaviDokument(akcija, prefiksDokumenta));
            return operacije.sacuvaj(akcija, dokument, prefiksDokumenta);
        }

        public String obrisi(Akcija akcija) {
            return operacije.obrisi(akcija, dokument, prefiksDokumenta, "pretragaPoIdUputa");
        }

        public String izmeni(Akcija akcija) {
            proveriUput(maper.dobaviDokument(akcija, prefiksDokumenta) );
            return operacije.izmeni(akcija, dokument, prefiksDokumenta);
        }

    private void proveriUput(Node sadrzaj) {
        String lekarId = "";
        String korisnikId = "";
        String specijalistaId = "";
        NodeList lista = sadrzaj.getChildNodes();
        Node element;
        for (int i = 0; i < lista.getLength(); i++) {
            element = lista.item(i);
            if (element.getLocalName().equals("osigurano_lice")) {
                korisnikId = element.getAttributes().item(0).getNodeValue();
            } else if (element.getLocalName().equals("lekar")) {
                lekarId = element.getAttributes().item(0).getNodeValue();
            }
            else if (element.getLocalName().equals("specialista")) {
                specijalistaId = element.getAttributes().item(0).getNodeValue();
                break;
            }
        }
        try {
            lekarXMLRepozitorijum.pretragaPoId(lekarId);
            lekarXMLRepozitorijum.pretragaPoId(specijalistaId);
            //korisnikXMLRepozitorijum.pretragaPoId(korisnikId);
        } catch (ValidacioniIzuzetak izuzetak) {
            throw izuzetak;
        }
    }

}
