package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.Stanja;
import zis.rs.zis.service.nonProcessService.StanjaPregledaServis;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Proces {

    @Autowired
    private StanjaPregledaServis servis;

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

    @PostConstruct
    public void init() {
        this.inicijalizacija();
    }

    private void inicijalizacija() {
        List<String> stanja = servis.dobaviProcese();
        for (String stanje : stanja) {
            String[] tokeni = stanje.split(",");
            if (tokeni[1].equals(Stanja.OPSTI_PREGLED.toString())) {
                procesi.put(tokeni[0], opstiPregled);
            } else if (tokeni[1].equals(Stanja.SPECIJALISTICKI_PREGLED.toString())) {
                procesi.put(tokeni[0], specijalistickiPregled);
            } else if (tokeni[1].equals(Stanja.ZAKAZIVANJE_TERMINA.toString())) {
                procesi.put(tokeni[0], zakazivanjePregleda);
            } else if (tokeni[1].equals(Stanja.CEKANJE.toString())) {
                procesi.put(tokeni[0], prihvatanjeTermina);
            } else if (tokeni[1].equals(Stanja.IZMENJEN_TERMIN.toString())) {
                procesi.put(tokeni[0], izmenjenTermin);
            }
        }
    }

    public String obradiZahtev(Akcija akcija) {
        try {
            return this.procesi.getOrDefault(maper.dobaviPacijentaIzPregleda(akcija), zakazivanjePregleda)
                    .obradiZahtev(akcija);
        } catch (NullPointerException e) {
            try {
                return this.procesi.getOrDefault(maper.dobaviPacijentaIzIzvestaja(akcija), zakazivanjePregleda)
                        .obradiZahtev(akcija);
            } catch (NullPointerException ex) {
                return this.procesi.getOrDefault(maper.dobaviPacijentaIzDokumentacije(akcija), zakazivanjePregleda)
                        .obradiZahtev(akcija);
            }
        }
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
