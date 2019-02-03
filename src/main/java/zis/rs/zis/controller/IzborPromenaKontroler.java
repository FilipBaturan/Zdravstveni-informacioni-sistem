package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.service.nonProcessService.IzborPromenaServis;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/izborLekara")
public class IzborPromenaKontroler {

    private static final Logger logger = LoggerFactory.getLogger(zis.rs.zis.controller.IzborPromenaKontroler.class);

    @Autowired
    private IzborPromenaServis izborPromenaServis;

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
        logger.info("Traze se svi izboriLekara: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /izboriLekara/{id}
     *
     * @param id trazenog izboraLekara
     * @return izborLekara sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Trazi se izborLekara sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.pretragaPoId(maper.dobaviURI("uput") + id), HttpStatus.OK);
    }

    /**
     * POST izboraLekara
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se dodavanje izboraLekara {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.sacuvaj(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/obrisi", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        logger.info("Vrsi se brisanje izboraLekara {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.obrisi(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/izmeni", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izmeni(@RequestBody Akcija akcija) {
        logger.info("Vrsi se izmena izboraLekara {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.izmeni(akcija), HttpStatus.OK);
    }

}


