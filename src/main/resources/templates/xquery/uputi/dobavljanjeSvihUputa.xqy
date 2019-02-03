xquery version "3.1";

declare namespace uputi = "http://www.zis.rs/seme/uputi";

declare namespace uput = "http://www.zis.rs/seme/uput";

for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput
return <uput:uput xmlns:uput="http://www.zis.rs/seme/uput" id="{$uput/@id}">
    {$uput/uput:osigurano_lice}
    {$uput/uput:misljenje}
    {$uput/uput:lekar}
    {$uput/uput:datum}
    {$uput/uput:specialista}
</uput:uput>
