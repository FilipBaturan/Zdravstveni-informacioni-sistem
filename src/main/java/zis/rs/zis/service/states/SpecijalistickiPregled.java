package zis.rs.zis.service.states;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class SpecijalistickiPregled extends Stanje {

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    @Autowired
    private SpecijalistickiPregled specijalistickiPregled;

    @Autowired
    private Maper maper;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Operacije operacije;

    @Override
    public String obradiZahtev(Akcija akcija) {
        return specijalistickiPregled.kreiranjeIzvestaja(akcija);
    }

    /**
     * Kreira izvestaj
     *
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String kreiranjeIzvestaja(Akcija akcija) {
        Node izvestaj = dobaviDokumentIzListe(akcija);
        if (izvestaj == null) {
            throw new ValidacioniIzuzetak("Izvestaj nije prosledjen!");
        }
        izvestajXMLRepozitorijum.proveriIzvestaj(izvestaj);
        String rezultat = operacije.sacuvaj(izvestaj, "izvestaji", "izvestaj");
        String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                        + maper.dobaviPrefiks("xmlSema"));
        rdfRepozitorijum.sacuvaj(noviRezultat, maper.dobaviGraf("izvestaji"), false);

        if (!rezultat.equals(""))
            return "Izvestaj uspesno sacuvan";
        else
            return "Greska prilikom snimanja izvestaja";
    }

    private Node dobaviDokumentIzListe(Akcija akcija) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        NodeList lista = dok.getChildNodes();
        Node element;
        try {
            for (int i = 0; i < lista.getLength(); i++) {
                element = lista.item(i);
                if (element.getLocalName().equals("izvestaj")) {
                    return element;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

}
