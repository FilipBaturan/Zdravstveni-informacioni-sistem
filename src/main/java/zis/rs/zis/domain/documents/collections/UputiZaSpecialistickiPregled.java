//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.15 at 04:55:53 PM CET 
//


package zis.rs.zis.domain.documents.collections;

import zis.rs.zis.domain.documents.UputZaSpecialistickiPregled;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{http://zis.rs/zis/seme/uput_za_specialisticki_pregled}uput_za_specialisticki_pregled" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "uputZaSpecialistickiPregled"
})
@XmlRootElement(name = "uputi_za_specialisticki_pregled", namespace = "http://zis.rs/zis/seme/uputi_za_specialisticki_pregled")
public class UputiZaSpecialistickiPregled {

    @XmlElement(name = "uput_za_specialisticki_pregled", namespace = "http://zis.rs/zis/seme/uput_za_specialisticki_pregled")
    protected List<UputZaSpecialistickiPregled> uputZaSpecialistickiPregled;

    /**
     * Gets the value of the uputZaSpecialistickiPregled property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uputZaSpecialistickiPregled property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUputZaSpecialistickiPregled().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UputZaSpecialistickiPregled }
     */
    public List<UputZaSpecialistickiPregled> getUputZaSpecialistickiPregled() {
        if (uputZaSpecialistickiPregled == null) {
            uputZaSpecialistickiPregled = new ArrayList<UputZaSpecialistickiPregled>();
        }
        return this.uputZaSpecialistickiPregled;
    }

}
