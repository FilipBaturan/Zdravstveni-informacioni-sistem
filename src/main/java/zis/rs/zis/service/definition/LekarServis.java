package zis.rs.zis.service.definition;

import zis.rs.zis.domain.entities.Lekar;
import zis.rs.zis.domain.entities.collections.Lekari;

public interface LekarServis {

    /**
     * @return dobavlja sve lekare iz baze
     */
    Lekari dobaviSve();

    /**
     * @return dobavlja sve lekare iz baze i sve ostale
     * entitete koji su u relaciji sa njim
     */
    String dobaviSveKaskadno();

    /**
     * @param id trazenog lekara
     * @return pronadjen lekar sa trazenim id-jem
     */
    Lekar pretragaPoId(String id);

    /**
     * @param id trazenog lekara
     * @return pronadjen lekar sa trezenim id-jem i sve
     * ostale entitete koji su u relaciji s njim
     */
    String pretragaPoIdKaskadno(String id);

    /**
     * @param lekar kojeg treba sacuvati u bazu
     * @return sacuvani lekar iz baze
     */
    String sacuvaj(Lekar lekar);

    /**
     * @param id lekara kojeg treba obrisati iz baze
     */
    void obrisi(String id);
}
