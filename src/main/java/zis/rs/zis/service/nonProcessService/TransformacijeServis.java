package zis.rs.zis.service.nonProcessService;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.ReceptXMLRepozitorijum;
import zis.rs.zis.util.transformatori.PDFTransformator;

import java.io.*;

@Service
public class TransformacijeServis {

    @Autowired
    private PDFTransformator pdfTransformator;

    @Autowired
    private ReceptXMLRepozitorijum receptXMLRepozitorijum;

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

}
