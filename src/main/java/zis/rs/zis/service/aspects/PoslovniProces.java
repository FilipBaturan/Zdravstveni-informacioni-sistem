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
import zis.rs.zis.domain.enums.*;
import zis.rs.zis.repository.xml.StanjaPregledaXMLRepozitorijum;
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
    private StanjaPregledaXMLRepozitorijum stanjaRepozitorijum;

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
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            proces.getPrihvatanjeTermina().setOpcija(Opcije.ODBIJANJE_PREGLEDA);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString()) &&
                akcija.getKontekst().equals(Kontekst.IZMENA.toString())) {
            proces.getPrihvatanjeTermina().setOpcija(Opcije.IZMENA_PREGLEDA);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString()) &&
                akcija.getKontekst().equals(Kontekst.PRIHVATANJE.toString())) {
            proces.getPrihvatanjeTermina().setOpcija(Opcije.PRIHVATANJE_PREGLEDA);
        } else {
            throw new ValidacioniIzuzetak("Nevalidna prosledjena akcija!");
        }

    }

    @Before("execution(* zis.rs.zis.service.states.IzmenjenTermin.obradiZahtev(..)) && args(akcija,..)")
    public void prePrihvatanjaIzmenjenogTermina(Akcija akcija) {
        if (akcija.getFunkcija().equals(TipAkcije.BRISANJE.toString())) {
            proces.getPrihvatanjeTermina().setOpcija(Opcije.ODBIJANJE_PREGLEDA);
        } else if (akcija.getFunkcija().equals(TipAkcije.IZMENA.toString()) &&
                akcija.getKontekst().equals(Kontekst.PRIHVATANJE.toString())) {
            proces.getPrihvatanjeTermina().setOpcija(Opcije.PRIHVATANJE_PREGLEDA);
        } else {
            throw new ValidacioniIzuzetak("Nevalidna prosledjena akcija!");
        }
    }

    /**
     * Dodaje novog pacijenta u poslovni proces i stavalja ga u stanje cekanje
     *
     * @param akcija koju je potrebno procesirati ciji sadrzaj
     *               predstavlja pregled koji se zakazuje
     */
    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..)) && args(akcija,..)")
    public void nakonKreiranjaPregleda(Akcija akcija) {
        stanjaRepozitorijum.dodajNoviProces(akcija);
        proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getPrihvatanjeTermina());
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.izmenaTermina(..)) && args(akcija,..)")
    public void nakonIzmeneTermina(Akcija akcija) {
        stanjaRepozitorijum.izmeniProces(akcija, Stanja.IZMENJEN_TERMIN.toString());
        proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getIzmenjenTermin());
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.odbijanjeTermina(..)) && args(akcija,..)")
    public void nakonOdbijanjaTermina(Akcija akcija) {
        stanjaRepozitorijum.izmeniProces(akcija, Stanja.KRAJ.toString());
        proces.getProcesi().remove(maper.dobaviPacijentaIzPregleda(akcija));
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.prihvatanjeTermina(..)) && args(akcija,..)")
    public void nakonPrihvatanjaTermina(Akcija akcija) {
        String tipLekara = maper.dobaviTipLekaraIzPregleda(akcija);
        if (tipLekara.equals(TipLekara.OPSTA_PRAKSA.toString())) {
            stanjaRepozitorijum.izmeniProces(akcija, Stanja.OPSTI_PREGLED.toString());
            proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getOpstiPregled());
        } else {
            stanjaRepozitorijum.izmeniProces(akcija, Stanja.SPECIJALISTICKI_PREGLED.toString());
            proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getSpecijalistickiPregled());
        }
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.IzmenjenTermin.odbijanjeTermina(..)) && args(akcija,..)")
    public void nakonOdbijanjaIzmenjenogTermina(Akcija akcija) {
        stanjaRepozitorijum.izmeniProces(akcija, Stanja.KRAJ.toString());
        proces.getProcesi().remove(maper.dobaviPacijentaIzPregleda(akcija));
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.IzmenjenTermin.prihvatanjeTermina(..)) && args(akcija,..)")
    public void nakonPrihvatanjaIzmenjenogTermina(Akcija akcija) {
        String tipLekara = maper.dobaviTipLekaraIzPregleda(akcija);
        if (tipLekara.equals(TipLekara.OPSTA_PRAKSA.toString())) {
            stanjaRepozitorijum.izmeniProces(akcija, Stanja.OPSTI_PREGLED.toString());
            proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getOpstiPregled());
        } else {
            stanjaRepozitorijum.izmeniProces(akcija, Stanja.SPECIJALISTICKI_PREGLED.toString());
            proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getSpecijalistickiPregled());
        }
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.SpecijalistickiPregled.kreiranjeIzvestaja(..)) && args(akcija,..)")
    public void nakonSpecijalistickogPregleda(Akcija akcija) {
        stanjaRepozitorijum.izmeniProces(akcija, Stanja.ZAKAZIVANJE_TERMINA.toString());
        proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getZakazivanjePregleda());
    }

//    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.SpecijalistickiPregled.kreiranjeIzvestaja(..)) && args(akcija,..)")
//    public void nakonOpstegPregleda(Akcija akcija) {
//        stanjaRepozitorijum.izmeniProces(akcija, Stanja.ZAKAZIVANJE_TERMINA.toString());
//        proces.getProcesi().put(maper.dobaviPacijentaIzPregleda(akcija), proces.getZakazivanjePregleda());
//    }
}
