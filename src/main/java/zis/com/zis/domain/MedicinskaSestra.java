package zis.com.zis.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "medicinska_sestra")
public class MedicinskaSestra extends Korisnik {

    public MedicinskaSestra() {
    }

    public MedicinskaSestra(Long id, String ime, String prezime, String jmbg, boolean aktivan,
                            String korisnickoIme, String lozinka) {
        super(id, ime, prezime, jmbg, aktivan, korisnickoIme, lozinka);
    }
}
