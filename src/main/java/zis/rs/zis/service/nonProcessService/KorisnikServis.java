package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class KorisnikServis {

    @Autowired
    private KorisnikXMLRepozitorijum korisnikXMLRepozitorijum;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Maper maper;

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
