package zis.rs.zis.domain.enums;

public enum TipKorisnika {

    LEKAR("LEKAR"),
    MEDICINSKA_SESTRA("SESTRA"),
    PACIJENT("PACIJENT");

    private final String tekst;

    /**
     * @param tekst string reprezentacija enumeracije
     */
    TipKorisnika(final String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return tekst;
    }
}
