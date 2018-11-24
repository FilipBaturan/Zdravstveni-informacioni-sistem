package zis.com.zis.domain;

import java.util.Date;

public class Pregled {

    private Long id;
    private Lekar lekar;
    private Pacijent pacijent;
    private Date datum;
    private boolean aktivan;

    public Pregled() {
    }

    public Pregled(Long id, Lekar lekar, Pacijent pacijent, Date datum, boolean aktivan) {
        this.id = id;
        this.lekar = lekar;
        this.pacijent = pacijent;
        this.datum = datum;
        this.aktivan = aktivan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lekar getLekar() {
        return lekar;
    }

    public void setLekar(Lekar lekar) {
        this.lekar = lekar;
    }

    public Pacijent getPacijent() {
        return pacijent;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }
}
