package zis.com.zis.domain;

public class Korisnik extends Osoba {

    private String korisnickoIme;
    private String lozinka;

    public Korisnik() {
    }

    public Korisnik(Long id, String ime, String prezime, String jmbg, boolean aktivan, String korisnickoIme, String lozinka) {
        super(id, ime, prezime, jmbg, aktivan);
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
