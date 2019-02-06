package zis.rs.zis.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.DTO;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.service.nonProcessService.PretrageServis;

@RestController
@RequestMapping("/pretrage")
public class PretrageKontroler {

    @Autowired
    private PretrageServis pretrageServis;


    @PostMapping(path = "/opstiUpit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DTO> pretragaPoId(@RequestBody UpitPretrage upit) {
        return new ResponseEntity<>(new DTO(pretrageServis.opstiUpit(upit)), HttpStatus.OK);
    }

    @GetMapping(path = "/json/{dokument}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DTO> izvozMetaPodatakaJson(@PathVariable String dokument) {
        return new ResponseEntity<>(new DTO(pretrageServis.izveziMetapodatke(dokument, "json")), HttpStatus.OK);
    }

    @GetMapping(path = "/rdf/{dokument}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izvozMetaPodatakaRDF(@PathVariable String dokument) {
        return new ResponseEntity<>(pretrageServis.izveziMetapodatke(dokument, "rdf"), HttpStatus.OK);
    }

}
