package zis.rs.zis.domain.enums;

public enum Kontekst {

    PRIHVATANJE("PRIHVATANJE"),
    IZMENA("IZMENA");

    private final String tekst;

    /**
     * @param tekst string reprezentacija enumeracije
     */
    Kontekst(final String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return tekst;
    }
}
