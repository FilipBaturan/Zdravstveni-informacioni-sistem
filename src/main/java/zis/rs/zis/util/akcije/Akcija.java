//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.18 at 12:56:18 AM CET 
//


package zis.rs.zis.util.akcije;

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
 *         &lt;element name="kontekst" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="funkcija"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="DODAVANJE"/&gt;
 *               &lt;enumeration value="BRISANJE"/&gt;
 *               &lt;enumeration value="IZMENA"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="sadrzaj"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;any minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "kontekst",
        "funkcija",
        "sadrzaj"
})
@XmlRootElement(name = "akcija")
public class Akcija {

    @XmlElement(required = true)
    protected String kontekst;
    @XmlElement(required = true)
    protected String funkcija;
    @XmlElement(required = true)
    protected Akcija.Sadrzaj sadrzaj;

    /**
     * Gets the value of the kontekst property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKontekst() {
        return kontekst;
    }

    /**
     * Sets the value of the kontekst property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKontekst(String value) {
        this.kontekst = value;
    }

    /**
     * Gets the value of the funkcija property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFunkcija() {
        return funkcija;
    }

    /**
     * Sets the value of the funkcija property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFunkcija(String value) {
        this.funkcija = value;
    }

    /**
     * Gets the value of the sadrzaj property.
     *
     * @return possible object is
     * {@link Akcija.Sadrzaj }
     */
    public Akcija.Sadrzaj getSadrzaj() {
        return sadrzaj;
    }

    /**
     * Sets the value of the sadrzaj property.
     *
     * @param value allowed object is
     *              {@link Akcija.Sadrzaj }
     */
    public void setSadrzaj(Akcija.Sadrzaj value) {
        this.sadrzaj = value;
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
     *       &lt;sequence&gt;
     *         &lt;any minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "any"
    })
    public static class Sadrzaj {

        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Gets the value of the any property.
         *
         * @return possible object is
         * {@link Object }
         */
        public Object getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         *
         * @param value allowed object is
         *              {@link Object }
         */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}
