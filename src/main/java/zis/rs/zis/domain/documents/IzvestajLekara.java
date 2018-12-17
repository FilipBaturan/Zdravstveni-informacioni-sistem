//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.17 at 02:07:47 AM CET 
//


package zis.rs.zis.domain.documents;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="dijagnoza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="anamneza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="terapija" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="datum" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="pacijent"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/izvestaj_lekara}identifikator use="required""/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="lekar"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/izvestaj_lekara}identifikator use="required""/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="oznaka" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="il1" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dijagnoza",
        "anamneza",
        "terapija",
        "datum",
        "pacijent",
        "lekar"
})
@XmlRootElement(name = "izvestaj_lekara", namespace = "http://zis.rs/zis/seme/izvestaj_lekara")
public class IzvestajLekara {

    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    protected String dijagnoza;
    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    protected String anamneza;
    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    protected String terapija;
    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datum;
    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    protected IzvestajLekara.Pacijent pacijent;
    @XmlElement(namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
    protected IzvestajLekara.Lekar lekar;
    @XmlAttribute(name = "oznaka", required = true)
    protected String oznaka;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the dijagnoza property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDijagnoza() {
        return dijagnoza;
    }

    /**
     * Sets the value of the dijagnoza property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDijagnoza(String value) {
        this.dijagnoza = value;
    }

    /**
     * Gets the value of the anamneza property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAnamneza() {
        return anamneza;
    }

    /**
     * Sets the value of the anamneza property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAnamneza(String value) {
        this.anamneza = value;
    }

    /**
     * Gets the value of the terapija property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTerapija() {
        return terapija;
    }

    /**
     * Sets the value of the terapija property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTerapija(String value) {
        this.terapija = value;
    }

    /**
     * Gets the value of the datum property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatum() {
        return datum;
    }

    /**
     * Sets the value of the datum property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDatum(XMLGregorianCalendar value) {
        this.datum = value;
    }

    /**
     * Gets the value of the pacijent property.
     *
     * @return possible object is
     * {@link IzvestajLekara.Pacijent }
     */
    public IzvestajLekara.Pacijent getPacijent() {
        return pacijent;
    }

    /**
     * Sets the value of the pacijent property.
     *
     * @param value allowed object is
     *              {@link IzvestajLekara.Pacijent }
     */
    public void setPacijent(IzvestajLekara.Pacijent value) {
        this.pacijent = value;
    }

    /**
     * Gets the value of the lekar property.
     *
     * @return possible object is
     * {@link IzvestajLekara.Lekar }
     */
    public IzvestajLekara.Lekar getLekar() {
        return lekar;
    }

    /**
     * Sets the value of the lekar property.
     *
     * @param value allowed object is
     *              {@link IzvestajLekara.Lekar }
     */
    public void setLekar(IzvestajLekara.Lekar value) {
        this.lekar = value;
    }

    /**
     * Gets the value of the oznaka property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOznaka() {
        if (oznaka == null) {
            return "il1";
        } else {
            return oznaka;
        }
    }

    /**
     * Sets the value of the oznaka property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOznaka(String value) {
        this.oznaka = value;
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
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute ref="{http://zis.rs/zis/seme/izvestaj_lekara}identifikator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Lekar {

        @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String identifikator;

        /**
         * Gets the value of the identifikator property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getIdentifikator() {
            return identifikator;
        }

        /**
         * Sets the value of the identifikator property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setIdentifikator(String value) {
            this.identifikator = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute ref="{http://zis.rs/zis/seme/izvestaj_lekara}identifikator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Pacijent {

        @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/izvestaj_lekara", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String identifikator;

        /**
         * Gets the value of the identifikator property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getIdentifikator() {
            return identifikator;
        }

        /**
         * Sets the value of the identifikator property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setIdentifikator(String value) {
            this.identifikator = value;
        }

    }

}
