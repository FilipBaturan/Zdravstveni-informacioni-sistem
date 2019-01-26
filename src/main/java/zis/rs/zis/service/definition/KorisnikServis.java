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
     * @param akcija koju treba izvrsiti priliko snimanja
     * @return sacuvani lek iz baze
     */
    String sacuvaj(Akcija akcija);

    /**
     * @param akcija koju treba izvrsiti prilikom registacije
     * @return registrovani korisnik
     */
    String[] registruj(Akcija akcija);

    /**
     * @param id korisnika kojeg treba obrisati
     */
    String obrisi(String id);
}
