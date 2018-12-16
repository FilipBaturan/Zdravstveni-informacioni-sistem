xquery version "3.0";

declare namespace l = "http://localhost:8080/zis/seme/lekari";

for $lekar in fn:doc("lekari.xml")/l:lekari/l:lekar
return <lekar> {$lekar} </lekar> 