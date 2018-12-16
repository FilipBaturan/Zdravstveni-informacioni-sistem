package zis.rs.zis.service.definition;

import zis.rs.zis.domain.entities.Lek;
import zis.rs.zis.domain.entities.collections.Lekovi;

public interface LekServis {

    /**
     * @return dobavlja sve lekove iz baze
     */
    Lekovi dobaviSve();

    /**
     * @param id trazenog leka
     * @return pronadjen lek sa trazenim id-jem
     */
    Lek pretragaPoId(String id);

    /**
     * @param lek koji treba sacuvati u bazu
     * @return sacuvani lek iz baze
     */
    Lek sacuvaj(Lek lek);

    /**
     * @param id leka koji treba obrisati iz baze
     */
    void obrisi(String id);
}
