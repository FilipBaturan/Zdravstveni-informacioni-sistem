package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzvestajServis {

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    public String dobaviSve() {
        return izvestajXMLRepozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return izvestajXMLRepozitorijum.pretragaPoId(id);
    }

    public String obrisi(Akcija akcija) {
        return izvestajXMLRepozitorijum.obrisi(akcija);
    }

    public String izmeni(Akcija akcija) {
        return izvestajXMLRepozitorijum.izmeni(akcija);
    }
}
