package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.ZdravstveniKartonXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class ZdravstveniKartonServis {

    @Autowired
    private ZdravstveniKartonXMLRepozitorijum repozitorijum;

    public String pretragaPoId(String id) {
        return repozitorijum.pretragaPoId(id);
    }

    public String izmena(Akcija akcija) {
        return repozitorijum.izmeniKarton(akcija);
    }
}
