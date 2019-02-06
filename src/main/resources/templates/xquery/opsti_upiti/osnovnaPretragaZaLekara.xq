xquery version "3.1";

declare namespace izvestaji = "http://www.zis.rs/seme/izvestaji";
declare namespace izvestaj = "http://www.zis.rs/seme/izvestaj";

declare namespace recepti = "http://www.zis.rs/seme/recepti";
declare namespace recept = "http://www.zis.rs/seme/recept";

declare namespace uputi = "http://www.zis.rs/seme/uputi";
declare namespace uput = "http://www.zis.rs/seme/uput";

declare function local:pronadji-recepte($text as xs:string) as element()* {
    let $recepti := for $recept in fn:doc("/db/rs/zis/recepti.xml")/recepti:recepti/recept:recept
    return
        if (fn:contains($recept/recept:opis/text(), $text))
        then <recept>{fn:data($recept/@id)}</recept>
        else ()
    return $recepti
};

declare function local:pronadji-upute($text as xs:string) as element()* {
    let $uputi := for $uput in fn:doc("/db/rs/zis/uputi.xml")/uputi:uputi/uput:uput
    return
        if (fn:contains($uput/uput:misljenje/text(), $text))
        then <uput>{fn:data($uput/@id)}</uput>
        else ()
    return $uputi
};

declare function local:pronadji-izvestaje($text as xs:string) as element()* {
    let $izvestaji := for $izvestaj in fn:doc("/db/rs/zis/izvestaji.xml")/izvestaji:izvestaji/izvestaj:izvestaj
    return
        if (fn:contains($izvestaj/izvestaj:anamneza/text(), $text) or
                fn:contains($izvestaj/izvestaj:terapija/text(), $text))
        then <izvestaj>{fn:data($izvestaj/@id)}</izvestaj>
        else ()
    return $izvestaji
};


let $recepti := local:pronadji-recepte("eU")
let $uputi := local:pronadji-upute("p")
let $izvestaji := local:pronadji-izvestaje("c")
return <dokumenti>{$uputi, $recepti, $izvestaji}</dokumenti>
