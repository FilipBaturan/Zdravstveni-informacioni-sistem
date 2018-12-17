xquery version "3.1";

declare namespace lekovi = "http://zis.rs/zis/seme/lekovi";

for $lekari in fn:doc("/db/rs/zis/lekovi/lekovi.xml")/lekovi:lekovi
return $lekari
