package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.service.nonProcessService.IzborPromenaServis;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/izbori")
public class IzborPromenaKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(IzborPromenaKontroler.class);

    @Autowired
    private IzborPromenaServis izborPromenaServis;

    /**
     * GET /izbori
     *
     * @return sve izbore iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi izbori lekara: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /izbori/{id}
     *
     * @param id trazenog izboraLekara
     * @return izbor lekara sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Trazi se izbor lekara sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(izborPromenaServis.pretragaPoId(maper.dobaviURI("izbor") + id), HttpStatus.OK);
    }

    /**
     * POST /izbori
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            logger.info("Vrsi se brisanje izboraLekara {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(izborPromenaServis.obrisi(akcija), HttpStatus.OK);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            logger.info("Vrsi se izmena izboraLekara {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(izborPromenaServis.izmeni(akcija), HttpStatus.OK);
        } else {
            logger.info("Vrsi se dodavanje izbora lekara {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(izborPromenaServis.sacuvaj(akcija), HttpStatus.OK);
        }

    }
}

