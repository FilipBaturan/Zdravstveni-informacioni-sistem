xquery version "3.1";

declare namespace pa = "http://www.zis.rs/seme/pacijenti";
declare namespace pacijent = "http://www.zis.rs/seme/pacijent";

for $pacijent in fn:doc("/db/rs/zis/pacijenti.xml")/pa:pacijenti/pacijent:pacijent
where $pacijent/pacijent:zdravstveni_karton/@pacijent:identifikator = "%1$s"
return $pacijent/pacijent:obavestenja