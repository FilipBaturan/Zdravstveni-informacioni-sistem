xquery version "3.1";

declare namespace recepti = "http://www.zis.rs/seme/recepti";
declare namespace recept = "http://www.zis.rs/seme/recept";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";
declare namespace lek = "http://www.zis.rs/seme/lek";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";


declare function local:dobavi-lek ($id as xs:anyURI) as element()* {
    for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek
    where $lek/@aktivan = "true" and $lek/@id = $id
    return <lek:lek xmlns:lek="http://www.zis.rs/seme/lek" id="{$lek/@id}">
        {$lek/lek:naziv}
        {$lek/lek:sifra}
        {$lek/lek:dijagnoza}
        {$lek/lek:namena}
    </lek:lek>
};

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


for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
let $lekar := local:dobavi-lekara($recept/recept:lekar/@recept:identifikator)
let $lek := local:dobavi-lek($recept/recept:propisani_lek/@recept:identifikator)
let $pacijent := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $recept/@aktivan = "true" and $recept/recept:osigurano_lice/@recept:identifikator = $pc/@id return $pc
return <recept:recept xmlns:recept="http://www.zis.rs/seme/recept" id="{$recept/@id}">
    {$recept/recept:naziv_zdravstvene_ustanove}
    <recept:osigurano_lice>{$pacijent}</recept:osigurano_lice>
    {$recept/recept:osnova_oslobadjenja_participacije}
    {$recept/recept:datum}
    {$recept/recept:dijagnoza}
    {$recept/recept:opis}
    <recept:propisani_lek>{$lek}</recept:propisani_lek>
    <recept:lekar>{$lekar}</recept:lekar>
</recept:recept>