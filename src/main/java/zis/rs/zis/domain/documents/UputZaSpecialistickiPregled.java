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
 *         &lt;element name="zdrastvene_ustanove"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="primalac" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="posaljilac" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="osigurano_lice"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="pregled"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="datum_prijavljivanja" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="datum_zavrsetka" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="anamneza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="broj_protokola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;pattern value="[0-9]{5}"/&gt;
 *                         &lt;length value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="lekar"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="izvestaj_specialiste"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="bolest" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="nalazi" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="specialista"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="oznaka" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="oz2" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "zdrastveneUstanove",
        "osiguranoLice",
        "pregled",
        "izvestajSpecialiste"
})
@XmlRootElement(name = "uput_za_specialisticki_pregled", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled")
public class UputZaSpecialistickiPregled {

    @XmlElement(name = "zdrastvene_ustanove", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
    protected UputZaSpecialistickiPregled.ZdrastveneUstanove zdrastveneUstanove;
    @XmlElement(name = "osigurano_lice", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
    protected UputZaSpecialistickiPregled.OsiguranoLice osiguranoLice;
    @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
    protected UputZaSpecialistickiPregled.Pregled pregled;
    @XmlElement(name = "izvestaj_specialiste", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
    protected UputZaSpecialistickiPregled.IzvestajSpecialiste izvestajSpecialiste;
    @XmlAttribute(name = "oznaka", required = true)
    protected String oznaka;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the zdrastveneUstanove property.
     *
     * @return possible object is
     * {@link UputZaSpecialistickiPregled.ZdrastveneUstanove }
     */
    public UputZaSpecialistickiPregled.ZdrastveneUstanove getZdrastveneUstanove() {
        return zdrastveneUstanove;
    }

    /**
     * Sets the value of the zdrastveneUstanove property.
     *
     * @param value allowed object is
     *              {@link UputZaSpecialistickiPregled.ZdrastveneUstanove }
     */
    public void setZdrastveneUstanove(UputZaSpecialistickiPregled.ZdrastveneUstanove value) {
        this.zdrastveneUstanove = value;
    }

    /**
     * Gets the value of the osiguranoLice property.
     *
     * @return possible object is
     * {@link UputZaSpecialistickiPregled.OsiguranoLice }
     */
    public UputZaSpecialistickiPregled.OsiguranoLice getOsiguranoLice() {
        return osiguranoLice;
    }

    /**
     * Sets the value of the osiguranoLice property.
     *
     * @param value allowed object is
     *              {@link UputZaSpecialistickiPregled.OsiguranoLice }
     */
    public void setOsiguranoLice(UputZaSpecialistickiPregled.OsiguranoLice value) {
        this.osiguranoLice = value;
    }

    /**
     * Gets the value of the pregled property.
     *
     * @return possible object is
     * {@link UputZaSpecialistickiPregled.Pregled }
     */
    public UputZaSpecialistickiPregled.Pregled getPregled() {
        return pregled;
    }

    /**
     * Sets the value of the pregled property.
     *
     * @param value allowed object is
     *              {@link UputZaSpecialistickiPregled.Pregled }
     */
    public void setPregled(UputZaSpecialistickiPregled.Pregled value) {
        this.pregled = value;
    }

    /**
     * Gets the value of the izvestajSpecialiste property.
     *
     * @return possible object is
     * {@link UputZaSpecialistickiPregled.IzvestajSpecialiste }
     */
    public UputZaSpecialistickiPregled.IzvestajSpecialiste getIzvestajSpecialiste() {
        return izvestajSpecialiste;
    }

    /**
     * Sets the value of the izvestajSpecialiste property.
     *
     * @param value allowed object is
     *              {@link UputZaSpecialistickiPregled.IzvestajSpecialiste }
     */
    public void setIzvestajSpecialiste(UputZaSpecialistickiPregled.IzvestajSpecialiste value) {
        this.izvestajSpecialiste = value;
    }

    /**
     * Gets the value of the oznaka property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOznaka() {
        if (oznaka == null) {
            return "oz2";
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
     *       &lt;sequence&gt;
     *         &lt;element name="bolest" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="nalazi" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="specialista"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
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
            "bolest",
            "nalazi",
            "specialista"
    })
    public static class IzvestajSpecialiste {

        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String bolest;
        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String nalazi;
        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected UputZaSpecialistickiPregled.IzvestajSpecialiste.Specialista specialista;

        /**
         * Gets the value of the bolest property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getBolest() {
            return bolest;
        }

        /**
         * Sets the value of the bolest property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setBolest(String value) {
            this.bolest = value;
        }

        /**
         * Gets the value of the nalazi property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getNalazi() {
            return nalazi;
        }

        /**
         * Sets the value of the nalazi property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setNalazi(String value) {
            this.nalazi = value;
        }

        /**
         * Gets the value of the specialista property.
         *
         * @return possible object is
         * {@link UputZaSpecialistickiPregled.IzvestajSpecialiste.Specialista }
         */
        public UputZaSpecialistickiPregled.IzvestajSpecialiste.Specialista getSpecialista() {
            return specialista;
        }

        /**
         * Sets the value of the specialista property.
         *
         * @param value allowed object is
         *              {@link UputZaSpecialistickiPregled.IzvestajSpecialiste.Specialista }
         */
        public void setSpecialista(UputZaSpecialistickiPregled.IzvestajSpecialiste.Specialista value) {
            this.specialista = value;
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
         *       &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Specialista {

            @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled")
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


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class OsiguranoLice {

        @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled")
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
     *       &lt;sequence&gt;
     *         &lt;element name="datum_prijavljivanja" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="datum_zavrsetka" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="anamneza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="broj_protokola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-9]{5}"/&gt;
     *               &lt;length value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="lekar"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
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
            "datumPrijavljivanja",
            "datumZavrsetka",
            "anamneza",
            "brojProtokola",
            "lekar"
    })
    public static class Pregled {

        @XmlElement(name = "datum_prijavljivanja", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datumPrijavljivanja;
        @XmlElement(name = "datum_zavrsetka", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datumZavrsetka;
        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String anamneza;
        @XmlElement(name = "broj_protokola", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String brojProtokola;
        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected UputZaSpecialistickiPregled.Pregled.Lekar lekar;

        /**
         * Gets the value of the datumPrijavljivanja property.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getDatumPrijavljivanja() {
            return datumPrijavljivanja;
        }

        /**
         * Sets the value of the datumPrijavljivanja property.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setDatumPrijavljivanja(XMLGregorianCalendar value) {
            this.datumPrijavljivanja = value;
        }

        /**
         * Gets the value of the datumZavrsetka property.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getDatumZavrsetka() {
            return datumZavrsetka;
        }

        /**
         * Sets the value of the datumZavrsetka property.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setDatumZavrsetka(XMLGregorianCalendar value) {
            this.datumZavrsetka = value;
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
         * Gets the value of the brojProtokola property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getBrojProtokola() {
            return brojProtokola;
        }

        /**
         * Sets the value of the brojProtokola property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setBrojProtokola(String value) {
            this.brojProtokola = value;
        }

        /**
         * Gets the value of the lekar property.
         *
         * @return possible object is
         * {@link UputZaSpecialistickiPregled.Pregled.Lekar }
         */
        public UputZaSpecialistickiPregled.Pregled.Lekar getLekar() {
            return lekar;
        }

        /**
         * Sets the value of the lekar property.
         *
         * @param value allowed object is
         *              {@link UputZaSpecialistickiPregled.Pregled.Lekar }
         */
        public void setLekar(UputZaSpecialistickiPregled.Pregled.Lekar value) {
            this.lekar = value;
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
         *       &lt;attribute ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}identifikator"/&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Lekar {

            @XmlAttribute(name = "identifikator", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled")
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
     *         &lt;element name="primalac" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="posaljilac" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "primalac",
            "posaljilac"
    })
    public static class ZdrastveneUstanove {

        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String primalac;
        @XmlElement(namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled", required = true)
        protected String posaljilac;

        /**
         * Gets the value of the primalac property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPrimalac() {
            return primalac;
        }

        /**
         * Sets the value of the primalac property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPrimalac(String value) {
            this.primalac = value;
        }

        /**
         * Gets the value of the posaljilac property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPosaljilac() {
            return posaljilac;
        }

        /**
         * Sets the value of the posaljilac property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPosaljilac(String value) {
            this.posaljilac = value;
        }

    }

}
