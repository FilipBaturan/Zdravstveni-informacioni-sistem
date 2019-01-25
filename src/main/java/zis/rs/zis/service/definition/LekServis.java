package zis.rs.zis.service.definition;

public interface LekServis {

    /**
     * @return dobavlja sve lekove iz baze
     */
    String dobaviSve();

    /**
     * @param id trazenog leka
     * @return pronadjen lek sa trazenim id-jem
     */
    String pretragaPoId(String id);

    /**
     * @param lek koji treba sacuvati u bazu
     * @return sacuvani lek iz baze
     */
    String sacuvaj(String lek);

    /**
     * @param id leka koji treba obrisati iz baze
     */
    void obrisi(String id);
}
