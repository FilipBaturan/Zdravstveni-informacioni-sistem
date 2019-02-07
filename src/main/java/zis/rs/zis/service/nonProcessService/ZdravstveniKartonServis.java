package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.rdf.RDFRepozitorijum;
import zis.rs.zis.repository.xml.ZdravstveniKartonXMLRepozitorijum;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.TransformacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class ZdravstveniKartonServis {

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum repozitorijum;

    @Autowired
    private RDFRepozitorijum rdfRepozitorijum;

    @Autowired
    private Maper maper;

    public String dobaviSve() {
        return repozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return repozitorijum.pretragaPoId(id);
    }

    public String dobaviDokumente(String id) { return repozitorijum.dobaviDokumente(id); }

    public String izmena(Akcija akcija) {
        String rezultat = repozitorijum.izmeniKarton(akcija);
        if (rezultat != null) {
            String noviRezultat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    rezultat.trim().replaceFirst(" ", "  " + maper.dobaviPrefiks("vokabular")
                            + maper.dobaviPrefiks("xmlSema"));
            rdfRepozitorijum.izmeni(noviRezultat, maper.dobaviGraf("zdravstveni_kartoni"), true);
            return "Uspesno izmenjen zdravstveni karton!";
        }
        throw new TransformacioniIzuzetak("Greska prilikom obrade podataka.");

    }

    public String opstaPretragaLekara(String tekst) {
        return repozitorijum.opstaPretraga(tekst, "opstaPretragaKartona");
    }

    public String opstaPretragaPacijenta(String tekst) {
        return repozitorijum.opstaPretraga(tekst, "opstaPregragaDokumenata");
    }
}
