xquery version "3.1";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare function local:prebroji-pacijente($lekar as xs:anyURI) as xs:integer {
    let $kartoni :=
        for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
        where $karton/zko:odabrani_lekar/@zko:identifikator = $lekar and $karton/@aktivan = "true"
        return $karton/@id
    return fn:count($kartoni)
};

<rezultat>
    {
        for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
        for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
        where $korisnik/@id = $lekar/lkr:korisnik/@lkr:identifikator and $korisnik/@aktivan = "true"
        return
            <lkr:lekar xmlns:lkr="http://www.zis.rs/seme/lekar" xmlns:voc="http://www.zis.rs/rdf/voc#" id="{$lekar/@id}">
                <korisnik:korisnik xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$korisnik/@id}">
                    {$korisnik/korisnik:ime}
                    {$korisnik/korisnik:prezime}
                    {$korisnik/korisnik:jmbg}
                </korisnik:korisnik>
                {$lekar/lkr:tip}
                {$lekar/lkr:oblast_zastite}
                {$lekar/lkr:broj_pacijenata}
                <broj_pacijenata>{local:prebroji-pacijente($lekar/@id)}</broj_pacijenata>
            </lkr:lekar>
    }
</rezultat>
