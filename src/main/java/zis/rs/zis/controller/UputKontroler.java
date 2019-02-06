package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.service.nonProcessService.UputServis;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/uputi")
public class UputKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(UputKontroler.class);

    @Autowired
    private UputServis uputServis;

    @Autowired
    private Validator validator;

    /**
     * GET /uputi
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
     * POST /uputi
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obrisi(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            logger.info("Vrsi se brisanje uputa {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(uputServis.obrisi(akcija), HttpStatus.OK);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            logger.info("Vrsi se izmena uputa {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(uputServis.izmeni(akcija), HttpStatus.OK);
        }
        throw new ValidacioniIzuzetak("Pogresno prosledjena akcija");
    }

}
