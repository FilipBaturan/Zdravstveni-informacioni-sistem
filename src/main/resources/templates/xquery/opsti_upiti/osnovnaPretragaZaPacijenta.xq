xquery version "3.1";

declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace recepti = "http://www.zis.rs/seme/recepti";
declare namespace recept = "http://www.zis.rs/seme/recept";

declare namespace uputi = "http://www.zis.rs/seme/uputi";
declare namespace uput = "http://www.zis.rs/seme/uput";

declare namespace izbori = "http://www.zis.rs/seme/izbori";
declare namespace izbor = "http://www.zis.rs/seme/izbor";

declare function local:pronadji-recepte($text as xs:string) as item()* {
    let $recepti := for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
    return
        if (fn:contains($recept/recept:opis/text(), $text))
        then fn:data($recept/@id)
        else ()
    return $recepti
};

declare function local:pronadji-upute($text as xs:string) as item()* {
    let $uputi := for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput
    return
        if (fn:contains($uput/uput:misljenje/text(), $text))
        then fn:data($uput/@id)
        else ()
    return $uputi
};

declare function local:pronadji-izvestaje($text as xs:string) as item()* {
    let $izvestaji := for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
    return
        if (fn:contains($izvestaj/izvestaj:anamneza/text(), $text) or
                fn:contains($izvestaj/izvestaj:terapija/text(), $text))
        then fn:data($izvestaj/@id)
        else ()
    return $izvestaji
};

declare function local:pronadji-izbore($text as xs:string) as item()* {
    let $izbori := for $izbor in fn:doc("/db/rs/zis/izbori.xml")/izbori:izbori/izbor:izbor
    return
        if (fn:contains($izbor//text(), $text))
        then fn:data($izbor/@id)
        else ()
    return $izbori
};

let $recepti := local:pronadji-recepte("%1$s")
let $uputi := local:pronadji-upute("%1$s")
let $izvestaji := local:pronadji-izvestaje("%1$s")
let $izbori := local:pronadji-izbore("%1$s")
return ($uputi, $recepti, $izvestaji, $izbori)
