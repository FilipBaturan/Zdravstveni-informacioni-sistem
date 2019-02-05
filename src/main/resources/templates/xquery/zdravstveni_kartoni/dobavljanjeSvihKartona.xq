xquery version "3.1";

let $kartoni := fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")
return $kartoni