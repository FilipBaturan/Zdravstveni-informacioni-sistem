package zis.rs.zis.service.states;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.repository.xml.ReceptXMLRepozitorijum;
import zis.rs.zis.repository.xml.UputXMLRepozitorijum;
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
    private UputXMLRepozitorijum uputXMLRepozitorijum;

    @Autowired
    private ReceptXMLRepozitorijum receptXMLRepozitorijum;

    @Autowired
    private Maper maper;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Operacije operacije;

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
        Node izvestaj = dobaviDokumentIzListe(akcija, "izvestaj");
        if (izvestaj == null) {
            throw new ValidacioniIzuzetak("Izvestaj nije prosledjen!");
        }
        Node uput = dobaviDokumentIzListe(akcija, "uput");
        Node recept = dobaviDokumentIzListe(akcija, "recept");
        String rezultat;
        String graf;
        if (uput != null && recept != null) {
            throw new ValidacioniIzuzetak("Prosledjeni i uput i izvestaj!");
        } else if (uput != null) {
            uputXMLRepozitorijum.proveriUput(uput);
            rezultat = operacije.sacuvaj(uput, "uputi", "uput");
            graf = "uputi";
            if (rezultat.equals(""))
                return "Greska prilikom snimanja uputa";
            String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                            + maper.dobaviPrefiks("xmlSema"));
            rdfRepozitorijum.sacuvaj(noviRezultat, maper.dobaviGraf(graf), false);
        } else if (recept != null) {
            receptXMLRepozitorijum.proveriRecept(recept);
            rezultat = operacije.sacuvaj(recept, "recepti", "recept");
            graf = "recepti";
            if (rezultat.equals(""))
                return "Greska prilikom snimanja recepta";
            String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                            + maper.dobaviPrefiks("xmlSema"));
            rdfRepozitorijum.sacuvaj(noviRezultat, maper.dobaviGraf(graf), false);
        }

        izvestajXMLRepozitorijum.proveriIzvestaj(izvestaj);
        rezultat = operacije.sacuvaj(izvestaj, "izvestaji", "izvestaj");
        graf = "izvestaji";
        String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                        + maper.dobaviPrefiks("xmlSema"));
        rdfRepozitorijum.sacuvaj(noviRezultat, maper.dobaviGraf(graf), false);

        if (!rezultat.equals(""))
            return "Izvestaj uspesno sacuvan";
        else
            return "Greska prilikom snimanja izvestaja";
    }

    private Node dobaviDokumentIzListe(Akcija akcija, String nazivDokumenta) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        NodeList lista = dok.getFirstChild().getChildNodes();
        Node element;
        try {
            for (int i = 0; i < lista.getLength(); i++) {
                element = lista.item(i);
                if (element.getLocalName().equals(nazivDokumenta)) {
                    return element;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
