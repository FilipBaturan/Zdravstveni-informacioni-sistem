//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.17 at 02:07:47 AM CET 
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
 *         &lt;element name="korisnik"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/pacijent}identifikator use="required""/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="zdrastveni_karton"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/pacijent}identifikator use="required""/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "korisnik",
        "zdrastveniKarton"
})
@XmlRootElement(name = "pacijent", namespace = "http://zis.rs/zis/seme/pacijent")
public class Pacijent {

    @XmlElement(namespace = "http://zis.rs/zis/seme/pacijent", required = true)
    protected Pacijent.Korisnik korisnik;
    @XmlElement(name = "zdrastveni_karton", namespace = "http://zis.rs/zis/seme/pacijent", required = true)
    protected Pacijent.ZdrastveniKarton zdrastveniKarton;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the korisnik property.
     *
     * @return possible object is
     * {@link Pacijent.Korisnik }
     */
    public Pacijent.Korisnik getKorisnik() {
        return korisnik;
    }

    /**
     * Sets the value of the korisnik property.
     *
     * @param value allowed object is
     *              {@link Pacijent.Korisnik }
     */
    public void setKorisnik(Pacijent.Korisnik value) {
        this.korisnik = value;
    }

    /**
     * Gets the value of the zdrastveniKarton property.
     *
     * @return possible object is
     * {@link Pacijent.ZdrastveniKarton }
     */
    public Pacijent.ZdrastveniKarton getZdrastveniKarton() {
        return zdrastveniKarton;
    }

    /**
     * Sets the value of the zdrastveniKarton property.
     *
     * @param value allowed object is
     *              {@link Pacijent.ZdrastveniKarton }
     */
    public void setZdrastveniKarton(Pacijent.ZdrastveniKarton value) {
        this.zdrastveniKarton = value;
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
     *       &lt;attribute ref="{http://zis.rs/zis/seme/pacijent}identifikator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Korisnik {

        @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/pacijent", required = true)
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
     *       &lt;attribute ref="{http://zis.rs/zis/seme/pacijent}identifikator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ZdrastveniKarton {

        @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/pacijent", required = true)
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
