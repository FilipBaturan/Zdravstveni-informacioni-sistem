package zis.rs.zis.service.definition;

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
}
