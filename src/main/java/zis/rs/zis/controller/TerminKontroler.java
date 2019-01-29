package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zis.rs.zis.service.states.ZakazivanjePregleda;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/termini")
public class TerminKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(IzvestajKontroler.class);

    // endpoint za prihvatanje i odbijanje termina

    @Autowired
    private ZakazivanjePregleda zakazivanjePregleda;

    /**
     * POST pregledi
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se azuriranje pregleda {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        zakazivanjePregleda.kreirajPregled(akcija);
        return null;
    }
}
