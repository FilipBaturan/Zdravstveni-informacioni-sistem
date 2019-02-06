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
        String htmlPutanja = "src/main/resources/generated/recepti.html";
        String pdfPutanja = "src/main/resources/generated/recepti.pdf";
        String xml = transformacijeServis.transformisiRecept(id, xmlPutanja, xslPutanja, htmlPutanja, pdfPutanja);
        return new ResponseEntity<String>(xml, HttpStatus.OK);
    }
}
