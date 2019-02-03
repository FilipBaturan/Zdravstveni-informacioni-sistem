xquery version "3.1";

declare namespace izbori = "http://www.zis.rs/seme/izbori";

declare namespace izbor = "http://www.zis.rs/seme/izbor";

for $izbor in fn:doc("/db/rs/zis/izbori.xml")/izbori:uputi/izbor:uput
return <izbor:izbor xmlns:ipl1="http://www.zis.rs/seme/izbor" id="{$izbor/@id}">
    {$izbor/izbor:osigurano_lice}
    {$izbor/izbor:misljenje}
    {$izbor/izbor:lekar}
    {$izbor/izbor:datum}
    {$izbor/izbor:specialista}
</izbor:izbor>
