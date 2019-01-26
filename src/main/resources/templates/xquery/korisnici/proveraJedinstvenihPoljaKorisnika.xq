xquery version "3.1";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

<rezultat> {
    for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
    return if ($korisnik/korisnik:jmbg = "%1$s" and $korisnik/korisnik:korisnicko_ime = "%2$s")
    then "JMBG i korisnicko ime vec postoje!"
    else if ($korisnik/korisnik:jmbg = "%1$s")
        then "JMBG vec postoji!"
        else if ($korisnik/korisnik:korisnicko_ime = "%2$s")
            then "Korisnicko ime vec postoji!"
            else ()
}
</rezultat>