package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.Opcije;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class PrihvatanjeTermina extends Stanje {

    @Autowired
    private PrihvatanjeTermina prihvatanjeTermina;

    @Autowired
    private PregledXMLRepozitorijum pregledXMLRepozitorijum;

    @Override
    public String obradiZahtev(Akcija akcija) {
        if (this.getOpcija() == Opcije.IZMENA_PREGLEDA) {
            return prihvatanjeTermina.izmenaTermina(akcija);
        } else if (this.getOpcija() == Opcije.ODBIJANJE_PREGLEDA) {
            return prihvatanjeTermina.odbijanjeTermina(akcija);
        } else {
            return prihvatanjeTermina.prihvatanjeTermina(akcija);
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String izmenaTermina(Akcija akcija) {
        return pregledXMLRepozitorijum.izmeni(akcija);
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String odbijanjeTermina(Akcija akcija) {
        return pregledXMLRepozitorijum.obrisi(akcija);
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String prihvatanjeTermina(Akcija akcija) {
        return "Prihvacen pregled!";
    }
}
