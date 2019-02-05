package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.IzborPromenaXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzborPromenaServis {

    @Autowired
    private IzborPromenaXMLRepozitorijum izborPromenaXMLRepozitorijum;

    public String dobaviSve() {
        return izborPromenaXMLRepozitorijum.dobaviSve();
    }

    public String pretragaPoId(String id) {
        return izborPromenaXMLRepozitorijum.pretragaPoId(id);
    }

    public String sacuvaj(Akcija akcija) {
        return izborPromenaXMLRepozitorijum.sacuvaj(akcija);
    }

    public String obrisi(Akcija akcija) {
        return izborPromenaXMLRepozitorijum.obrisi(akcija);
    }

    public String izmeni(Akcija akcija) {
        return izborPromenaXMLRepozitorijum.izmeni(akcija);
    }
}
