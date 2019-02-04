package zis.rs.zis.util;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

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
        try {
            StreamSource transformSource = new StreamSource(new File(ResourceUtils
                    .getFile(maper.dobaviTransformaciju("metapodaci")).getPath()));
            Transformer grddlTransformer = transformerFactory.newTransformer(transformSource);
            grddlTransformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            grddlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamSource source = new StreamSource(in);
            StreamResult result = new StreamResult(out);
            grddlTransformer.transform(source, result);
            return new ByteArrayInputStream(((ByteArrayOutputStream) result.getOutputStream()).toByteArray());
        } catch (TransformerException | FileNotFoundException e) {
            throw new TransformacioniIzuzetak("Greska prilikom obrade podataka!");
        }

    }
}
