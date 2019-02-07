package zis.rs.zis.service.nonProcessService;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.IzborPromenaXMLRepozitorijum;
import zis.rs.zis.repository.xml.ZdravstveniKartonXMLRepozitorijum;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzborPromenaServis {

    @Autowired
    private IzborPromenaXMLRepozitorijum izborPromenaXMLRepozitorijum;

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum kartonXMLRepozitorijum;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Maper maper;

    public String dobaviSve() {
        return izborPromenaXMLRepozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return izborPromenaXMLRepozitorijum.pretragaPoId(id);
    }

    public String sacuvaj(Akcija akcija) {
        String rezultat = izborPromenaXMLRepozitorijum.sacuvaj(akcija);
        String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                        + maper.dobaviPrefiks("xmlSema"));
        rdfRepozitorijum.sacuvaj(noviRezultat, maper.dobaviGraf("izbori"), false);
        this.izmeniLekaraUKartonu(akcija);
        return "Uspesno promenjen lekar.";
    }

    public String obrisi(Akcija akcija) {
        return izborPromenaXMLRepozitorijum.obrisi(akcija);
    }

    public String izmeni(Akcija akcija) {
        return izborPromenaXMLRepozitorijum.izmeni(akcija);
    }

    private void izmeniLekaraUKartonu(Akcija akcija) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        String lekarId = "";
        String kartonId = "";
        NodeList elementi = dok.getFirstChild().getChildNodes();
        Element element;
        for (int i = 0; i < elementi.getLength(); i++) {
            try {
                element = (Element) elementi.item(i);
                switch (element.getTagName().split(":")[1]) {
                    case "lekar":
                        lekarId = element.getAttributes().item(0).getNodeValue();
                        break;
                    case "osigurano_lice":
                        kartonId = element.getAttributes().item(0).getNodeValue();
                        break;
                }
            } catch (Exception ignored) {

            }

        }
        if (lekarId.equals("") || kartonId.equals("")) {
            throw new ValidacioniIzuzetak("Nevalidni prosledjeni podaci!");
        }
        kartonXMLRepozitorijum.izmeniOdabranogLekara(kartonId, lekarId);
        rdfRepozitorijum.izmeniPoljeUKartonu(kartonId, "voc:odabraniLekar", "<" + lekarId + ">");

    }
}
