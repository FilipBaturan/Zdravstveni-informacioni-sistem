package zis.rs.zis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.TransformacioniIzuzetak;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

public class ValidatorKontoler {

    @Autowired
    protected Maper maper;

    protected void validirajAkciju(Akcija akcija) {
        try {
            Marshaller marshaller = JAXBContext.newInstance(zis.rs.zis.util.akcije.ObjectFactory.class)
                    .createMarshaller();
            Document dok = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            marshaller.marshal(akcija, dok);

            Node sadrzaj = dok.getElementsByTagName("sadrzaj").item(0);
            obrisiSadrzaj(sadrzaj);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(maper.dobaviSemu("akcija")))
                    .newValidator().validate(new DOMSource(dok));
        } catch (JAXBException | ParserConfigurationException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e) {
            throw new ValidacioniIzuzetak("Nevalidna prosledjena akcija!");
        }
    }

    private void obrisiSadrzaj(Node sadrzaj) {
        while (sadrzaj.hasChildNodes())
            sadrzaj.removeChild(sadrzaj.getFirstChild());
    }
}
