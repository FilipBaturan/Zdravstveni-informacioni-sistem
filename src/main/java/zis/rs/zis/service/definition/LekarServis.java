package zis.rs.zis.service.definition;

import zis.rs.zis.domain.entities.Lekar;

public interface LekarServis {

    /**
     * @return dobavlja sve lekare iz baze
     */
    String dobaviSve();

    /**
     * @param id trazenog lekara
     * @return pronadjen lekar sa trezenim id-jem
     */
    String pretragaPoId(String id);

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
