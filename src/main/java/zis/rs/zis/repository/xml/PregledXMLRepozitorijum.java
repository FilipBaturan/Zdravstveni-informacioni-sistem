package zis.rs.zis.repository.xml;

import org.exist.xmldb.EXistResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Service
public class PregledXMLRepozitorijum extends IOStrimer{

    private static final Logger logger = LoggerFactory.getLogger(PregledXMLRepozitorijum.class);

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private Sekvencer sekvencer;


    public String pretragaPoId(String id) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("pregledi"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("pretragaPoIdPregleda")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija()
                    .getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), id);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {

                try {
                    res = i.nextResource();
                    sb.append(res.getContent().toString());
                } finally {
                    if (res != null)
                        ((EXistResource) res).freeResources();

                }
            }
            String pregledi = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (pregledi.isEmpty()) {
                throw new ValidacioniIzuzetak("Ne postoji ni jedan pregled u bazi!");
            } else {
                return pregledi;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    public String sacuvaj(String pregled) {

        String prefiks = maper.koverturjUDokument(pregled).getFirstChild().getNodeName().split(":")[0];
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("pregledi"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();
            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String putanja = maper.dobaviPutanju("pregledi");

            Long id = sekvencer.dobaviId();
            this.validirajPregled(maper.koverturjUDokument(pregled).getFirstChild(), id,  prefiks);

            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    prefiks, maper.dobaviPrefiks("pregled"), maper.dobaviPutanju("pregledi"), pregled,
                    maper.dobaviPrefiks("pregledi"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("pregledi"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            return "Pregled uspesno dodat!";
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    /**
     * @param pregled kojeg treba validirati
     * @return xml reprezentacija pregleda
     */
    private String validirajPregled(Node pregled, Long id, String prefiks) {
        DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
        fabrika.setNamespaceAware(true);
        try {
            Document dok = fabrika.newDocumentBuilder().newDocument();
            Node importovan = dok.importNode(pregled, true);
            dok.appendChild(importovan);

            this.proveriOgranicenjaPregleda(dok, prefiks);
            ((Element) dok.getFirstChild()).setAttribute("id", maper.dobaviURI("pregled") + id);

            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ResourceUtils.getFile(maper.dobaviSemu("pregled")))
                    .newValidator().validate(new DOMSource(dok));
            return this.konvertujUString(dok);
        } catch (ParserConfigurationException | IOException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        } catch (SAXException e) {
            throw new ValidacioniIzuzetak("Nevalidan sadrzaj pregleda!");
        }
    }

    private void proveriOgranicenjaPregleda(Document dokument, String prefiks) {
        String lekar = dokument.getElementsByTagName(prefiks + ":lekar")
                .item(0).getAttributes().item(0).getNodeValue();

        String datum = dokument.getElementsByTagName(prefiks + ":datum").item(0).getTextContent();

        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(), maper.dobaviDokument("pregledi"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("ogranicenjaPregleda")).getPath();
            XQueryService upitServis = (XQueryService) resursi.getKolekcija().getService("XQueryService", "1.0");
            upitServis.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita), lekar, datum);
            CompiledExpression kompajliraniSadrzajUpita = upitServis.compile(sadrzajUpita);
            ResourceSet rezultat = upitServis.execute(kompajliraniSadrzajUpita);
            ResourceIterator i = rezultat.getIterator();
            Resource res = null;

            StringBuilder sb = new StringBuilder();

            while (i.hasMoreResources()) {
                try {
                    res = i.nextResource();
                    sb.append(DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    + res.getContent().toString()))).getFirstChild().getTextContent());
                } finally {
                    if (res != null)
                        ((EXistResource) res).freeResources();
                }
            }
            String greska = sb.toString();
            konekcija.oslobodiResurse(resursi);
            if (!greska.isEmpty()) {
                throw new ValidacioniIzuzetak(greska);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException |
                IOException | ParserConfigurationException | SAXException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    private String konvertujUString(Document dokument) {
        StringWriter w = new StringWriter();

        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(dokument), new StreamResult(w));

            String sadrzaj = w.toString();
            if (sadrzaj.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                sadrzaj = sadrzaj.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            }
            return sadrzaj;
        } catch (TransformerException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
    }



}
