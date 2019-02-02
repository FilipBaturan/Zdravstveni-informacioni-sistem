declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace lekari = "http://www.zis.rs/seme/lekari";
declare namespace lkr = "http://www.zis.rs/seme/lekar";

for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
for $lekar in fn:doc("/db/rs/zis/lekari.xml")/lekari:lekari/lkr:lekar
where $lekar/@id = $izvestaj/izvestaj:lekar/@izvestaj:identifikator and $izvestaj/@id = "http://www.zis.rs/izvestaji/id3"

return <izvestaj:izvestaj xmlns:izvestaj="http://www.zis.rs/seme/izvestaj">
    <lkr:lekar xmlns:korisnik="http://www.zis.rs/seme/korisnik" id="{$lekar/@id}">
        {$lekar/lkr:tip}
        {$lekar/lkr:oblast_zastite}
    </lkr:lekar>
    {$izvestaj/izvestaj:anamneza}
    {$izvestaj/izvestaj:dijagnoza}
    {$izvestaj/izvestaj:datum}
</izvestaj:izvestaj>