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
import zis.rs.zis.service.nonProcessService.IzvestajServis;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;

@RestController
@RequestMapping("/izvestaji")
public class IzvestajKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(IzvestajKontroler.class);

    @Autowired
    private IzvestajServis izvestajServis;

    /**
     * POST /izvestaji
     *
     * @param akcija koja se izvrsava
     * @return rezultat akcije
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
        logger.info("Vrsi se azuriranje pregleda {}.", Calendar.getInstance().getTime());
        this.validirajAkciju(akcija);
        if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString())){
            return new ResponseEntity<>(izvestajServis.izmeni(akcija), HttpStatus.OK);
        } else if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            return new ResponseEntity<>(izvestajServis.obrisi(akcija), HttpStatus.OK);
        }
        throw new ValidacioniIzuzetak("Nevalidno prosledjena akcija");
    }
}
