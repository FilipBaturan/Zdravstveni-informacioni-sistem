package zis.com.zis.domain;

abstract public class Osoba {

    protected Long id;
    protected String ime;
    protected String prezime;
    protected String jmbg;
    protected boolean aktivan;

    public Osoba() {
    }

    public Osoba(Long id, String ime, String prezime, String jmbg, boolean aktivan) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.aktivan = aktivan;
    }
}
