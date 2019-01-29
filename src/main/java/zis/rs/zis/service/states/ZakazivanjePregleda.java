package zis.rs.zis.service.states;

import org.springframework.stereotype.Service;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class ZakazivanjePregleda {

    /**
     * Kreira novi kod termim kod lekara opste prakse
     * ili kod lekara specijaliste
     *
     * @param akcija koju treba izvrsiti
     * @return poruka o rezultatu akcije
     */
    public String kreirajPregled(Akcija akcija) {
        return "Test";
    }
}
