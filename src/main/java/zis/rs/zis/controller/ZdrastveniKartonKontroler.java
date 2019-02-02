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
import zis.rs.zis.service.implementation.ZdravstveniKartonServis;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/kartoni")
public class ZdrastveniKartonKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(LekarKontroler.class);

    @Autowired
    private ZdravstveniKartonServis zdravstveniKartonServis;

    /**
     * POST /korisnici
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se azuriranje zdravstvenog kartona {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())) {
            return new ResponseEntity<>(zdravstveniKartonServis.izmena(akcija), HttpStatus.OK);
        }
        throw new ValidacioniIzuzetak("Pogresno prosledjena akcija!");
    }
}
