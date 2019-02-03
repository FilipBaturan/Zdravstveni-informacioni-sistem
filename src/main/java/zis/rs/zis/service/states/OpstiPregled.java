package zis.rs.zis.service.states;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class OpstiPregled extends Stanje {

    @Autowired
    private OpstiPregled opstiPregled;

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    @Override
    public String obradiZahtev(Akcija akcija) {
        return opstiPregled.kreiranjeDokumentacije(akcija);
    }

    /**
     * Kreiranje lekarskog izvestaja i opciono
     * lekarskog recepta i izvestaja
     *
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String kreiranjeDokumentacije(Akcija akcija) {
        Node izvestaj = dobaviDokument(akcija, "izvestaj");
        if (izvestaj == null) {
            throw new ValidacioniIzuzetak("Izvestaj nije prosledjen!");
        }
        String rezultatIzvestaja = izvestajXMLRepozitorijum.sacuvaj(akcija);
        return null;
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
