xquery version "3.1";

declare namespace lekari = "http://zis.rs/zis/seme/lekari";
declare namespace lkr = "http://zis.rs/zis/seme/lekar";

declare namespace korisnici = "http://zis.rs/zis/seme/korisnici";
declare namespace korisnik = "http://zis.rs/zis/seme/korisnik";

for $lekar in fn:doc("/db/rs/zis/lekari/lekari.xml")/lekari:lekari/lkr:lekar
for $korisnik in fn:doc("/db/rs/zis/korisnici/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
where $korisnik/@id = $lekar/lkr:korisnik/@lkr:identifikator and $lekar/@id = "%1$s"
return <lkr:lekar xmlns:lkr="http://zis.rs/zis/seme/lekar" id="{$lekar/@id}">
    <korisnik:korisnik xmlns:korisnik="http://zis.rs/zis/seme/korisnik" id="{$korisnik/@id}">
        {$korisnik/korisnik:ime}
        {$korisnik/korisnik:prezime}
        {$korisnik/korisnik:jmbg}
        {$korisnik/korisnik:aktivan}
        {$korisnik/korisnik:korisnicko_ime}
    </korisnik:korisnik>
    {$lekar/lkr:tip}
    {$lekar/lkr:oblast_zastite}
</lkr:lekar>