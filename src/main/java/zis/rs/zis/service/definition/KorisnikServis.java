package zis.rs.zis.service.definition;

import zis.rs.zis.util.akcije.Akcija;

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
    String sacuvaj(String korisnik);

    /**
     * @param akcija koji treba izvrsiti prilikom registacije
     * @return registrovani korisnik
     */
    String registruj(Akcija akcija);

    /**
     * @param id korisnika koji treba obrisati iz baze
     */
    void obrisi(String id);
}
