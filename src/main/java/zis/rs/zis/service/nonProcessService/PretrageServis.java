package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.repository.rdf.PretrageRDFRepozitorijum;

@Service
public class PretrageServis {

    @Autowired
    private PretrageRDFRepozitorijum pretrageRDFRepozitorijum;

    public PretrageServis() {
    }

    public String opstiUpit(UpitPretrage upitPretrage)
    {
        return pretrageRDFRepozitorijum.opstiUpit(upitPretrage);
    }
}
