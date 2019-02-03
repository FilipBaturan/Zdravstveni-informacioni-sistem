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
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.util.Validator;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

/**
 * API za korisnike
 */
@RestController
@RequestMapping("/korisnici")
public class KorisnikKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(KorisnikKontroler.class);

    @Autowired
    private Validator validator;

    @Autowired
    private KorisnikXMLRepozitorijum korisnikRepozertorijum;


    /**
     * POST korisnici/registracija
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(path = "registracija", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> registracija(@RequestBody Akcija akcija) {
        logger.info("Vrsi se registracija korisnika {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        // ODRADI CUVANJE U FUSEKI BAZU
        return new ResponseEntity<>(korisnikRepozertorijum.registruj(akcija)[0], HttpStatus.OK);
    }

    /**
     * POST /korisnici
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se azuriranje korisnika {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            return new ResponseEntity<>(korisnikRepozertorijum.obrisi(akcija.getKontekst()), HttpStatus.OK);
        } else {
            return null;
        }
    }

}
