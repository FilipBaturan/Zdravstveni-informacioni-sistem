//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.17 at 10:33:15 PM CET 
//


package zis.rs.zis.domain.entities;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://zis.rs/zis/seme/korisnik}osoba"/&gt;
 *         &lt;element name="korisnicko_ime"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="lozinka"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *       &lt;attribute name="aktivan" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "ime",
        "prezime",
        "jmbg",
        "korisnickoIme",
        "lozinka"
})
@XmlRootElement(name = "korisnik", namespace = "http://zis.rs/zis/seme/korisnik")
public class Korisnik {

    @XmlElement(namespace = "http://zis.rs/zis/seme/korisnik", required = true)
    protected String ime;
    @XmlElement(namespace = "http://zis.rs/zis/seme/korisnik", required = true)
    protected String prezime;
    @XmlElement(namespace = "http://zis.rs/zis/seme/korisnik", required = true)
    protected String jmbg;
    @XmlElement(name = "korisnicko_ime", namespace = "http://zis.rs/zis/seme/korisnik", required = true)
    protected String korisnickoIme;
    @XmlElement(namespace = "http://zis.rs/zis/seme/korisnik", required = true)
    protected String lozinka;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;
    @XmlAttribute(name = "aktivan", required = true)
    protected boolean aktivan;

    /**
     * Gets the value of the ime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIme() {
        return ime;
    }

    /**
     * Sets the value of the ime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIme(String value) {
        this.ime = value;
    }

    /**
     * Gets the value of the prezime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPrezime() {
        return prezime;
    }

    /**
     * Sets the value of the prezime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrezime(String value) {
        this.prezime = value;
    }

    /**
     * Gets the value of the jmbg property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getJmbg() {
        return jmbg;
    }

    /**
     * Sets the value of the jmbg property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setJmbg(String value) {
        this.jmbg = value;
    }

    /**
     * Gets the value of the korisnickoIme property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     * Sets the value of the korisnickoIme property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKorisnickoIme(String value) {
        this.korisnickoIme = value;
    }

    /**
     * Gets the value of the lozinka property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     * Sets the value of the lozinka property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLozinka(String value) {
        this.lozinka = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the aktivan property.
     */
    public boolean isAktivan() {
        return aktivan;
    }

    /**
     * Sets the value of the aktivan property.
     */
    public void setAktivan(boolean value) {
        this.aktivan = value;
    }

}
