package zis.rs.zis.service.states;

import zis.rs.zis.util.akcije.Akcija;

public abstract class Stanje {

    private long opcija;

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    abstract String obradiZahtev(Akcija akcija);

    public long getOpcija() {
        return opcija;
    }

    public void setOpcija(long opcija) {
        this.opcija = opcija;
    }
}
