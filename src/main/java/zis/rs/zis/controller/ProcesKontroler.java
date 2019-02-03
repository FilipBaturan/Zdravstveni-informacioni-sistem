package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zis.rs.zis.service.states.Proces;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/proces")
public class ProcesKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(ProcesKontroler.class);

    @Autowired
    private Maper maper;

    @Autowired
    private Proces proces;

    /**
     * POST /proces
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> proces(@RequestBody Akcija akcija) {
        logger.info("Obradjuje se proces {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        return new ResponseEntity<>(proces.obradiZahtev(akcija), HttpStatus.OK);
    }
}
