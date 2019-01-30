package zis.rs.zis.service.states;

import org.springframework.stereotype.Service;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzmenjenTermin extends Stanje {

    @Override
    public String obradiZahtev(Akcija akcija) {
        return null;
    }

    /**
     * @param akcija koju treba izvrsiti
     * @return da li je termin prihvacen
     */
    public boolean prihvatiTermin(Akcija akcija) {
        return true;
    }
}
