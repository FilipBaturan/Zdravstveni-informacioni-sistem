package zis.rs.zis.util;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringWriter;

@Component
public class Validator {

    /**
     * @param akcija koju treba pocesirati
     * @param sema koja vrsi validaciju
     * @return sadrzaj akcije
     */
    public String procesirajAkciju(Akcija akcija, String sema) {
        Document dok = ((ElementNSImpl) akcija.getSadrzaj().getAny()).getOwnerDocument();
        if (((Element)dok.getFirstChild()).hasAttribute("xmlns:akc")){  // ukloni nepotrebne namespace-ove
            ((Element)dok.getFirstChild()).removeAttribute("xmlns:akc");
        }
        try {
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(sema))
                    .newValidator().validate(new DOMSource(dok));

            StringWriter w = new StringWriter();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(dok), new StreamResult(w));

            String sadrzaj = w.toString();
            if(sadrzaj.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                sadrzaj = sadrzaj.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            }
            return sadrzaj;
        } catch (TransformerException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e){
            throw new ValidacioniIzuzetak("Nevalidni prosledjeni podaci!");
        }
    }
}
