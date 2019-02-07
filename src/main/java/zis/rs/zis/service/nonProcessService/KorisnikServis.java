package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zis.rs.zis.domain.DTO.Prijava;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class KorisnikServis {

    @Autowired
    private KorisnikXMLRepozitorijum korisnikXMLRepozitorijum;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Maper maper;

    public Prijava prijava(Akcija akcija) {
        Document dok = maper.konvertujUDokument(akcija);
        NodeList elementi = dok.getFirstChild().getLastChild().getFirstChild().getChildNodes();
        Element element;
        String korisnickoIme = null;
        String lozinka = null;
        for (int i = 0; i < elementi.getLength(); i++) {
            try {
                element = (Element) elementi.item(i);
                switch (element.getTagName()) {
                    case "korisnicko_ime":
                        korisnickoIme = element.getTextContent();
                        break;
                    case "lozinka":
                        lozinka = element.getTextContent();
                        break;
                }
            } catch (Exception ignored) {}
        }
        if (korisnickoIme == null || lozinka == null) {
            throw new ValidacioniIzuzetak("Pogresno prosledjena akcija!");
        }
        return korisnikXMLRepozitorijum.prijava(korisnickoIme, lozinka);
    }

    public String registruj(Akcija akcija) {
        String[] rezultati = korisnikXMLRepozitorijum.registruj(akcija);
        rezultati = this.preProcesiranje(rezultati[1], rezultati[0]);
        if (rezultati[1].equals(maper.dobaviGraf("zdravstveni_kartoni"))) {
            rdfRepozitorijum.sacuvaj(rezultati[0], rezultati[1], true);
        } else {
            rdfRepozitorijum.sacuvaj(rezultati[0], rezultati[1], false);
        }
        return "Uspesna registracija!";
    }

    public String obrisi(String id) {
        String kartonId = korisnikXMLRepozitorijum.obrisi(id);
        rdfRepozitorijum.obrisi("zdravstveni_kartoni", kartonId);
        return "Korisnik uspesno obrisan!";
    }

    public String dobavljanjeObavestenja(String id) {
        return korisnikXMLRepozitorijum.dobavljanjeObavestenja(id);
    }

    private String[] preProcesiranje(String glavni, String pomocni) {
        String graf = maper.dobaviGraf("zdravstveni_kartoni");
        glavni = glavni.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                + maper.dobaviPrefiks("xmlSema"));
        if (!glavni.contains("zdravstveni_karton")) {
            glavni = glavni.replaceFirst("<.*korisnik.*/>", pomocni);
            graf = maper.dobaviGraf("lekari");
        }
        return new String[]{"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + glavni, graf};

    }
}
