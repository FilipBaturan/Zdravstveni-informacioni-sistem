package zis.rs.zis.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.service.nonProcessService.LekServis;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

/**
 * API za lekove
 */
@RestController
@RequestMapping("/lekovi")
public class LekKontroler {

    private static final Logger logger = LoggerFactory.getLogger(LekKontroler.class);

    private static final String URI_PREFIX = "/lekovi";

    @Autowired
    private LekServis lekServis;

    @Autowired
    private Validator validator;

    @Autowired
    private Maper maper;

    /**
     * GET /rs/zis/lekovi
     *
     * @return sve lekove iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi lekovi: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /lekovi/{id}
     *
     * @param id trazenog leka
     * @return lek sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se lek sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.pretragaPoId(maper.dobaviURI("lek") + id), HttpStatus.OK);
    }

    /**
     * POST lekovi
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se dodavanje leka {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.sacuvaj(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/obrisi", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        logger.info("Vrsi se brisanje leka {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.obrisi(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/izmeni", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izmeni(@RequestBody Akcija akcija) {
        logger.info("Vrsi se izmena leka {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.izmeni(akcija), HttpStatus.OK);
    }


}
