xquery version "3.1";

declare namespace izbori = "http://www.zis.rs/seme/izbori";
declare namespace izbor = "http://www.zis.rs/seme/izbor";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

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

<izbori:izbori xmlns:izbor="http://www.zis.rs/seme/izbor" xmlns:izbori="http://www.zis.rs/seme/izbori">
    {
        for $izbor in fn:doc("/db/rs/zis/izbori.xml")/izbori:izbori/izbor:izbor
        let $lekar := local:dobavi-lekara($izbor/izbor:lekar/@izbor:identifikator)
        let $prosli_lekar := local:dobavi-lekara($izbor/izbor:prosli_lekar/@izbor:identifikator)
        let $pacijent := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
        where $izbor/izbor:osigurano_lice/@izbor:identifikator = $pc/@id return $pc
        return
            <izbor:izbor oznaka="{$izbor/@oznaka}" id="{$izbor/@id}" aktivan="{$izbor/@aktivan}">
                {$izbor/izbor:naziv_ustanove}
                {$izbor/izbor:tip_obrasca}
                {$izbor/izbor:razlog_promene}
                <izbor:prosli_lekar>{$prosli_lekar}</izbor:prosli_lekar>
                <izbor:lekar>{$lekar}</izbor:lekar>
                <izbor:osigurano_lice>{$pacijent}</izbor:osigurano_lice>
                {$izbor/izbor:datum}
            </izbor:izbor>
    }
</izbori:izbori>



