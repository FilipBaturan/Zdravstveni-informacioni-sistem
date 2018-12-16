package zis.rs.zis.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zis.rs.zis.domain.entities.Lek;
import zis.rs.zis.domain.entities.collections.Lekovi;
import zis.rs.zis.service.definition.LekServis;

import java.util.Calendar;

/**
 * API za lekove
 */
@RestController
@RequestMapping("/rs/zis/lekovi")
public class LekKontroler {

    private static final Logger logger = LoggerFactory.getLogger(LekKontroler.class);

    private static final String URI_PREFIX = "rs/zis/lekovi";

    @Autowired
    private LekServis lekServis;

    /**
     * GET /rs/zis/lekovi
     *
     * @return sve lekove iz baze
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Lekovi> dobaviSve() {
        logger.info("Traze se svi lekovi: {}.", Calendar.getInstance().getTime());

        return new ResponseEntity<>(lekServis.dobaviSve(), HttpStatus.OK);
    }

    /**
     * GET /rs/zis/lekovi/id{id}
     *
     * @param id trazenog leka
     * @return lek sa trazenim id-jem
     */
    @GetMapping(path = "id{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Lek> pretragaPoId(@PathVariable String id) {
        logger.info("Traze se lek sa id={}: {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(lekServis.pretragaPoId(URI_PREFIX + "/id" + id), HttpStatus.OK);
    }

}
