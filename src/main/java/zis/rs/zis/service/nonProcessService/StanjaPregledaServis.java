package zis.rs.zis.service.nonProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.StanjaPregledaXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

import java.util.List;

@Service
public class StanjaPregledaServis {

    @Autowired
    private StanjaPregledaXMLRepozitorijum stanjaRepozitorijum;

    public List<String> dobaviProcese() {
        return stanjaRepozitorijum.dobaviProcese();
    }

    public void dodajNoviProces(Akcija akcija) {
        stanjaRepozitorijum.dodajNoviProces(akcija);
    }

    public void izmeniProces(String stanje, String pacijent) {
        stanjaRepozitorijum.izmeniProces(stanje, pacijent);
    }
}
