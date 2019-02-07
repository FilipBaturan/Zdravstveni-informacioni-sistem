xquery version "3.1";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare namespace medse = "http://www.zis.rs/seme/medicinske_sestre";
declare namespace medsa = "http://www.zis.rs/seme/medicinska_sestra";

declare namespace pa = "http://www.zis.rs/seme/pacijenti";
declare namespace pacijent = "http://www.zis.rs/seme/pacijent";

declare function local:tip-korisnika($korisnik as xs:string) as xs:string {
    let $lekar := for $lkr in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
    where $korisnik = $lkr/lkr:korisnik/@lkr:identifikator return "true"
    let $medicinska_sestra := for $ms in fn:doc("/db/rs/zis/medicinske_sestre.xml")/medse:medicinske_sestre/medsa:medicinska_sestra
    where $korisnik = $ms/medsa:korisnik/@medsa:identifikator return "true"
    let $pacijent := for $pc in fn:doc("/db/rs/zis/pacijenti.xml")/pa:pacijenti/pacijent:pacijent
    where $korisnik = $pc/pacijent:korisnik/@pacijent:identifikator return "true"
    return if ($lekar = "true") then "LEKAR"
    else if ($medicinska_sestra = "true") then "SESTRA"
        else if ($pacijent = "true") then "PACIJENT"
            else ()
};

for $kor in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
where $kor/korisnik:korisnicko_ime = "%1$s" and
        $kor/korisnik:lozinka = "%2$s"  and
        $kor/@aktivan = "true"
return (fn:data($kor/@id), local:tip-korisnika($kor/@id))