package zis.rs.zis.service.states;

import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.util.akcije.Akcija;

public abstract class Stanje {

    private TipAkcije opcija;

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    abstract String obradiZahtev(Akcija akcija);

    public TipAkcije getOpcija() {
        return opcija;
    }

    public void setOpcija(TipAkcije opcija) {
        this.opcija = opcija;
    }
}
