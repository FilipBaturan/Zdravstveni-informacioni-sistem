
declare namespace zd= "http://www.zis.rs/seme/zdravstveni_kartoni";
declare namespace zko="http://www.zis.rs/seme/zdravstveni_karton";

declare namespace lekovi = "http://www.zis.rs/seme/lekovi";
declare namespace lek = "http://www.zis.rs/seme/lek";

declare namespace functx = "http://www.functx.com";
declare function functx:is-value-in-sequence
( $value as xs:anyAtomicType? ,
        $seq as xs:anyAtomicType* )  as xs:boolean {

    $value = $seq
} ;


let $upozorenje := for $pc in fn:doc("/db/rs/zis/zdravstveni_kartoni.xml")/zd:zdravstveni_kartoni/zko:zdravstveni_karton
where $pc/@id = "%2$s" return $pc/@upozorenje

for $lek in fn:doc("/db/rs/zis/lekovi.xml")/lekovi:lekovi/lek:lek
where $lek/@aktivan = "true" and $lek/lek:dijagnoza = "%1$s" and fn:not(functx:is-value-in-sequence($lek/lek:naziv, tokenize(fn:data($upozorenje), '-')) )
return $lek


