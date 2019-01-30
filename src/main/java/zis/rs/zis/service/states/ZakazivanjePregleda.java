package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class ZakazivanjePregleda extends Stanje {

    @Autowired
    private ZakazivanjePregleda zakazivanjePregleda;

    @Autowired
    private PregledXMLRepozitorijum pregledXMLRepozitorijum;

    @Override
    public String obradiZahtev(Akcija akcija) {

        return zakazivanjePregleda.kreirajPregled(akcija);
    }

    /**
     * Kreira novi kod termim kod lekara opste prakse
     * ili kod lekara specijaliste
     *
     * @param akcija koju treba izvrsiti
     * @return poruka o rezultatu akcije
     */
    public String kreirajPregled(Akcija akcija) {
        return "temp";
        //return pregledXMLRepozitorijum.sacuvaj(akcija);
    }
}
