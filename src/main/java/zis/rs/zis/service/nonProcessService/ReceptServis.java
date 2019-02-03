package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.ReceptXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class ReceptServis {

    @Autowired
    ReceptXMLRepozitorijum receptXMLRepozitorijum;

    public String dobaviSve() { return receptXMLRepozitorijum.dobaviSve();}

    public String pretragaPoId(String id){ return receptXMLRepozitorijum.pretragaPoId(id);}

    public String sacuvaj(Akcija akcija) { return receptXMLRepozitorijum.sacuvaj(akcija);}

    public String obrisi(Akcija akcija) { return  receptXMLRepozitorijum.obrisi(akcija);}

    public String izmeni(Akcija akcija) { return  receptXMLRepozitorijum.izmeni(akcija);}
}
