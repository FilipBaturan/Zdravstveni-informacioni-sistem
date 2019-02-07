package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;
import zis.rs.zis.service.nonProcessService.PregledServis;

import java.util.Calendar;


@RestController
@RequestMapping("/pregledi")
public class PregledKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(LekarKontroler.class);

    @Autowired
    private PregledServis pregledServis;

    /**
     * GET /pregledi/{id}
     *
     * @param id trazenog pregleda
     * @return pregled sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se pregled sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(pregledServis.pretragaPoId(maper.dobaviURI("pregled") + id),
                HttpStatus.OK);
    }
}
