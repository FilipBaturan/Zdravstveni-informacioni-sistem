package zis.rs.zis.service.definition;

import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.entities.collections.Korisnici;

public interface KorisnikServis {

    /**
     * @return dobavlja sve korisnike iz baze
     */
    Korisnici dobaviSve();

    /**
     * @param id trazenog korisnika
     * @return pronadjen korisnik sa trazenim id-jem
     */
    Korisnik pretragaPoId(String id);

    /**
     * @param korisnik koji treba sacuvati u bazu
     * @return sacuvani lek iz baze
     */
    Korisnik sacuvaj(Korisnik korisnik);

    /**
     * @param id korisnika koji treba obrisati iz baze
     */
    void obrisi(String id);
}
