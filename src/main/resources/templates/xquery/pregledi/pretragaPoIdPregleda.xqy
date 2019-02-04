xquery version "3.1";

declare namespace pregledi = "http://www.zis.rs/seme/pregledi";
declare namespace pregled = "http://www.zis.rs/seme/pregled";

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

for $pregled in fn:doc("/db/rs/zis/pregledi.xml")/pregledi:pregledi/pregled:pregled
let $lekar := local:dobavi-lekara($pregled/pregled:lekar/@pregled:identifikator)
let $pacijent := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $pregled/pregled:pacijent/@pregled:identifikator = $pc/@id return $pc
where $pregled/@id = "%1$s" and $pregled/@aktivan = "true"


return <pregled:pregled xmlns:pregled="http://www.zis.rs/seme/pregled" id="{$pregled/@id}">
    <pregled:pacijent>{$pacijent}</pregled:pacijent>
    <pregled:lekar>{$lekar}</pregled:lekar>
    {$pregled/pregled:datum}
</pregled:pregled>