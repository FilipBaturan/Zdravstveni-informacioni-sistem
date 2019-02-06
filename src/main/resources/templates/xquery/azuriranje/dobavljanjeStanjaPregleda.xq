xquery version "3.1";

declare namespace sp="http://www.zis.rs/seme/stanja_pregleda";
declare namespace stp="http://www.zis.rs/seme/stanje_pregleda";


for $stanje in fn:doc("/db/rs/zis/stanja_pregleda.xml")/sp:stanja_pregleda/stp:stanje_pregleda
where $stanje/@stanje != "kraj"
return fn:concat(fn:concat(fn:data($stanje/@pacijent), ","),fn:data($stanje/@stanje))