package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class PrihvatanjeTermina extends Stanje {

    @Autowired
    private PrihvatanjeTermina prihvatanjeTermina;

    @Override
    public String obradiZahtev(Akcija akcija) {
        if (this.getOpcija() == TipAkcije.IZMENA_PREGLEDA) {
            return prihvatanjeTermina.izmenaTermina(akcija);
        } else if (this.getOpcija() == TipAkcije.ODBIJANJE_PREGLEDA) {
            return prihvatanjeTermina.odbijanjeTermina(akcija);
        } else {
            return prihvatanjeTermina.prihvatanjeTermina(akcija);
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String izmenaTermina(Akcija akcija) {
        return null;
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String odbijanjeTermina(Akcija akcija) {
        return null;
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String prihvatanjeTermina(Akcija akcija) {
        return null;
    }
}
