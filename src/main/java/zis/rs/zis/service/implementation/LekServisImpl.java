package zis.rs.zis.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;
import zis.rs.zis.domain.ObjectFactory;
import zis.rs.zis.domain.entities.Lek;
import zis.rs.zis.domain.entities.collections.Lekovi;
import zis.rs.zis.service.definition.LekServis;
import zis.rs.zis.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Service
public class LekServisImpl implements LekServis {

    @Autowired
    private XMLDBKonekcija konekcija;


    @Override
    public Lekovi dobaviSve() {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekovi", "lekovi.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Lekovi lekovi = (Lekovi) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            konekcija.oslobodiResurse(resursi);
            return lekovi;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
    }

    @Override
    public Lek pretragaPoId(String id) {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/lekovi", "lekovi.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Lekovi lekovi = (Lekovi) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            for (Lek l : lekovi.getLek()) {
                if (l.getId().equals(id)) {
                    konekcija.oslobodiResurse(resursi);
                    return l;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        throw new ValidacioniIzuzetak("Trazeni lek ne postoji u bazi!");
    }


    @Override
    public Lek sacuvaj(Lek lek) {
        return null;
    }

    @Override
    public void obrisi(String id) {

    }
}
