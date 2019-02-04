package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.UputXMLRepozitorijum;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class UputServis {

    @Autowired
    UputXMLRepozitorijum uputXMLRepozitorijum;

    @Autowired
    RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    Maper maper;

    public String dobaviSve() {
        return uputXMLRepozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return uputXMLRepozitorijum.pretragaPoId(id);
    }

    public String sacuvaj(Akcija akcija) {
        String rezultat =  uputXMLRepozitorijum.sacuvaj(akcija);
        String noviRez = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  +
                rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                + maper.dobaviPrefiks("xmlSema"));

        rdfRepozitorijum.sacuvaj(noviRez, maper.dobaviGraf("uputi"), false);
        if (!rezultat.equals(""))
            return "Uspesno sacuvan uput";
        else
            return "Greska prilikom sacuvavanja uputa";
    }

    public String obrisi(Akcija akcija) {
        return uputXMLRepozitorijum.obrisi(akcija);
    }

    public String izmeni(Akcija akcija) {
        return uputXMLRepozitorijum.izmeni(akcija);
    }
}
