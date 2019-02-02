package zis.rs.zis.util;

import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XMLResource;

public class ResursiBaze {

    private Collection kolekcija;
    private XMLResource xmlResurs;

    public ResursiBaze() {
    }

    public ResursiBaze(Collection kolekcija, XMLResource xmlResurs) {
        this.kolekcija = kolekcija;
        this.xmlResurs = xmlResurs;
    }

    public Collection getKolekcija() {
        return kolekcija;
    }

    public void setKolekcija(Collection kolekcija) {
        this.kolekcija = kolekcija;
    }

    public XMLResource getXmlResurs() {
        return xmlResurs;
    }

    public void setXmlResurs(XMLResource xmlResurs) {
        this.xmlResurs = xmlResurs;
    }
}
