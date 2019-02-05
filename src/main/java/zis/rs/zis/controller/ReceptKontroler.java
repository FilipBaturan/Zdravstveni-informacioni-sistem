package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.service.nonProcessService.ReceptServis;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/recepti")
public class ReceptKontroler {

    private static final Logger logger = LoggerFactory.getLogger(ReceptKontroler.class);

    private static final String URI_PREFIX = "/recepti";

    @Autowired
    private ReceptServis receptServis;

    @Autowired
    private Validator validator;

    @Autowired
    private Maper maper;

    /**
     * GET /rs/recepti
     *
     * @return sve recepte iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi recepti: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(receptServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /recepti/{id}
     *
     * @param id trazenog recepta
     * @return recept sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Trazi se recept sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(receptServis.pretragaPoId(maper.dobaviURI("recept") + id), HttpStatus.OK);
    }

    @PostMapping(value = "/obrisi", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        logger.info("Vrsi se brisanje recepta {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(receptServis.obrisi(akcija), HttpStatus.OK);
    }

    @PostMapping(value = "/izmeni", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izmeni(@RequestBody Akcija akcija) {
        logger.info("Vrsi se izmena recepta {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(receptServis.izmeni(akcija), HttpStatus.OK);
    }

}
