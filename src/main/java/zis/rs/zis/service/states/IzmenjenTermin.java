package zis.rs.zis.service.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zis.rs.zis.domain.enums.Opcije;
import zis.rs.zis.repository.xml.KorisnikXMLRepozitorijum;
import zis.rs.zis.util.CRUD.Operacije;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.akcije.Akcija;

@Service
public class IzmenjenTermin extends Stanje {

    @Autowired
    private IzmenjenTermin izmenjenTermin;

    @Autowired
    private KorisnikXMLRepozitorijum korisnikXMLRepozitorijum;

    @Autowired
    private Operacije operacije;

    @Autowired
    private Maper maper;

    @Override
    public String obradiZahtev(Akcija akcija) {
        if (this.getOpcija() == Opcije.ODBIJANJE_PREGLEDA) {
            return izmenjenTermin.obradaTermina(akcija, "Odbijen termin.");
        } else {
            return izmenjenTermin.obradaTermina(akcija, "Prihvacen termin.");
        }
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
