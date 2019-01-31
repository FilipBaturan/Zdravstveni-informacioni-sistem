package zis.rs.zis.domain.enums;

public enum TipLekara {

    OPSTA_PRAKSA("opsta_praksa"),
    SPECIJALISTA("specijalista"),;

    private final String tekst;

    /**
     * @param tekst string reprezentacija enumeracije
     */
    TipLekara(final String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return tekst;
    }
}
