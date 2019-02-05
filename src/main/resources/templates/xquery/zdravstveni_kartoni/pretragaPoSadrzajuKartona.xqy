xquery version "3.1";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";


let $kartoni := for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return 
    if(fn:contains($karton//text(), "%1$s")) then fn:data($karton/@id)
    else ()
return $kartoni

