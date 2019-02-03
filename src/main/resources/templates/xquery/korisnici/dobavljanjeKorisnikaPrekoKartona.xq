xquery version "3.1";

declare namespace korisnici = "http://www.zis.rs/seme/korisnici";
declare namespace korisnik = "http://www.zis.rs/seme/korisnik";

declare namespace pacijenti = "http://www.zis.rs/seme/pacijenti";
declare namespace pacijent = "http://www.zis.rs/seme/pacijent";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

for $korisnik in fn:doc("/db/rs/zis/korisnici.xml")/korisnici:korisnici/korisnik:korisnik
for $pacijent in fn:doc("/db/rs/zis/pacijenti.xml")/pacijenti:pacijenti/pacijent:pacijent
for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $karton/@id = "%1$s" and
        $pacijent/pacijent:korisnik/@pacijent:identifikator = $korisnik/@id  and
        $karton/@id = $pacijent/pacijent:zdravstveni_karton/@pacijent:identifikator
return $korisnik