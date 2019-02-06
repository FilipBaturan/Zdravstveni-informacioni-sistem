package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.service.nonProcessService.ReceptServis;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/recepti")
public class ReceptKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(ReceptKontroler.class);

    @Autowired
    private ReceptServis receptServis;

    @Autowired
    private Validator validator;

    /**
     * GET /recepti
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

    /**
     * POST /recepti
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            logger.info("Vrsi se brisanje recepta {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(receptServis.obrisi(akcija), HttpStatus.OK);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            logger.info("Vrsi se izmena recepta {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(receptServis.izmeni(akcija), HttpStatus.OK);
        }
        throw new ValidacioniIzuzetak("Pogresno prosledjena akcija");
    }
}
