package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.DTO.Prijava;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.service.nonProcessService.KorisnikServis;
import zis.rs.zis.util.akcije.Akcija;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * API za korisnike
 */
@RestController
@RequestMapping("/korisnici")
public class KorisnikKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(KorisnikKontroler.class);

    @Autowired
    private KorisnikServis korisnikServis;


    /**
     * POST korisnici/registracija
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(path = "registracija", consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registracija(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        logger.info("Vrsi se registracija korisnika {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(korisnikServis.registruj(akcija), HttpStatus.OK);
    }

    /**
     * POST /korisnici
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        logger.info("Vrsi se azuriranje korisnika {}.", Calendar.getInstance().getTime());
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            return new ResponseEntity<>(korisnikServis.obrisi(akcija.getKontekst()), HttpStatus.OK);
        } else {
            return null;
        }
    }

    /**
     * POST /korisnici/prijava
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(path = "/prijava", consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> prijava(@RequestBody Akcija akcija, HttpSession sesija) {
        this.validirajAkciju(akcija);
        logger.info("Vrsi se prijavljivanje korisnika {}.", Calendar.getInstance().getTime());
        Prijava prijava = korisnikServis.prijava(akcija);
        sesija.setAttribute("id", prijava.getId());
        sesija.setAttribute("tip", prijava.getTip());
        return new ResponseEntity<>("Uspesna prijava na sistem!", HttpStatus.OK);
    }

    /**
     * GET /korisnici/odjava
     *
     * @return rezultat akcije
     */
    @GetMapping(path = "/odjava", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> odjava(HttpSession sesija) {
        logger.info("Vrsi se odjavljivanje {}.", Calendar.getInstance().getTime());
        sesija.invalidate();
        return new ResponseEntity<>("Uspesna odjava sa sistem!", HttpStatus.OK);
    }

}
