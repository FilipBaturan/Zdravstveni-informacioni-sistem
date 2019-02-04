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
        String rezultat = this.preProcesiranje(rezultati[1], rezultati[0]);
        rdfRepozitorijum.sacuvaj(rezultat);
        return "Uspesna registracija!";
    }

    private String preProcesiranje(String glavni, String pomocni) {
        glavni = glavni.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                + maper.dobaviPrefiks("xmlSema")).replaceFirst("<.*korisnik.*/>", pomocni);
        final String PROTOKOL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        return PROTOKOL + glavni;

    }
}
