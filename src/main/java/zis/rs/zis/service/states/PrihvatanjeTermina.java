package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.Opcije;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.repository.xml.PregledXMLRepozitorijum;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class PrihvatanjeTermina extends Stanje {

    @Autowired
    private PrihvatanjeTermina prihvatanjeTermina;

    @Autowired
    private PregledXMLRepozitorijum pregledXMLRepozitorijum;

    @Autowired
    private KorisnikXMLRepozitorijum korisnikXMLRepozitorijum;

    @Autowired
    private Operacije operacije;

    @Autowired
    private Maper maper;

    @Override
    public String obradiZahtev(Akcija akcija) {
        if (this.getOpcija() == Opcije.IZMENA_PREGLEDA) {
            return prihvatanjeTermina.izmenaTermina(akcija);
        } else if (this.getOpcija() == Opcije.ODBIJANJE_PREGLEDA) {
            return prihvatanjeTermina.obradaTermina(akcija, "Odbijen termin.");
        } else {
            return prihvatanjeTermina.obradaTermina(akcija, "Prihvacen termin.");
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return rezultat akcije
     */
    public String izmenaTermina(Akcija akcija) {
        pregledXMLRepozitorijum.izmeni(akcija);
        this.obradaTermina(akcija, "Izmenjen termin.");
        return "Uspesno izmenjen termin!";
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @param poruka koja se prosledjuje kao obavestenje
     * @return rezultat akcije
     */
    public String obradaTermina(Akcija akcija, String poruka) {
        String pacijent = operacije.pretragaPoId(maper.dobaviPacijentaIzPregleda(akcija),
                "zdravstveni_kartoni", "dobavljanjePacijentaPrekoKartona");
        korisnikXMLRepozitorijum.dodajObavestenje(pacijent, poruka);
        return poruka;
    }
}
