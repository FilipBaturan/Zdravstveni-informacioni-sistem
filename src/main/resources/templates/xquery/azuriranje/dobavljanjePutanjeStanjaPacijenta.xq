xquery version "3.1";

declare namespace sp = "http://www.zis.rs/seme/stanja_pregleda";
declare namespace stp = "http://www.zis.rs/seme/stanje_pregleda";

declare namespace functx = "http://www.functx.com";

declare function functx:last-node
( $nodes as node()* )  as node()? {

    ($nodes/.)[fn:last()]
} ;

declare function functx:index-of-node
( $nodes as node()* ,
        $nodeToFind as node() )  as xs:integer* {

    for $seq in (1 to fn:count($nodes))
    return $seq[$nodes[$seq] is $nodeToFind]
} ;

declare function functx:path-to-node-with-pos
( $node as node()? )  as xs:string {

    fn:string-join(
            for $ancestor in $node/ancestor-or-self::*
            let $sibsOfSameName := $ancestor/../*[fn:name() = fn:name($ancestor)]
            return fn:concat(fn:name($ancestor),
                    if (fn:count($sibsOfSameName) <= 1)
                    then ''
                    else fn:concat(
                            '[',functx:index-of-node($sibsOfSameName,$ancestor),']'))
            , '/')
} ;

declare variable  $stanja :=
    for $stanje in fn:doc("/db/rs/zis/stanja_pregleda.xml")/sp:stanja_pregleda/stp:stanje_pregleda
    where $stanje/@pacijent = "%1$s"
    return $stanje;

let $stanje := functx:last-node($stanja)
return fn:concat("/", functx:path-to-node-with-pos($stanje))
