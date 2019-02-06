xquery version "3.1";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare variable $jbmg :=  for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return if ($karton/@jmbg = "%1$s" and $karton/@aktivan = "true") then "JMBG nije jedinstven! " else "";

declare variable $broj_zdr_knjizice :=  for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return if ($karton/@broj_zdr_knjizice = "%2$s" and $karton/@aktivan = "true") then "Broj zdrastvene knjizice nije jedinstven! " else "";

declare variable $broj_kartona :=  for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return if ($karton/@broj_kartona = "%3$s" and $karton/@aktivan = "true") then "Broj zdrastvenog kartona nije jedinstven! " else "";

declare variable $lbo :=  for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
return if ($karton/@lbo = "%4$s" and $karton/@aktivan = "true") then "Broj licne karte nije jedinstven! " else "";

<rezultat>{$jbmg, $broj_zdr_knjizice, $broj_kartona, $lbo}</rezultat>