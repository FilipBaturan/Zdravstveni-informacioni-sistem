package zis.rs.zis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/izvestaji")
public class IzvestajKontroler extends ValidatorKontoler {

    private static final Logger logger = LoggerFactory.getLogger(IzvestajKontroler.class);
    // endpoint za kreiranjen novog izvestaja, a opciono i recepta


}
