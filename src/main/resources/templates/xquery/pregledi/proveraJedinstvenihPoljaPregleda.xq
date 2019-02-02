xquery version "3.1";

declare namespace pregledi = "http://www.zis.rs/seme/pregledi";
declare namespace pregled = "http://www.zis.rs/seme/pregled";

<rezultat> {
    for $pregled in fn:doc("/db/rs/zis/pregledi.xml")/pregledi:pregledi/pregled:pregled
    return if ($pregled/pregled:lekar/@pregled:identifikator = "%1$s" and $pregled/pregled:datum = "%2$s")
    then "Lekar vec ima pregled u zadato vreme!"
    else ()
}
</rezultat>