package zis.rs.zis.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zis.rs.zis.domain.DTO.RezultatPretrage;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.service.nonProcessService.PretrageServis;
import zis.rs.zis.util.akcije.Akcija;

@RestController
@RequestMapping("/pretrage")
public class PretrageKontroler extends ValidatorKontoler {

    @Autowired
    private PretrageServis pretrageServis;


    @PostMapping(path = "/opstiUpit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RezultatPretrage> pretragaPoId(@RequestBody UpitPretrage upit) {
        return new ResponseEntity<>(new RezultatPretrage(pretrageServis.opstiUpit(upit)), HttpStatus.OK);
    }

    @GetMapping(path = "/json/{dokument}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RezultatPretrage> izvozMetaPodatakaJson(@PathVariable String dokument) {
        return new ResponseEntity<>(new RezultatPretrage(pretrageServis.izveziMetapodatke(dokument, "json")), HttpStatus.OK);
    }

    @GetMapping(path = "/rdf/{dokument}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> izvozMetaPodatakaRDF(@PathVariable String dokument) {
        return new ResponseEntity<>(pretrageServis.izveziMetapodatke(dokument, "rdf"), HttpStatus.OK);
    }

    @PostMapping(path = "/linkovi", consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RezultatPretrage> linkoviNaDokument(@RequestBody Akcija akcija) {
        this.validirajAkciju(akcija);
        return new ResponseEntity<>(new RezultatPretrage(pretrageServis.linkoviNaDokument(akcija.getKontekst())),
                HttpStatus.OK);
    }
}
