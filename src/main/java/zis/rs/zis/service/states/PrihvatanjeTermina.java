package zis.rs.zis.service.states;

import org.springframework.stereotype.Service;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class PrihvatanjeTermina extends Stanje {

    @Override
    public String obradiZahtev(Akcija akcija) {
        return null;
    }

    public String azuriranjeTermina(String termin) {
        return null;
    }
}
