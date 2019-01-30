package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class OpstiPregled extends Stanje {

    @Autowired
    private OpstiPregled opstiPregled;

    @Override
    public String obradiZahtev(Akcija akcija) {
        return opstiPregled.kreirajDokumentaciju(akcija);
    }

    /**
     * Kreiranje lekarskog izvestaja i opciono
     * lekarskog recepta i izvestaja
     *
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String kreirajDokumentaciju(Akcija akcija) {
        return null;
    }
}
