package zis.rs.zis.service.nonProcessService;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.IzborPromenaXMLRepozitorijum;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.repository.xml.ReceptXMLRepozitorijum;
import zis.rs.zis.repository.xml.UputXMLRepozitorijum;
import zis.rs.zis.util.transformatori.PDFTransformator;

import java.io.*;

@Service
public class TransformacijeServis {

    @Autowired
    private PDFTransformator pdfTransformator;

    @Autowired
    private ReceptXMLRepozitorijum receptXMLRepozitorijum;

    @Autowired
    private UputXMLRepozitorijum uputXMLRepozitorijum;

    @Autowired
    private IzborPromenaXMLRepozitorijum izborPromenaXMLRepozitorijum;

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    public String transformisiRecept(Long id, String xmlPutanja, String xslPutanja, String htmlPutanja, String pdfPutanja) {
        String xml = receptXMLRepozitorijum.pretragaPoId("http://www.zis.rs/recepti/id" + id);
        try {
            File newFile = new File("src/main/resources/generated/recepti.xml");
            newFile.createNewFile();

            BufferedWriter bis = new BufferedWriter(new PrintWriter(newFile));
            bis.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"src/main/resources/xsl/recepti.xsl\"?>\n" +
                    "<recepti:recepti\n" +
                    " xmlns:recept=\"http://www.zis.rs/seme/recept\"\n" +
                    " xmlns:recepti=\"http://www.zis.rs/seme/recepti\"\n" +
                    ">\n");
            bis.append(xml);
            bis.append("\n</recepti:recepti>");
            bis.close();
            PDFTransformator.generateHTML(xmlPutanja, xslPutanja, htmlPutanja);
            PDFTransformator.generatePDF(pdfPutanja, htmlPutanja);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String transformisiUput(Long id, String xmlPutanja, String xslPutanja, String htmlPutanja, String pdfPutanja){
        String xml = uputXMLRepozitorijum.pretragaPoId("http://www.zis.rs/uputi/id" + id);
        try{
            File newFile = new File("src/main/resources/generated/uput.xml");
            newFile.createNewFile();
            BufferedWriter bis = new BufferedWriter(new PrintWriter(newFile));
            bis.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"src/main/resources/xsl/uputi.xsl\"?>\n" +
                    "<uputi:uputi\n" +
                    " xmlns:uput=\"http://www.zis.rs/seme/uput\"\n" +
                    " xmlns:uputi=\"http://www.zis.rs/seme/uputi\"\n" +
                    ">\n");
            bis.append(xml);
            bis.append("\n</uputi:uputi>");
            bis.close();
            PDFTransformator.generateHTML(xmlPutanja, xslPutanja, htmlPutanja);
            PDFTransformator.generatePDF(pdfPutanja, htmlPutanja);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return xml;
    }


    public String transformisiIzbor(Long id, String xmlPutanja, String xslPutanja, String htmlPutanja, String pdfPutanja) {
        String xml = izborPromenaXMLRepozitorijum.pretragaPoId("http://www.zis.rs/izbori/id" + id);
        try {
            File newFile = new File("src/main/resources/generated/izbori.xml");
            newFile.createNewFile();

            BufferedWriter bis = new BufferedWriter(new PrintWriter(newFile));
            bis.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"src/main/resources/xsl/izbori.xsl\"?>\n" +
                    "<izbori:izbori\n" +
                    " xmlns:izbor=\"http://www.zis.rs/seme/izbor\"\n" +
                    " xmlns:izbori=\"http://www.zis.rs/seme/izbori\"\n" +
                    ">\n");
            bis.append(xml);
            bis.append("\n</izbori:izbori>");
            bis.close();
            PDFTransformator.generateHTML(xmlPutanja, xslPutanja, htmlPutanja);
            PDFTransformator.generatePDF(pdfPutanja, htmlPutanja);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String transformisiIzvestaj(Long id, String xmlPutanja, String xslPutanja, String htmlPutanja, String pdfPutanja) {
        String xml = izvestajXMLRepozitorijum.pretragaPoId("http://www.zis.rs/izvestaji/id" + id);
        try {
            File newFile = new File("src/main/resources/generated/izvestaji.xml");
            newFile.createNewFile();

            BufferedWriter bis = new BufferedWriter(new PrintWriter(newFile));
            bis.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"src/main/resources/xsl/izvestaji.xsl\"?>\n" +
                    "<izvestaji:izvestaji\n" +
                    " xmlns:izvestaj=\"http://www.zis.rs/seme/izvestaj\"\n" +
                    " xmlns:izvestaji=\"http://www.zis.rs/seme/izvestaji\"\n" +
                    ">\n");
            bis.append(xml);
            bis.append("\n</izvestaji:izvestaji>");
            bis.close();
            PDFTransformator.generateHTML(xmlPutanja, xslPutanja, htmlPutanja);
            PDFTransformator.generatePDF(pdfPutanja, htmlPutanja);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return xml;
    }


}
