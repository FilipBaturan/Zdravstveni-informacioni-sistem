package zis.com.zis.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lekar")
public class Lekar extends Korisnik {

    @XmlElement(name = "tip")
    private TipLekara tip;

    public Lekar() {
    }

    public Lekar(Long id, String ime, String prezime, String jmbg, boolean aktivan, String korisnickoIme,
                 String lozinka, TipLekara tip) {
        super(id, ime, prezime, jmbg, aktivan, korisnickoIme, lozinka);
        this.tip = tip;
    }

    public TipLekara getTip() {
        return tip;
    }

    public void setTip(TipLekara tip) {
        this.tip = tip;
    }
}
