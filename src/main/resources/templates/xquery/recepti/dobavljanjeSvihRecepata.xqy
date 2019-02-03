xquery version "3.1";

declare namespace recepti = "http://www.zis.rs/seme/recepti";

declare namespace recept = "http://www.zis.rs/seme/recept";

for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
return <recept:recept xmlns:recept="http://www.zis.rs/seme/recept" id="{$recept/@id}">
    {$recept/recept:naziv_zdrastvene_ustanove}
    {$recept/recept:osnova_oslobadjenja_participacije}
    {$recept/recept:osigurano_lice}
    {$recept/recept:datum}
    {$recept/recept:dijagnoza}
    {$recept/recept:opis}
    {$recept/recept:propisani_lek}
    {$recept/recept:lekar}
</recept:recept>
