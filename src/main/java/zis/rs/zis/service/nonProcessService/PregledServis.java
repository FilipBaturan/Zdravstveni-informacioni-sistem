package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;

@Service
public class PregledServis {

    @Autowired
    private PregledXMLRepozitorijum pregledXMLRepozitorijum;

    public String pretragaPoId(String id) {
        return pregledXMLRepozitorijum.pretragaPoId(id);
    }
}
