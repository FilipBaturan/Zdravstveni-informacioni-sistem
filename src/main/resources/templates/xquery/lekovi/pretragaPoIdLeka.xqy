xquery version "3.1";

declare namespace lekovi = "http://zis.rs/zis/seme/lekovi";
declare namespace lek = "http://zis.rs/zis/seme/lek";

for $lek in fn:doc("/db/rs/zis/lekovi/lekovi.xml")/lekovi:lekovi/lek:lek
where $lek/@id = "%1$s"
return $lek
