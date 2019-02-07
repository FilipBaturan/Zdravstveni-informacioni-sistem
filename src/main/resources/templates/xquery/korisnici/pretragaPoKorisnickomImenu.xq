xquery version "3.1";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace medse = "http://www.zis.rs/seme/medicinske_sestre";
declare namespace medsa = "http://www.zis.rs/seme/medicinska_sestra";

declare namespace pa = "http://www.zis.rs/seme/pacijenti";
declare namespace pacijent = "http://www.zis.rs/seme/pacijent";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare function local:dobavi-karton($pacijent as element()*) as xs:string {
    for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
    where $karton/@id = $pacijent/pacijent:zdravstveni_karton/@pacijent:identifikator
    return fn:data($karton/@id)
};

declare function local:tip-korisnika($korisnik as xs:string) as item()* {
    let $lekar := for $lkr in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
    where $korisnik = $lkr/lkr:korisnik/@lkr:identifikator return $lkr
    let $medicinska_sestra := for $ms in fn:doc("/db/rs/zis/medicinske_sestre.xml")/medse:medicinske_sestre/medsa:medicinska_sestra
    where $korisnik = $ms/medsa:korisnik/@medsa:identifikator return $ms
    let $pacijent := for $pc in fn:doc("/db/rs/zis/pacijenti.xml")/pa:pacijenti/pacijent:pacijent
    where $korisnik = $pc/pacijent:korisnik/@pacijent:identifikator return $pc
    return if ($lekar) then (fn:data($lekar/@id),"LEKAR")
    else if ($medicinska_sestra) then (fn:data($medicinska_sestra/@id),"SESTRA")
        else if ($pacijent) then (local:dobavi-karton($pacijent),"PACIJENT")
            else ()
};

for $kor in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
where $kor/korisnik:korisnicko_ime = "%1$s" and
        $kor/korisnik:lozinka = "%2$s"  and
        $kor/@aktivan = "true"
return (local:tip-korisnika($kor/@id))