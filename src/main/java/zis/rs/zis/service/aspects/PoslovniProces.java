package zis.rs.zis.service.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import zis.rs.zis.domain.enums.*;
import zis.rs.zis.service.nonProcessService.StanjaPregledaServis;
import zis.rs.zis.service.states.Proces;
import zis.rs.zis.util.IOStrimer;
import zis.rs.zis.util.Maper;
import zis.rs.zis.util.ValidacioniIzuzetak;
import zis.rs.zis.util.akcije.Akcija;

@Aspect
@Configuration
public class PoslovniProces extends IOStrimer {

    @Autowired
    private StanjaPregledaServis servis;

    @Autowired
    private Maper maper;

    @Autowired
    private Proces proces;

    @Before("execution(* zis.rs.zis.service.states.ZakazivanjePregleda.obradiZahtev(..)) && args(akcija,..)")
    public void preKreiranjaTermina(Akcija akcija) {
        if (!akcija.getFunkcija().equals(TipAkcije.DODAVANJE.toString())) {
            throw new ValidacioniIzuzetak("Nevalidna prosledjena akcija!");
        }

    }

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
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        if (proces.getProcesi().containsKey(pacijent)) {
            servis.izmeniProces(Stanja.CEKANJE.toString(), pacijent);
        } else {
            servis.dodajNoviProces(akcija);
        }
        proces.getProcesi().put(pacijent, proces.getPrihvatanjeTermina());
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.izmenaTermina(..)) && args(akcija,..)")
    public void nakonIzmeneTermina(Akcija akcija) {
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        servis.izmeniProces(Stanja.IZMENJEN_TERMIN.toString(), pacijent);
        proces.getProcesi().put(pacijent, proces.getIzmenjenTermin());
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.odbijanjeTermina(..)) && args(akcija,..)")
    public void nakonOdbijanjaTermina(Akcija akcija) {
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        servis.izmeniProces(Stanja.KRAJ.toString(), pacijent);
        proces.getProcesi().remove(pacijent);
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.PrihvatanjeTermina.obradaTermina(..)) && args(akcija,..)")
    public void nakonPrihvatanjaTermina(Akcija akcija) {
        String tipLekara = maper.dobaviTipLekaraIzPregleda(akcija);
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        if (tipLekara.equals(TipLekara.OPSTA_PRAKSA.toString())) {
            servis.izmeniProces(Stanja.OPSTI_PREGLED.toString(), pacijent);
            proces.getProcesi().put(pacijent, proces.getOpstiPregled());
        } else {
            servis.izmeniProces(Stanja.SPECIJALISTICKI_PREGLED.toString(), pacijent);
            proces.getProcesi().put(pacijent, proces.getSpecijalistickiPregled());
        }
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.IzmenjenTermin.odbijanjeTermina(..)) && args(akcija,..)")
    public void nakonOdbijanjaIzmenjenogTermina(Akcija akcija) {
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        servis.izmeniProces(Stanja.KRAJ.toString(), pacijent);
        proces.getProcesi().remove(pacijent);
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.IzmenjenTermin.obradaTermina(..)) && args(akcija,..)")
    public void nakonPrihvatanjaIzmenjenogTermina(Akcija akcija) {
        String tipLekara = maper.dobaviTipLekaraIzPregleda(akcija);
        String pacijent = maper.dobaviPacijentaIzPregleda(akcija);
        if (tipLekara.equals(TipLekara.OPSTA_PRAKSA.toString())) {
            servis.izmeniProces(Stanja.OPSTI_PREGLED.toString(), pacijent);
            proces.getProcesi().put(pacijent, proces.getOpstiPregled());
        } else {
            servis.izmeniProces(Stanja.SPECIJALISTICKI_PREGLED.toString(), pacijent);
            proces.getProcesi().put(pacijent, proces.getSpecijalistickiPregled());
        }
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.SpecijalistickiPregled.kreiranjeIzvestaja(..)) && args(akcija,..)")
    public void nakonSpecijalistickogPregleda(Akcija akcija) {
        String pacijent = maper.dobaviPacijentaIzIzvestaja(akcija);
        servis.izmeniProces(Stanja.ZAKAZIVANJE_TERMINA.toString(), pacijent);
        proces.getProcesi().put(pacijent, proces.getZakazivanjePregleda());
    }

    @AfterReturning(pointcut = "execution(* zis.rs.zis.service.states.OpstiPregled.kreiranjeDokumentacije(..)) && args(akcija,..)")
    public void nakonOpstegPregleda(Akcija akcija) {
        Document dok = maper.konvertujUDokument(akcija);
        boolean izvestaj = false, uput = false;
        NodeList lista = dok.getFirstChild().getLastChild().getFirstChild().getChildNodes();
        for (int i = 0; i < lista.getLength(); i++) {
            try {
                switch (lista.item(i).getLocalName()) {
                    case "izvestaj":
                        izvestaj = true;
                        break;
                    case "uput":
                        uput = true;
                        break;
                }
            } catch (NullPointerException e) {
                break;
            }
        }

        if (izvestaj && uput) {
            servis.izmeniProces(Stanja.ZAKAZIVANJE_TERMINA.toString(),
                    maper.dobaviPacijentaIzDokumentacije(akcija));
            proces.getProcesi().put(maper.dobaviPacijentaIzDokumentacije(akcija), proces.getZakazivanjePregleda());
        } else {
            servis.izmeniProces(Stanja.KRAJ.toString(),
                    maper.dobaviPacijentaIzDokumentacije(akcija));
            proces.getProcesi().remove(maper.dobaviPacijentaIzDokumentacije(akcija));
        }
    }

    @Before("execution(* zis.rs.zis.service.nonProcessService.IzborPromenaServis.sacuvaj(..)) && args(akcija,..)")
    public void preIzmeneLekara(Akcija akcija) {
        String pacijent = maper.dobaviPacijentaIzPromeneLekara(akcija);
        if (proces.getProcesi().containsKey(pacijent)) {
            throw new ValidacioniIzuzetak("Onemogucena promena lekara u toku pregleda.");
        }
    }
}
