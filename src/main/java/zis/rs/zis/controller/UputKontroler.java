package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.service.nonProcessService.UputServis;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/uputi")
public class UputKontroler {

    private static final Logger logger = LoggerFactory.getLogger(UputKontroler.class);

    private static final String URI_PREFIX = "/uputi";

    @Autowired
    private UputServis uputServis;

    @Autowired
    private Validator validator;

    @Autowired
    private Maper maper;

    /**
     * GET /rs/uputi
     *
     * @return sve upute iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi uputi: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(uputServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /uputi/{id}
     *
     * @param id trazenog uputa
     * @return uput sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Trazi se uput sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(uputServis.pretragaPoId(maper.dobaviURI("uput") + id), HttpStatus.OK);
    }

    /**
     * POST uput
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se dodavanje uputa {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(uputServis.sacuvaj(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/obrisi", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        logger.info("Vrsi se brisanje uputa {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(uputServis.obrisi(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/izmeni", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izmeni(@RequestBody Akcija akcija) {
        logger.info("Vrsi se izmena uputa {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(uputServis.izmeni(akcija), HttpStatus.OK);
    }

}
