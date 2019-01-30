package zis.rs.zis.service.states;

import org.springframework.stereotype.Component;
import zis.rs.zis.util.akcije.Akcija;

@Component
public class SpecijalistickiPregled extends Stanje {


    @Override
    public String obradiZahtev(Akcija akcija) {
        return null;
    }

    /**
     * Azurira vec postojeci uput
     *
     * @param akcija koju treba izvrsiti
     * @return poruka o rezultatu akcije
     */
    public String azuriranjeUputa(Akcija akcija) {
        return null;
    }

}
