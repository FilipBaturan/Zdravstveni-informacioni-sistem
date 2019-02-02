package zis.rs.zis.domain.enums;

public enum TipAkcije {

    DODAVANJE("DODAVANJE"),
    IZMENA("IZMENA"),
    BRISANJE("BRISANJE");

    private final String tekst;

    /**
     * @param tekst string reprezentacija enumeracije
     */
    TipAkcije(final String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return tekst;
    }
}
