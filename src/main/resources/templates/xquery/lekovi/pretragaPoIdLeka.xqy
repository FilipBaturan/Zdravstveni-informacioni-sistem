xquery version "3.1";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";
declare namespace lek = "http://www.zis.rs/seme/lek";

for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek
where $lek/@aktivan = "true" and $lek/@id = "%1$s"
return $lek
