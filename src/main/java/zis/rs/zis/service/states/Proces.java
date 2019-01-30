package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

import java.util.HashMap;
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

    @Autowired
    private Maper maper;

    private Map<String, Stanje> procesi;

    public Proces() {
        this.procesi = new HashMap<>();
    }

    public String obradiZahtev(Akcija akcija) {
        return this.procesi.getOrDefault(maper.dobaviPacijentaIzPregleda(akcija), zakazivanjePregleda)
                .obradiZahtev(akcija);
    }

    public Map<String, Stanje> getProcesi() {
        return procesi;
    }

    public void setProcesi(Map<String, Stanje> procesi) {
        this.procesi = procesi;
    }

    public ZakazivanjePregleda getZakazivanjePregleda() {
        return zakazivanjePregleda;
    }

    public PrihvatanjeTermina getPrihvatanjeTermina() {
        return prihvatanjeTermina;
    }

    public IzmenjenTermin getIzmenjenTermin() {
        return izmenjenTermin;
    }

    public OpstiPregled getOpstiPregled() {
        return opstiPregled;
    }

    public SpecijalistickiPregled getSpecijalistickiPregled() {
        return specijalistickiPregled;
    }
}
