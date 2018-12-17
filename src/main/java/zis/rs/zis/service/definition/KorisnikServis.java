package zis.rs.zis.service.definition;

import zis.rs.zis.domain.entities.Korisnik;

public interface KorisnikServis {

    /**
     * @return dobavlja sve korisnike iz baze
     */
    String dobaviSve();

    /**
     * @param id trazenog korisnika
     * @return pronadjen korisnik sa trazenim id-jem
     */
    String pretragaPoId(String id);

    /**
     * @param korisnik koji treba sacuvati u bazu
     * @return sacuvani lek iz baze
     */
    String sacuvaj(Korisnik korisnik);

    /**
     * @param id korisnika koji treba obrisati iz baze
     */
    void obrisi(String id);
}
