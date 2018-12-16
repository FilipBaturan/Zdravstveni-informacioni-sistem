package zis.rs.zis.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;
import zis.rs.zis.domain.ObjectFactory;
import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.entities.collections.Korisnici;
import zis.rs.zis.service.definition.KorisnikServis;
import zis.rs.zis.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Service
public class KorisnikServisImpl implements KorisnikServis {

    @Autowired
    private XMLDBKonekcija konekcija;

    @Override
    public Korisnici dobaviSve() {
        return null;
    }

    @Override
    public Korisnik pretragaPoId(String id) {
        try {
            ResursiBaze resursi = konekcija.uspostaviKonekciju("/db/rs/zis/korisnici",
                    "korisnici.xml");
            JAXBContext context = JAXBContext.newInstance("zis.rs.zis.domain",
                    ObjectFactory.class.getClassLoader());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Korisnici korisnici = (Korisnici) unmarshaller.unmarshal(resursi.getXmlResurs().getContentAsDOM());

            for (Korisnik k : korisnici.getKorisnik()) {
                if (k.getId().equals(id)) {
                    konekcija.oslobodiResurse(resursi);
                    if (k.isAktivan()) {
                        return k;
                    } else {
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            throw new KonekcijaSBazomIzuzetak("Onemogucen pristup bazi!");
        } catch (JAXBException e) {
            throw new TransformacioniIzuzetak("Onemogucena obrada podataka!");
        }
        throw new ValidacioniIzuzetak("Trazeni korisnik ne postoji u bazi!");
    }

    @Override
    public Korisnik sacuvaj(Korisnik korisnik) {
        return null;
    }

    @Override
    public void obrisi(String id) {

    }
}
