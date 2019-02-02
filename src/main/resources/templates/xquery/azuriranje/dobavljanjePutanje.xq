xquery version "3.1";

declare namespace functx = "http://www.functx.com";

declare function functx:index-of-node
($nodes as node()*,
        $nodeToFind as node()) as xs:integer* {

    for $seq in (1 to fn:count($nodes))
    return $seq[$nodes[$seq] is $nodeToFind]
};

declare function functx:path-to-node-with-pos
($node as node()?) as xs:string {

    fn:string-join(
            for $ancestor in $node/ancestor-or-self::*
            let $sibsOfSameName := $ancestor/../*[fn:name() = fn:name($ancestor)]
            return fn:concat(fn:name($ancestor),
                    if (fn:count($sibsOfSameName) <= 1)
                    then ''
                    else fn:concat(
                            '[', functx:index-of-node($sibsOfSameName, $ancestor), ']'))
            , '/')
};

let $dokument := fn:doc("%1$s")
return fn:concat("/", functx:path-to-node-with-pos($dokument//@id[. = "%2$s"]))

