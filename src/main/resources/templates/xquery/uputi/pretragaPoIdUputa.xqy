xquery version "3.1";

declare namespace uputi = "http://www.zis.rs/seme/uputi";
declare namespace uput = "http://www.zis.rs/seme/uput";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare function local:dobavi-lekara ($id as xs:anyURI) as element()* {
    for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
    for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
    where $lekar/@id = $id and $korisnik/@id = $lekar/lkr:korisnik/@lkr:identifikator
    return
        <lkr:lekar xmlns:lkr="http://www.zis.rs/seme/lekar" xmlns:voc="http://www.zis.rs/rdf/voc#" id="{$lekar/@id}">
            <korisnik:korisnik xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$korisnik/@id}">
                {$korisnik/korisnik:ime}
                {$korisnik/korisnik:prezime}
                {$korisnik/korisnik:jmbg}
                {$korisnik/korisnik:aktivan}
                {$korisnik/korisnik:korisnicko_ime}
            </korisnik:korisnik>
            {$lekar/lkr:tip}
            {$lekar/lkr:oblast_zastite}
            {$lekar/lkr:broj_pacijenata}
        </lkr:lekar>
};

for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput
let $lekar := local:dobavi-lekara($uput/uput:lekar/@uput:identifikator)
let $specijalista := local:dobavi-lekara($uput/uput:specialista/@uput:identifikator)
let $pacijent := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $uput/uput:osigurano_lice/@uput:identifikator = $pc/@id return $pc
where $uput/@aktivan = "true" and $uput/@id = "$1%s"
return <uput:uput xmlns:uput="http://www.zis.rs/seme/uput" id="{$uput/@id}">
    <uput:osigurano_lice>{$pacijent}</uput:osigurano_lice>
    {$uput/uput:misljenje}
    <uput:lekar>{$lekar}</uput:lekar>
    {$uput/uput:datum}
    <uput:specialista>{$specijalista}</uput:specialista>
</uput:uput>
