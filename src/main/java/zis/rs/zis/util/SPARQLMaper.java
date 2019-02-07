package zis.rs.zis.util;

import org.springframework.stereotype.Component;

@Component
public class SPARQLMaper {

    private final String OBRISI_BAZU = "DROP ALL";

    private final String OBRISI_GRAF = "DROP GRAPH <%s>";

    private final String AZURIRANJE_TEMPLEJT = "INSERT DATA { %s }";

    private final String AZURIRANJE_TEMPLEJT_GRAF = "INSERT DATA { GRAPH <%1$s> { %2$s } }";

    private final String SELEKTOVANJE_TEMPLEJT_GRAF = "SELECT * FROM <%1$s> WHERE { %2$s }";

    private final String ZAMENA_TEMPLEJT_GRAF = "WITH <%1$s> DELETE { <%2$s> ?p ?o. } WHERE { <%2$s> ?p ?o.};"
            + "INSERT DATA { GRAPH <%1$s> { %3$s } }";

    private final String VOCABULAR = "PREFIX voc: <http://www.zis.rs/rdf/voc#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";

    private final String ZAMENA_POLJA = VOCABULAR + "WITH <%1$s> DELETE { <%2$s> %3$s ?o. } WHERE { <%2$s> %3$s ?o.};"
            + "INSERT DATA { GRAPH <%1$s> {  <%2$s> %3$s %4$s } }";

    private final String SELEKTOVANJE_FILTER = VOCABULAR + " SELECT DISTINCT  ?s \n" +
            "FROM <http://localhost:3030/Zis/data/zdravstveni_kartoni>\n" +
            "FROM <http://localhost:3030/Zis/data/recepti>\n" +
            "FROM <http://localhost:3030/Zis/data/izvestaji>\n" +
            "FROM <http://localhost:3030/Zis/data/uputi>\n" +
            "FROM <http://localhost:3030/Zis/data/lekari>\n" +
            "FROM <http://localhost:3030/Zis/data/izbori>" +
            "WHERE { %1$s }";

    private final String SELEKTOVANJE_LINKOVA = VOCABULAR +
            "SELECT DISTINCT  ?s\n" +
            "FROM <http://localhost:3030/Zis/data/zdravstveni_kartoni>\n" +
            "FROM <http://localhost:3030/Zis/data/recepti>\n" +
            "FROM <http://localhost:3030/Zis/data/izvestaji>\n" +
            "FROM <http://localhost:3030/Zis/data/uputi>\n" +
            "FROM <http://localhost:3030/Zis/data/lekari>\n" +
            "FROM <http://localhost:3030/Zis/data/izbori>\n" +
            "WHERE {\n" +
            "  ?s ?p <%1$s>\n" +
            "  }";

    public static final String NTRIPLES = "N-TRIPLES";

    public static final String RDF_XML = "RDF/XML";

    public static final String JSON = "JSON";

    public String unesiPodatke(String graphURI, String ntriples) {
        return String.format(AZURIRANJE_TEMPLEJT_GRAF, graphURI, ntriples);
    }

    public String zameniPodatke(String grafURI, String cvorURI, String tripleti) {
        return String.format(ZAMENA_TEMPLEJT_GRAF, grafURI, cvorURI, tripleti);
    }

    public String selektujPodatke(String grafURI, String sparqlUslovi) {
        return String.format(SELEKTOVANJE_TEMPLEJT_GRAF, grafURI, sparqlUslovi);
    }

    public String selektujFilter(String sparqlUslovi) {
        return String.format(SELEKTOVANJE_FILTER, sparqlUslovi);
    }

    public String zameniPolje(String grafURI, String id, String polje, String tripleti) {
        return String.format(ZAMENA_POLJA, grafURI, id, polje, tripleti);
    }

    public String selektujLinkove(String id) {
        return String.format(SELEKTOVANJE_LINKOVA, id);
    }
}
