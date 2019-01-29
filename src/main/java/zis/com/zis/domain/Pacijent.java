package zis.com.zis.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pacijent")
public class Pacijent extends Korisnik {

    @XmlElement(name = "izabrani_lekar")
    private Lekar izabraniLekar;

    @XmlElement(name = "zdravstveni_karton")
    private ZdrastveniKarton zdrastveniKarton;

    @XmlElement(name = "obavestenja")
    private List<String> obavestenja;

    public Pacijent() {
    }

    public Pacijent(Long id, String ime, String prezime, String jmbg, boolean aktivan, String korisnickoIme,
                    String lozinka, Lekar izabraniLekar, ZdrastveniKarton zdrastveniKarton, List<String> obavestenja) {
        super(id, ime, prezime, jmbg, aktivan, korisnickoIme, lozinka);
        this.izabraniLekar = izabraniLekar;
        this.zdrastveniKarton = zdrastveniKarton;
        this.obavestenja = obavestenja;
    }

    public Lekar getIzabraniLekar() {
        return izabraniLekar;
    }

    public void setIzabraniLekar(Lekar izabraniLekar) {
        this.izabraniLekar = izabraniLekar;
    }

    public ZdrastveniKarton getZdrastveniKarton() {
        return zdrastveniKarton;
    }

    public void setZdrastveniKarton(ZdrastveniKarton zdrastveniKarton) {
        this.zdrastveniKarton = zdrastveniKarton;
    }

    public List<String> getObavestenja() {
        return obavestenja;
    }

    public void setObavestenja(List<String> obavestenja) {
        this.obavestenja = obavestenja;
    }

}
