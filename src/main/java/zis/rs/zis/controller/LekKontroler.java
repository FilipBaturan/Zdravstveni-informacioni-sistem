package zis.rs.zis.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.service.nonProcessService.LekServis;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

/**
 * API za lekove
 */
@RestController
@RequestMapping("/lekovi")
public class LekKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(LekKontroler.class);

    @Autowired
    private LekServis lekServis;

    @Autowired
    private Validator validator;

    /**
     * GET /lekovi
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
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            logger.info("Vrsi se brisanje leka {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(lekServis.obrisi(akcija), HttpStatus.OK);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            logger.info("Vrsi se izmena leka {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(lekServis.izmeni(akcija), HttpStatus.OK);
        } else {
            logger.info("Vrsi se dodavanje leka {}.", Calendar.getInstance().getTime());
            return new ResponseEntity<>(lekServis.sacuvaj(akcija), HttpStatus.OK);
        }
    }

    /**
     * GET /lekovi/{dijagnoza}/{pacijentId}
     *
     * @param dijagnoza  pacijenta
     * @param pacijentId id pacijenta
     * @return lekevo za pogodni za datu dijagnozu
     */
    @GetMapping(value = "/{dijagnoza}/{pacijentId}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dijagnozaPacijenta(@PathVariable String dijagnoza, @PathVariable String pacijentId) {
        logger.info("Vrsi se dobavljanje leka po dijagnozi{}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.dobaviLekZaDijagnozu(dijagnoza,
                maper.dobaviURI("zdravstveni_karton") + pacijentId), HttpStatus.OK);
    }


}
