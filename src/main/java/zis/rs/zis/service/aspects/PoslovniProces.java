package zis.rs.zis.service.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XUpdateQueryService;
import zis.rs.zis.domain.enums.TipAkcije;
import zis.rs.zis.service.states.Proces;
import zis.rs.zis.util.*;
import zis.rs.zis.util.akcije.Akcija;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;

@Aspect
@Configuration
public class PoslovniProces extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private Proces proces;

    private static final Logger logger = LoggerFactory.getLogger(PoslovniProces.class);


//    @AfterThrowing(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))",
//            throwing = "error")
//    public void afterThrowingAdvice(JoinPoint jp, Throwable error){
//        System.out.println("Method Signature: "  + jp.getSignature());
//        System.out.println("Exception: "+error);
//    }

//    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))",
//            returning = "retVal")
//    public void afterReturningAdvice(JoinPoint jp, Object retVal){
//        logger.info("Method Signature: "  + jp.getSignature());
//        logger.info("Returning:" + retVal.toString());
//    }


    @Before("execution(* zis.rs.zis.service.states.PrihvatanjeTermina.obradiZahtev(..)) && args(akcija,..)")
    public void prePrihvatanjaTermina(Akcija akcija) {
        logger.info("Pre prihvatanja terimna");
        proces.getPrihvatanjeTermina().setOpcija(TipAkcije.IZMENA_PREGLEDA);
    }

    @Before("execution(* zis.rs.zis.service.states.IzmenjenTermin.obradiZahtev(..)) && args(akcija,..)")
    public void prePrihvatanjaIzmenjenogTermina(Akcija akcija) {
        logger.info("Pre prihvatanja izmenjenog terimna");
        proces.getPrihvatanjeTermina().setOpcija(TipAkcije.IZMENA_PREGLEDA);
    }

    /**
     * Dodaje novog pacijenta u poslovni proces i stavalja ga u stanje cekanje
     *
     * @param akcija koju je potrebno procesirati ciji sadrzaj
     *               predstavlja pregled koji se zakazuje
     */
    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..)) && args(akcija,..)")
    public void nakonKreiranjaPregleda(Akcija akcija) {
        ResursiBaze resursi = null;
        try {
            resursi = konekcija.uspostaviKonekciju(maper.dobaviKolekciju(),
                    maper.dobaviDokument("stanja_pregleda"));
            String putanjaDoUpita = ResourceUtils.getFile(maper.dobaviUpit("dodavanje")).getPath();

            XUpdateQueryService xupdateService = (XUpdateQueryService) resursi.getKolekcija()
                    .getService("XUpdateQueryService", "1.0");
            xupdateService.setProperty("indent", "yes");
            String sadrzajUpita = String.format(this.ucitajSadrzajFajla(putanjaDoUpita),
                    "stp", maper.dobaviPrefiks("stanje_pregleda"),
                    maper.dobaviPutanju("stanja_pregleda"),
                    this.konverturjUString(akcija), maper.dobaviPrefiks("stanja_pregleda"));
            logger.info(sadrzajUpita);
            long mods = xupdateService.updateResource(maper.dobaviDokument("stanja_pregleda"), sadrzajUpita);
            logger.info(mods + " izmene procesirane.");

            konekcija.oslobodiResurse(resursi);
            if (mods == 0) {
                throw new KonekcijaSaBazomIzuzetak("Greska prilikom snimanja podataka");
            }
            proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getPrihvatanjeTermina());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.izmenaTermina(..)) && args(akcija,..)")
    public void nakonIzmeneTermina(Akcija akcija) {

    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.odbijanjeTermina(..)) && args(akcija,..)")
    public void nakonOdbijanjaTermina(Akcija akcija) {

    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.prihvatanjeTermina(..)) && args(akcija,..)")
    public void nakonPrihvatanjaTermina(Akcija akcija) {

    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return string reprezentaciju nove stavke poslovnog procesa
     */
    private String konverturjUString(Akcija akcija) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            Document dok = dbf.newDocumentBuilder().newDocument();

            Element pregled = dok.createElementNS(maper.dobaviPrefiks("stanje_pregleda"), "stanje_pregleda");
            pregled.setPrefix("stp");
            pregled.setAttribute("pacijent", maper.dobaviPacijentaIzPregleda(akcija));
            pregled.setAttribute("stanje", "cekanje");
            pregled.setAttribute("datum", LocalDateTime.now().toString());
            dok.appendChild(pregled);
            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Greska pri obradi podataka!");
        }
    }


}
