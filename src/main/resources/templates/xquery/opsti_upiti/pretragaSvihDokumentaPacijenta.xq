xquery version "3.1";

declare namespace uputi = "http://www.zis.rs/seme/uputi";
declare namespace uput = "http://www.zis.rs/seme/uput";

declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace recepti = "http://www.zis.rs/seme/recepti";
declare namespace recept = "http://www.zis.rs/seme/recept";

declare namespace izbori = "http://www.zis.rs/seme/izbori";
declare namespace izbor = "http://www.zis.rs/seme/izbor";

declare variable $ol := "%1$s";

let $recepti := for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
where $recept/recept:osigurano_lice/@recept:identifikator = $ol
return $recept

let $izvestaji := for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
where $izvestaj/izvestaj:osigurano_lice/@izvestaj:identifikator = $ol
return $izvestaj

let $uputi := for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput
where $uput/uput:osigurano_lice/@uput:identifikator = $ol
return $uput

let $izbori := for $izbor in fn:doc("/db/rs/zis/izbori.xml")/izbori:izbori/izbor:izbor
where $izbor/izbor:osigurano_lice/@izbor:identifikator = $ol
return $izbor

return
    <dokumenti> {
        ($recepti,$uputi, $izvestaji,$izbori)
    }
    </dokumenti>
