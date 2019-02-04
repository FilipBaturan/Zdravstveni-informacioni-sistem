package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.LekXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;


@Service
public class LekServis {

    @Autowired
    private LekXMLRepozitorijum lekXMLRepozitorijum;

    public String dobaviSve() {
        return lekXMLRepozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return lekXMLRepozitorijum.pretragaPoId(id);
    }

    public String sacuvaj(Akcija akcija) {
        return lekXMLRepozitorijum.sacuvaj(akcija);
    }

    public String obrisi(Akcija akcija) {
        return lekXMLRepozitorijum.obrisi(akcija);
    }

    public String izmeni(Akcija akcija) {
        return lekXMLRepozitorijum.izmeni(akcija);
    }
}