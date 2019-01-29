xquery version "3.1";

declare namespace pregledi = "http://www.zis.rs/seme/pregledi";
declare namespace pregled = "http://www.zis.rs/seme/pregled";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

for $pregled in fn:doc("/db/rs/zis/pregledi.xml")/pregledi:pregledi/pregled:pregled
for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
where $lekar/@id = $pregled/pregled:lekar/@pregled:identifikator and $pregled/@id = "%1$s"

return <pregled:pregled xmlns:pregled="http://www.zis.rs/seme/pregled" id="{$pregled/@id}">
    <lkr:lekar xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$lekar/@id}">
        {$lekar/lkr:tip}
        {$lekar/lkr:oblast_zastite}
    </lkr:lekar>
    {$pregled/pregled:datum}
</pregled:pregled>