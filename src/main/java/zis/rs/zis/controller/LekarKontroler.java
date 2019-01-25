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
import zis.rs.zis.service.definition.LekarServis;

import java.util.Calendar;

@RestController
@RequestMapping("/lekari")
public class LekarKontroler {

    private static final Logger logger = LoggerFactory.getLogger(LekarKontroler.class);

    private static final String URI_PREFIX = "http://www.zis.rs/lekari/";

    @Autowired
    private LekarServis lekarServis;

    /**
     * GET /rs/zis/lekori
     *
     * @return sve lekare iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi lekari: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekarServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /lekari/{id}
     *
     * @param id trazenog lekara
     * @return lekar sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se lekar sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekarServis.pretragaPoId(URI_PREFIX + id), HttpStatus.OK);
    }
}
