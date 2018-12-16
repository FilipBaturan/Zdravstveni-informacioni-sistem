package zis.rs.zis.service.implementation;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import zis.rs.zis.domain.ObjectFactory;
import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.entities.Lekar;
import zis.rs.zis.domain.entities.collections.Lekari;
import zis.rs.zis.service.definition.KorisnikServis;
import zis.rs.zis.service.definition.LekarServis;
import zis.rs.zis.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LekarServisImpl implements LekarServis {

    private static final Logger logger = LoggerFactory.getLogger(LekarServisImpl.class);

    private static final String TARGET_NAMESPACE = "http://localhost:8080/zis/seme/lekari";

    @Autowired
    private KorisnikServis korisnikServis;

    @Autowired
    private XMLDBKonekcija konekcija;

    @Override
    public Lekari dobaviSve() {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekari",
                    "lekari.xml");

            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Lekari lekari = (Lekari) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            konekcija.oslobodiResurse(resursi);
            return lekari;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
    }

    /**
     * Convenience method for reading file contents into a string.
     */
    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public void test() throws Exception {
        ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekari",
                "lekari.xml");


        //String xqueryFilePath =  "templates/xquery/lekari/pregragaLekaraPoId.xqy";
        File file = ResourceUtils.getFile("classpath:templates/xquery/lekari/pregragaLekaraPoId.xqy");
        String xqueryFilePath = file.getPath();
        //logger.info(file.getPath());
        // get an instance of xquery service
        XQueryService xqueryService = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
        xqueryService.setProperty("indent", "yes");

        // make the service aware of namespaces
        xqueryService.setNamespace("l", TARGET_NAMESPACE);

        // read xquery
        logger.info("[INFO] Invoking XQuery service for: " + xqueryFilePath);
        String xqueryExpression = this.readFile(xqueryFilePath, StandardCharsets.UTF_8);
        logger.info(xqueryExpression);

        // compile and execute the expression
        CompiledExpression compiledXquery = xqueryService.compile(xqueryExpression);
        ResourceSet result = xqueryService.execute(compiledXquery);

        // handle the results
        logger.info("[INFO] Handling the results... ");

        ResourceIterator i = result.getIterator();
        Resource res = null;

        while (i.hasMoreResources()) {

            try {
                res = i.nextResource();
                logger.info(res.getContent().toString());
            } finally {

                // don't forget to cleanup resources
                try {
                    ((EXistResource) res).freeResources();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }
    }

    @Override
    public String dobaviSveKaskadno() {
        return null;
    }

    @Override
    public Lekar pretragaPoId(String id) {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekari",
                    "lekari.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Lekari lekari = (Lekari) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            for (Lekar l : lekari.getLekar()) {
                if (l.getId().equals(id)) {
                    konekcija.oslobodiResurse(resursi);
                    return l;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        throw new ValidacioniIzuzetak("Trazeni lekar ne postoji u bazi!");
    }

    @Override
    public String pretragaPoIdKaskadno(String id) {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekari",
                    "lekari.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Lekari lekari = (Lekari) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            Lekar lekar = null;

            for (Lekar l : lekari.getLekar()) {
                if (l.getId().equals(id)) {
                    lekar = l;
                    break;
                }
            }
            if (lekar == null) {
                konekcija.oslobodiResurse(resursi);
                throw new ValidacioniIzuzetak("Trazeni lekar ne postoji u bazi!");
            } else {
                Korisnik korisnik = korisnikServis.pretragaPoId(lekar.getKorisnik().getIdentifkator());

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                docFactory.setNamespaceAware(true);
                docFactory.setIgnoringComments(true);
                docFactory.setIgnoringElementContentWhitespace(true);

                JAXBContext lekarContext = JAXBContext.newInstance(Lekar.class);
                JAXBContext korisnikContext = JAXBContext.newInstance(Korisnik.class);
                Marshaller lekarMarshaller = lekarContext.createMarshaller();
                Marshaller korisnikMarshaller = korisnikContext.createMarshaller();

                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                lekarMarshaller.marshal(lekar, doc);
                Node koren = doc.getFirstChild();

                korisnikMarshaller.marshal(korisnik, koren.getFirstChild());
                Node krs = koren.getFirstChild().getFirstChild();
                //ukloni lozinku
                krs.removeChild(krs.getLastChild());
                koren.replaceChild(krs, koren.getFirstChild());

                StringWriter sw = new StringWriter();
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.transform(new DOMSource(doc), new StreamResult(sw));

                konekcija.oslobodiResurse(resursi);
                return sw.toString();
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException | ParserConfigurationException | TransformerException e) {
            throw new TransformacioniIzuzetak("Onemoguvena obrada podataka!");
        }
    }


    @Override
    public String sacuvaj(Lekar lekar) {
        return null;
    }

    @Override
    public void obrisi(String id) {

    }
}
