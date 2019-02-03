package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.LekarXMLRepozitorijum;

@Service
public class LekarServis {

    @Autowired
    private LekarXMLRepozitorijum lekarXMLRepozitorijum;

    public String dobaviSve() { return lekarXMLRepozitorijum.dobaviSve();}

    public String pretragaPoId(String id){ return lekarXMLRepozitorijum.pretragaPoId(id);}

}
