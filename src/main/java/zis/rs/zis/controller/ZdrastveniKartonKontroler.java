package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.DTO.RezultatPretrage;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.domain.enums.TipKorisnika;
import zis.rs.zis.service.nonProcessService.ZdravstveniKartonServis;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

@RestController
@RequestMapping("/kartoni")
public class ZdrastveniKartonKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(LekarKontroler.class);

    @Autowired
    private ZdravstveniKartonServis zdravstveniKartonServis;


    /**
     * GET /kartoni
     *
     * @return sve izbore iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobaviSve() {
        logger.info("Traze se svi izbori karonga: {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(zdravstveniKartonServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /kartoni/{id}
     *
     * @param id trazenog lekara
     * @return karton sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se zdravstveni karton sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(zdravstveniKartonServis.
                pretragaPoId(maper.dobaviURI("zdravstveni_karton") + id), HttpStatus.OK);
    }

    /**
     * GET /kartoni/dokumenti/{id}
     *
     * @param id trazenog lekara
     * @return karton sa trazenim id-jem
     */
    @GetMapping(path = "/dokumenti/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> dobavljenjeDokumenata(@PathVariable String id) {
        logger.info("Traze se zdravstveni karton sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(zdravstveniKartonServis.
                dobaviDokumente(maper.dobaviURI("zdravstveni_karton") + id), HttpStatus.OK);
    }

    /**
     * GET /kartoni/pretraga/{tekst}
     *
     * @param tekst koji karton treba da sadrzi
     * @return karton sa trazenim tekstom
     */
    @GetMapping(path = "pretraga/{tekst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RezultatPretrage> opstaPretraga(@PathVariable String tekst, HttpSession sesija) {
        logger.info("Traze se zdravstveni karton sa tekstom ={}: {}.", tekst, Calendar.getInstance().getTime());
        try {
        if (sesija.getAttribute("tip").equals(TipKorisnika.PACIJENT.toString())) {
            return new ResponseEntity<>(new RezultatPretrage(zdravstveniKartonServis.
                    opstaPretragaPacijenta(tekst)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RezultatPretrage(zdravstveniKartonServis.
                    opstaPretragaLekara(tekst)), HttpStatus.OK);
        }} catch (NullPointerException e) {
            throw new ValidacioniIzuzetak("Korisnik nije ulogovan.");
        }

    }

    /**
     * POST /kartoni
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        logger.info("Vrsi se azuriranje zdravstvenog kartona {}.", Calendar.getInstance().getTime());
        if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            return new ResponseEntity<>(zdravstveniKartonServis.izmena(akcija), HttpStatus.OK);
        }
        throw new ValidacioniIzuzetak("Pogresno prosledjena akcija!");
    }
}
