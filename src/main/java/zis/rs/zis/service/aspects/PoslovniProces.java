package zis.rs.zis.service.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XUpdateQueryService;
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

    private static final Logger logger = LoggerFactory.getLogger(PoslovniProces.class);

    /**
     * Dodaje novog pacijenta u poslovni proces i stavalja ga u stanje
     * zakazivanje pregleda
     * @param akcija koju je potrebno procesirati ciji sadrzaj
     *               predstavlja pregled koji se zakazuje
     */
    @Before("execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..)) && args(akcija,..)")
    public void inicijalizacija(Akcija akcija) {

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
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                XMLDBException | IOException e) {
            konekcija.oslobodiResurse(resursi);
            throw new KonekcijaSaBazomIzuzetak("Onemogucen pristup bazi!");
        }
    }

//    @After("execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))")
//    public void prihvatanjePregleda() {
//        // menja stanje pacijenta u stanje prihvatanje termina i zapisuje u fajl
//        logger.info("Usao u after!");
//    }

    @AfterThrowing(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))",
            throwing = "error")
    public void afterThrowingAdvice(JoinPoint jp, Throwable error){
        System.out.println("Method Signature: "  + jp.getSignature());
        System.out.println("Exception: "+error);
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))",
            returning = "retVal")
    public void afterReturningAdvice(JoinPoint jp, Object retVal){
        logger.info("Method Signature: "  + jp.getSignature());
        logger.info("Returning:" + retVal.toString());
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
            pregled.setAttribute("pacijent", this.dobaviPacijenta(akcija));
            pregled.setAttribute("stanje", "zakazivanje");
            pregled.setAttribute("datum", LocalDateTime.now().toString());
            dok.appendChild(pregled);
            return maper.konvertujUString(dok);
        } catch (ParserConfigurationException e) {
            throw new TransformacioniIzuzetak("Greska pri obradi podataka!");
        }
    }

    /**
     * @param akcija koju je potrebno procesirati
     * @return id pacijenta
     */
    private String dobaviPacijenta(Akcija akcija) {
        Document dok = maper.konvertujUDokument(akcija);
        return dok.getFirstChild().getLastChild().getFirstChild().
                getChildNodes().item(2).getAttributes().item(0).getNodeValue();
    }
}
