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
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

/**
 * API za lekove
 */
@RestController
@RequestMapping("/rs/zis/korisnici")
public class KorisnikKontroler {

    private static final Logger logger = LoggerFactory.getLogger(KorisnikKontroler.class);

    @Autowired
    private Validator validator;

    /**
     * POST /rs/zis/autn
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se azuriranje leka {}.", Calendar.getInstance().getTime());
        return null;
//        return new ResponseEntity<>(lekServis.sacuvaj(validator.procesirajAkciju(akcija,
//                "classpath:static/zis/seme/lek.xsd")), HttpStatus.OK);
    }
}
