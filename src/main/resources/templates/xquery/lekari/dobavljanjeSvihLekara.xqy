xquery version "3.1";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

<lekari xmlns:lekari="http://www.zis.rs/seme/lekari" xmlns:voc="http://www.zis.rs/rdf/voc#">{
    for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
    for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
    where $korisnik/@id = $lekar/lkr:korisnik/@lkr:identifikator and $korisnik/@aktivan = "true"
    return <lkr:lekar xmlns:lkr="http://www.zis.rs/seme/lekar" id="{$lekar/@id}">
        <korisnik:korisnik xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$korisnik/@id}">
            {$korisnik/korisnik:ime}
            {$korisnik/korisnik:prezime}
            {$korisnik/korisnik:jmbg}
            {$korisnik/korisnik:aktivan}
            {$korisnik/korisnik:korisnicko_ime}
        </korisnik:korisnik>
        {$lekar/lkr:tip}
        {$lekar/lkr:oblast_zastite}
    </lkr:lekar>
}
</lekari>
