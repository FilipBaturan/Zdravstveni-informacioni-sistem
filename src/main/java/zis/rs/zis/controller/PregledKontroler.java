package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;
import zis.rs.zis.service.states.Proces;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

import java.util.Calendar;


@RestController
@RequestMapping("/pregledi")
public class PregledKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(LekarKontroler.class);

    @Autowired
    private Maper maper;

    @Autowired
    private PregledXMLRepozitorijum pregledXMLRepozitorijum;

    /**
     * GET /pregledi/{id}
     *
     * @param id trazenog pregleda
     * @return pregled sa trazenim id-jem
     */
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se pregled sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(pregledXMLRepozitorijum.pretragaPoId(maper.dobaviURI("pregled") + id),
                HttpStatus.OK);
    }

//    /**
//     * POST /pregledi
//     *
//     * @param akcija koja se izvrsava
//     * @return rezultat akcije
//     */
//    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> sacuvaj(@RequestBody Akcija akcija) {
//        logger.info("Vrsi se azuriranje pregleda {}.", Calendar.getInstance().getTime());
//        this.validirajAkciju(akcija);
//        return new ResponseEntity<>(proces.obradiZahtev(akcija), HttpStatus.OK);
//    }





}
