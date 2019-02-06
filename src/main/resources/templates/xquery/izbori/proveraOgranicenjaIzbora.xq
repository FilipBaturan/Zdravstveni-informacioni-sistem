xquery version "3.1";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

declare function local:tip-lekara($lekar as xs:string) as xs:string {
    for $l in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
    where $l/@id = $lekar return $l/lkr:tip
};

let $karton := for $krt in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $krt/@id = "%1$s" return $krt
return
    if(fn:not($karton/zko:odabrani_lekar/@zko:identifikator = "%2$s"))
    then "Nevalidne prosledjene informacije o proslom lekaru!"
    else if (fn:not(local:tip-lekara("%3$s") = "opsta_praksa"))
    then ("Odabrani lekar nije lekar opste prakse!")
    else ()


