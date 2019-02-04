package zis.rs.zis.service.states;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.repository.xml.LekarXMLRepozitorijum;
import zis.rs.zis.repository.xml.ZdravstveniKartonXMLRepozitorijum;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class OpstiPregled extends Stanje {

    @Autowired
    private OpstiPregled opstiPregled;

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    @Autowired
    private Maper maper;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum korisnikXMLRepozitorijum;

    @Autowired
    Operacije operacije;

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

        proveriUput(maper.dobaviDokument(akcija, "uput"));
        String rezultat = operacije.sacuvaj(dobaviDokument(akcija, "uput"), "uputi", "uput");

        String noviRez = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  +
                rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                        + maper.dobaviPrefiks("xmlSema"));

        rdfRepozitorijum.sacuvaj(noviRez, maper.dobaviGraf("izvestaji"), false);
        if (!rezultat.equals(""))
            return "Uspesno sacuvan uput";
        else
            return "Greska prilikom sacuvavanja uputa";
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
            } else if (element.getLocalName().equals("specialista")) {
                specijalistaId = element.getAttributes().item(0).getNodeValue();
                break;
            }
        }

        lekarXMLRepozitorijum.pretragaPoId(lekarId);
        lekarXMLRepozitorijum.pretragaPoId(specijalistaId);
        korisnikXMLRepozitorijum.pretragaPoId(korisnikId);

    }
}
