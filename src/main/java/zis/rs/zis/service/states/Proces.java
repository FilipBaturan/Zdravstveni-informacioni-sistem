package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Map;

@Service
public class Proces {

    @Autowired
    private ZakazivanjePregleda zakazivanjePregleda;

    @Autowired
    private PrihvatanjeTermina prihvatanjeTermina;

    @Autowired
    private IzmenjenTermin izmenjenTermin;

    @Autowired
    private OpstiPregled opstiPregled;

    @Autowired
    private SpecijalistickiPregled specijalistickiPregled;

    private Map<String, Stanje> procesi;

    public Proces() {

    }

    public String obradiZahtev(Akcija akcija) {
        return null;
    }
}
