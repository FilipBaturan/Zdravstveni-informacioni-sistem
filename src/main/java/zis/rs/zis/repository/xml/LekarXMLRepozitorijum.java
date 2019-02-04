package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.util.*;
import zis.rs.zis.util.CRUD.Operacije;

import java.io.IOException;

@Repository
public class LekarXMLRepozitorijum extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Operacije operacije;

    @Autowired
    private Maper maper;

    private String dokument = "lekari";
    private String prefiksDokumenta = "lekar";

    public String dobaviSve() {
        return operacije.dobaviSve(dokument, "dobaviSveLekare");
    }

    public String pretragaPoId(String id) {
        return operacije.pretragaPoId(id, dokument, "pretragaPoIdLekara");
    }
}