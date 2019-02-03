xquery version "3.1";

declare namespace recepti = "http://www.zis.rs/seme/recepti";

declare namespace recept = "http://www.zis.rs/seme/recept";

for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
where $recept/@aktivan = "true" and $recept/@id = "http://www.zis.rs/recepti/id1"
return <recept:recept xmlns:recept="http://www.zis.rs/seme/recept" id="{$recept/@id}">
    {$recept/recept:naziv_zdravstvene_ustanove}
    {$recept/recept:datum}
    {$recept/recept:dijagnoza}
    {$recept/recept:opis}
</recept:recept>