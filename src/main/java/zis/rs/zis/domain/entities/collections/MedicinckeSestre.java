//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.17 at 10:33:15 PM CET 
//


package zis.rs.zis.domain.entities.collections;


import zis.rs.zis.domain.entities.MedicinskaSestra;

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
 *         &lt;element ref="{http://localhost:8080/zis/seme/medicinske_sestra}medicinska_sestra" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "medicinskaSestra"
})
@XmlRootElement(name = "medicincke_sestre", namespace = "http://zis.rs/zis/seme/medicinske_sestre")
public class MedicinckeSestre {

    @XmlElement(name = "medicinska_sestra", namespace = "http://localhost:8080/zis/seme/medicinske_sestra")
    protected List<MedicinskaSestra> medicinskaSestra;

    /**
     * Gets the value of the medicinskaSestra property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the medicinskaSestra property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMedicinskaSestra().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MedicinskaSestra }
     */
    public List<MedicinskaSestra> getMedicinskaSestra() {
        if (medicinskaSestra == null) {
            medicinskaSestra = new ArrayList<MedicinskaSestra>();
        }
        return this.medicinskaSestra;
    }

}
