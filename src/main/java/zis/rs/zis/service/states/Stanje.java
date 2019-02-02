package zis.rs.zis.service.states;

import zis.rs.zis.domain.enums.Opcije;
import zis.rs.zis.util.akcije.Akcija;

public abstract class Stanje {

    private Opcije opcija;

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    abstract String obradiZahtev(Akcija akcija);

    public Opcije getOpcija() {
        return opcija;
    }

    public void setOpcija(Opcije opcija) {
        this.opcija = opcija;
    }
}
