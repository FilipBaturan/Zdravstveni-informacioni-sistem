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
 *         &lt;element name="korisnik"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://localhost:8080/zis/seme/medicinske_sestra}identifikator use="required""/&gt;
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
        "korisnik"
})
@XmlRootElement(name = "medicinska_sestra", namespace = "http://localhost:8080/zis/seme/medicinske_sestra")
public class MedicinskaSestra {

    @XmlElement(namespace = "http://localhost:8080/zis/seme/medicinske_sestra", required = true)
    protected MedicinskaSestra.Korisnik korisnik;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the korisnik property.
     *
     * @return possible object is
     * {@link MedicinskaSestra.Korisnik }
     */
    public MedicinskaSestra.Korisnik getKorisnik() {
        return korisnik;
    }

    /**
     * Sets the value of the korisnik property.
     *
     * @param value allowed object is
     *              {@link MedicinskaSestra.Korisnik }
     */
    public void setKorisnik(MedicinskaSestra.Korisnik value) {
        this.korisnik = value;
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
     *       &lt;attribute ref="{http://localhost:8080/zis/seme/medicinske_sestra}identifikator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Korisnik {

        @XmlAttribute(name = "identifikator", namespace = "http://localhost:8080/zis/seme/medicinske_sestra", required = true)
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
