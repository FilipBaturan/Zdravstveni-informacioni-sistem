package zis.rs.zis.util;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Component
public class MetaPodaciEkstraktor extends IOStrimer {

    private TransformerFactory transformerFactory;

    @Autowired
    private Maper maper;

    public MetaPodaciEkstraktor() {
        transformerFactory = new TransformerFactoryImpl();
    }

    /**
     * Generise RDF/XML na osnovu RDFa meta podataka  iz XML
     * primenom GRDDL XSL transformacije
     *
     * @param in XML ulazni string
     */
    public ByteArrayInputStream ekstraktujMetaPodatke(InputStream in, OutputStream out) {
//        try {
//            String putanjaDoTransformacije = ResourceUtils
//                    .getFile(maper.dobaviTransformaciju("metapodaci")).getPath();
//            StreamSource transformSource = new StreamSource(new File("/home/themaniac/Documents/XML_i_web_servisi/Projekat/Zdravstveni-informacioni-sistem/src/main/resources/xsl/grddl.xsl"));
//            Transformer grddlTransformer = transformerFactory.newTransformer(transformSource);
//            grddlTransformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
//            grddlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            StringWriter w = new StringWriter();
//            grddlTransformer.transform(new StreamSource(in), new StreamResult(w));
//            return w.toString();
//        } catch (TransformerException | IOException e) {
//            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
//        }
        try {
            // Create transformation source
            StreamSource transformSource = new StreamSource(new File("/home/themaniac/Documents/XML_i_web_servisi/Projekat/Zdravstveni-informacioni-sistem/src/main/resources/xsl/grddl.xsl"));

            // Initialize GRDDL transformer object
            Transformer grddlTransformer = transformerFactory.newTransformer(transformSource);

            // Set the indentation properties
            grddlTransformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            grddlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Initialize transformation subject
            StreamSource source = new StreamSource(in);

            // Initialize result stream
            StreamResult result = new StreamResult(out);

            // Trigger the transformation

            grddlTransformer.transform(source, result);
            return new ByteArrayInputStream(((ByteArrayOutputStream) result.getOutputStream()).toByteArray());
        } catch (TransformerException e) {
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }

    }
}
