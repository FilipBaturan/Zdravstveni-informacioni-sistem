xquery version "3.1";

declare namespace medicinske_sestre = "http://www.zis.rs/seme/medicinske_sestre";
declare namespace medicinska_sestra = "http://www.zis.rs/seme/medicinska_sestra";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

<medicinske_sestre xmlns:medicinske_sestre="http://www.zis.rs/seme/medicinske_sestre" xmlns:voc="http://www.zis.rs/rdf/voc#">{
    for $medicinska_sestra in fn:doc("/db/rs/zis/medicinske_sestre.xml")/medicinske_sestre:medicinske_sestre/medicinska_sestra:medicinska_sestra
    for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
    where $korisnik/@id = $medicinska_sestra/medicinska_sestra:korisnik/@medicinska_sestra:identifikator
            and $korisnik/@aktivan = "true"
    return <medicinska_sestra:medicinska_sestra xmlns:medicinska_sestra="http://www.zis.rs/seme/medicinska_sestra" id="{$medicinska_sestra/@id}">
        <korisnik:korisnik xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$korisnik/@id}">
            {$korisnik/korisnik:ime}
            {$korisnik/korisnik:prezime}
            {$korisnik/korisnik:jmbg}
            {$korisnik/korisnik:aktivan}
            {$korisnik/korisnik:korisnicko_ime}
        </korisnik:korisnik>
    </medicinska_sestra:medicinska_sestra>
}
</medicinske_sestre>
