xquery version "3.1";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lekar = "http://www.zis.rs/seme/lekar";

declare namespace uputi = "http://www.zis.rs/seme/uputi";
declare namespace uput = "http://www.zis.rs/seme/uput";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";
declare namespace lek = "http://www.zis.rs/seme/lek";

declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace recepti = "http://www.zis.rs/seme/recepti";
declare namespace recept = "http://www.zis.rs/seme/recept";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace pregledi = "http://www.zis.rs/seme/pregledi";
declare namespace pregled = "http://www.zis.rs/seme/pregled";

declare namespace me = "http://www.zis.rs/seme/medicinske_sestre";
declare namespace ms = "http://www.zis.rs/seme/medicinska_sestra";

declare namespace ko = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace izbori = "http://www.zis.rs/seme/izbori";
declare namespace izbor = "http://www.zis.rs/seme/izbor";

let $korisnici := for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/ko:korisnici/korisnik:korisnik
return $korisnik

let $med_ses := for $med_se in fn:doc("/db/rs/zis/medicinske_sestre.xml")/me:medicinske_sestre/ms:medicinska_sestra
return $med_se

let $recepti := for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
return $recept

let $izvestaji := for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
return $izvestaj

let $uputi := for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput return $uput

let $lekovi := for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek return $lek

let $lekari := for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lekar:lekar return $lekar

let $zd_kartoni := for $zd_karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return $zd_karton

let $pregledi := for $pregled in fn:doc("/db/rs/zis/pregledi.xml")/pregledi:pregledi/pregled:pregled
return $pregled

let $izbori := for $izbor in fn:doc("/db/rs/zis/izbori.xml")/izbori:izbori/izbor:izbor
return $izbor

return fn:count($lekari) +  fn:count($lekovi) +  fn:count($uputi) + fn:count($izvestaji) + fn:count($lekari)
        + fn:count($recepti) + fn:count($zd_kartoni) + fn:count($pregledi) + fn:count($med_ses) + fn:count($korisnici)
        + fn:count($izbori)