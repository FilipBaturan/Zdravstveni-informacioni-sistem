package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzmenjenTermin extends Stanje {

    @Autowired
    private IzmenjenTermin izmenjenTermin;

    @Override
    public String obradiZahtev(Akcija akcija) {
        if (this.getOpcija() == TipAkcije.ODBIJANJE_PREGLEDA) {
            return izmenjenTermin.odbijanjeTermina(akcija);
        } else  {
            return izmenjenTermin.prihvatanjeTermina(akcija);
        }
    }

    /**
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String odbijanjeTermina(Akcija akcija) {
        return null;
    }

    /**
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String prihvatanjeTermina(Akcija akcija) {
        return null;
    }
}
