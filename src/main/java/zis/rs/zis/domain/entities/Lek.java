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
 *         &lt;element name="naziv" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sifra" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="namena" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        "naziv",
        "sifra",
        "namena"
})
@XmlRootElement(name = "lek", namespace = "http://zis.rs/zis/seme/lek")
public class Lek {

    @XmlElement(namespace = "http://zis.rs/zis/seme/lek", required = true)
    protected String naziv;
    @XmlElement(namespace = "http://zis.rs/zis/seme/lek", required = true)
    protected String sifra;
    @XmlElement(namespace = "http://zis.rs/zis/seme/lek", required = true)
    protected String namena;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;
    @XmlAttribute(name = "aktivan", required = true)
    protected boolean aktivan;

    /**
     * Gets the value of the naziv property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Sets the value of the naziv property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNaziv(String value) {
        this.naziv = value;
    }

    /**
     * Gets the value of the sifra property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSifra() {
        return sifra;
    }

    /**
     * Sets the value of the sifra property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSifra(String value) {
        this.sifra = value;
    }

    /**
     * Gets the value of the namena property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNamena() {
        return namena;
    }

    /**
     * Sets the value of the namena property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNamena(String value) {
        this.namena = value;
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
