package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.IzvestajXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class SpecijalistickiPregled extends Stanje {

    @Autowired
    private IzvestajXMLRepozitorijum izvestajXMLRepozitorijum;

    @Autowired
    private SpecijalistickiPregled specijalistickiPregled;

    @Override
    public String obradiZahtev(Akcija akcija) {
        return specijalistickiPregled.kreiranjeIzvestaja(akcija);
    }

    /**
     * Kreira izvestaj
     *
     * @param akcija koju treba izvrsiti
     * @return rezultat akcije
     */
    public String kreiranjeIzvestaja(Akcija akcija) {
        return izvestajXMLRepozitorijum.sacuvaj(akcija);
    }

}
