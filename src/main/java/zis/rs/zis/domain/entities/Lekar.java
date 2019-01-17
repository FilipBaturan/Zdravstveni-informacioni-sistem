//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.17 at 10:33:15 PM CET 
//


package zis.rs.zis.domain.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/lekar}identifkator use="required""/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tip"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="ospta_praksa"/&gt;
 *               &lt;enumeration value="ginekologije"/&gt;
 *               &lt;enumeration value="pedijatar"/&gt;
 *               &lt;enumeration value="stomatolog"/&gt;
 *               &lt;enumeration value="dermatolog"/&gt;
 *               &lt;enumeration value="ostalo"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="oblast_zastite"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="deca"/&gt;
 *               &lt;enumeration value="odrasli"/&gt;
 *               &lt;enumeration value="zene"/&gt;
 *               &lt;enumeration value="stomatologija"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "korisnik",
    "tip",
    "oblastZastite"
})
@XmlRootElement(name = "lekar", namespace = "http://zis.rs/zis/seme/lekar")
public class Lekar {

    @XmlElement(namespace = "http://zis.rs/zis/seme/lekar", required = true)
    protected Lekar.Korisnik korisnik;
    @XmlElement(namespace = "http://zis.rs/zis/seme/lekar", required = true, defaultValue = "ospta_praksa")
    protected String tip;
    @XmlElement(name = "oblast_zastite", namespace = "http://zis.rs/zis/seme/lekar", required = true)
    protected String oblastZastite;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the korisnik property.
     *
     * @return
     *     possible object is
     *     {@link Lekar.Korisnik }
     *
     */
    public Lekar.Korisnik getKorisnik() {
        return korisnik;
    }

    /**
     * Sets the value of the korisnik property.
     *
     * @param value
     *     allowed object is
     *     {@link Lekar.Korisnik }
     *
     */
    public void setKorisnik(Lekar.Korisnik value) {
        this.korisnik = value;
    }

    /**
     * Gets the value of the tip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTip() {
        return tip;
    }

    /**
     * Sets the value of the tip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTip(String value) {
        this.tip = value;
    }

    /**
     * Gets the value of the oblastZastite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOblastZastite() {
        return oblastZastite;
    }

    /**
     * Sets the value of the oblastZastite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOblastZastite(String value) {
        this.oblastZastite = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
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
     *       &lt;attribute ref="{http://zis.rs/zis/seme/lekar}identifkator use="required""/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Korisnik {

        @XmlAttribute(name = "identifkator", namespace = "http://zis.rs/zis/seme/lekar", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String identifkator;

        /**
         * Gets the value of the identifkator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentifkator() {
            return identifkator;
        }

        /**
         * Sets the value of the identifkator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentifkator(String value) {
            this.identifkator = value;
        }

    }

}
