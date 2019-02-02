package zis.rs.zis.domain.enums;

public enum Stanja {

    ZAKAZIVANJE_TERMINA("zakazivanje"),
    CEKANJE("cekanje"),
    IZMENJEN_TERMIN("izmenjen_pregled"),
    OPSTI_PREGLED("pregled_lek_op"),
    SPECIJALISTICKI_PREGLED("pregled_lek_spec"),
    KRAJ("kraj");

    private final String tekst;

    /**
     * @param tekst string reprezentacija enumeracije
     */
    Stanja(final String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return tekst;
    }

}
