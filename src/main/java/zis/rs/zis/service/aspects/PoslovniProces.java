package zis.rs.zis.service.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import zis.rs.zis.util.akcije.Akcija;

@Aspect
@Configuration
public class PoslovniProces {

    private static final Logger logger = LoggerFactory.getLogger(PoslovniProces.class);

    @Before("execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..)) && args(akcija,..)")
    public void inicijalizacija(Akcija akcija) {
        // dodaje novog pacijenta u poslovni_procesi.xml
        logger.info(akcija.getFunkcija());
        logger.info("Usao u before!");
    }

    @After("execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))")
    public void prihvatanjePregleda() {
        // menja stanje pacijenta u stanje prihvatanje termina i zapisuje u fajl
        logger.info("Usao u after!");
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.ZakazivanjePregleda.kreirajPregled(..))",
            returning = "retVal")
    public void afterReturningAdvice(JoinPoint jp, Object retVal){
        logger.info("Method Signature: "  + jp.getSignature());
        logger.info("Returning:" + retVal.toString());
    }
}
