package zis.rs.zis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zis.rs.zis.service.nonProcessService.TransformacijeServis;

@RestController
@RequestMapping("/transformacije")
public class TransformacijeKontroler {

    @Autowired
    private TransformacijeServis transformacijeServis;

    @GetMapping("/recept/{id}")
    public ResponseEntity<String> transformisiRecept(@PathVariable Long id) {
        String xmlPutanja = "src/main/resources/generated/recepti.xml";
        String xslPutanja = "src/main/resources/xsl/recepti.xsl";
        String htmlPutanja = "src/main/resources/static/recepti" + id.toString() + ".html";
        String pdfPutanja = "src/main/resources/static/recepti" + id.toString() + ".pdf";
        String xml = transformacijeServis.transformisiRecept(id, xmlPutanja, xslPutanja, htmlPutanja, pdfPutanja);
        return new ResponseEntity<String>(xml, HttpStatus.OK);
    }

    @GetMapping("/uput/{id}")
    public ResponseEntity<String> transformisiUput(@PathVariable Long id) {
        String xmlPutanja = "src/main/resources/generated/uput.xml";
        String xslPutanja = "src/main/resources/xsl/uputi.xsl";
        String htmlPutanja = "src/main/resources/static/uput" + id.toString() + ".html";
        String pdfPutanja = "src/main/resources/static/uput" + id.toString() + ".pdf";
        String xml = transformacijeServis.transformisiUput(id, xmlPutanja, xslPutanja, htmlPutanja, pdfPutanja);
        return new ResponseEntity<String>(xml, HttpStatus.OK);
    }

    @GetMapping("/izbor/{id}")
    public ResponseEntity<String> transformisiIzbor(@PathVariable Long id) {
        String xmlPutanja = "src/main/resources/generated/izbori.xml";
        String xslPutanja = "src/main/resources/xsl/izbor.xsl";
        String htmlPutanja = "src/main/resources/static/izbor"+id.toString()+".html";
        String pdfPutanja = "src/main/resources/static/izbor"+id.toString()+"pdf";
        String xml = transformacijeServis.transformisiIzbor(id, xmlPutanja, xslPutanja, htmlPutanja, pdfPutanja);
        return new ResponseEntity<String>(xml, HttpStatus.OK);
    }


}
