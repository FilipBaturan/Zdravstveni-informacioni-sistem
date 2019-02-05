xquery version "3.1";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";
declare namespace lek = "http://www.zis.rs/seme/lek";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

let $upozorenje := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $pc/@id = "%2$s" return $pc/zko:osigurano_lice/zko:upozorenje

for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek
where $lek/@aktivan = "true" and $lek/lek:dijagnoza = "%1$s" and $lek/lek:naziv != fn:data($upozorenje)
return $lek


