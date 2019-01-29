package zis.com.zis.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "pregled")
public class Pregled {

    @XmlAttribute(name = "id", required = true)
    private Long id;

    @XmlElement(name = "lekar")
    private Lekar lekar;

    @XmlElement(name = "pacijent")
    private Pacijent pacijent;

    @XmlElement(name = "datum")
    private Date datum;

    @XmlElement(name = "aktivan")
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
