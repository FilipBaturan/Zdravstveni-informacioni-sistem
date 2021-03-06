xquery version "3.1";

declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";


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
        </lkr:lekar>
};

for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
let $lekar := local:dobavi-lekara(for $lk in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
where $lk/@id = $izvestaj/izvestaj:lekar/@izvestaj:identifikator return $lk)
let $pacijent := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $pc/@id = $izvestaj/izvestaj:osigurano_lice/@izvestaj:identifikator return $pc
where $izvestaj/@id = "%1$s" and $izvestaj/@aktivan = "true"

return <izvestaj:izvestaj xmlns:izvestaj="http://www.zis.rs/seme/izvestaj"
about="{$izvestaj/@id}" aktivan="true" id="{$izvestaj/@id}" oznaka="il1">
    <izvestaj:lekar>{$lekar}</izvestaj:lekar>
    {$izvestaj/izvestaj:anamneza}
    {$izvestaj/izvestaj:dijagnoza}
    {$izvestaj/izvestaj:terapija}
    {$izvestaj/izvestaj:datum}
    <izvestaj:osigurano_lice>{$pacijent}</izvestaj:osigurano_lice>
</izvestaj:izvestaj>