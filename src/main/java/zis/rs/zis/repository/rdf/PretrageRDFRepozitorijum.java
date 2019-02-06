package zis.rs.zis.repository.rdf;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.util.*;

import java.io.IOException;

@Repository
public class PretrageRDFRepozitorijum extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired Maper maper;

    @Autowired
    SPARQLMaper sparqlMaper;

    public PretrageRDFRepozitorijum() {
    }

    public String opstiUpit(UpitPretrage upitPretrage)
    {
        return "asdasd";
    }
}
