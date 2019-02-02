xquery version "3.1";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";

declare namespace lek = "http://www.zis.rs/seme/lek";

for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek
return <lek:lek xmlns:pregled="http://www.zis.rs/seme/lek" id="{$lek/@id}">
    {$lek/lek:naziv}
    {$lek/lek:sifra}
    {$lek/lek:dijagnoza}
    {$lek/lek:namena}
</lek:lek>
