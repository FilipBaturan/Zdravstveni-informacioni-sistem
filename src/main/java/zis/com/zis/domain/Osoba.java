package zis.com.zis.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract public class Osoba {

    @XmlAttribute(name = "id", required = true)
    protected Long id;

    @XmlElement(name = "ime")
    protected String ime;

    @XmlElement(name = "prezime")
    protected String prezime;

    @XmlElement(name = "jmbg")
    protected String jmbg;

    @XmlElement(name = "aktivan")
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
