xquery version "3.1";

declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

<zd:zdravstveni_kartoni xmlns:zd="http://www.zis.rs/seme/zdravstveni_kartoni"
xmlns:zko="http://www.zis.rs/seme/zdravstveni_karton">
    {
        for $karton in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
        where $karton/@aktivan = "true"
        return $karton
    }
</zd:zdravstveni_kartoni>


