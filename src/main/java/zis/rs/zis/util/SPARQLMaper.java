package zis.rs.zis.util;

import org.springframework.stereotype.Component;

@Component
public class SPARQLMaper {

    /* The following operation causes all of the triples in all of the graphs to be deleted */
    private final String OBRISI_BAZU = "DROP ALL";

    /* Removes all of the triples from a named graphed */
    private final String OBRISI_GRAF = "DROP GRAPH <%s>";

    /**
     * A template for creating SPARUL (SPARQL Update) query can be found here:
     * https://www.w3.org/TR/sparql11-update/
     */
    /* Insert RDF data into the default graph */
    private final String AZURIRANJE_TEMPLEJT = "INSERT DATA { %s }";

    /* Insert RDF data to an arbitrary named graph */
    private final String AZURIRANJE_TEMPLEJT_GRAF = "INSERT DATA { GRAPH <%1$s> { %2$s } }";


    /* Simple SPARQL query on a named graph */
    private final String SELEKTOVANJE_TEMPLEJT_GRAF = "SELECT * FROM <%1$s> WHERE { %2$s }";

    private final String ZAMENA_TEMPLEJT_GRAF = "WITH <%1$s> DELETE { <%2$s> ?p ?o. } WHERE { <%2$s> ?p ?o.};"
            + "INSERT DATA { GRAPH <%1$s> { %3$s } }";

    private final String VOCABULAR = "PREFIX voc: <http://www.zis.rs/rdf/voc#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";

    private final String ZAMENA_POLJA = VOCABULAR + "WITH <%1$s> DELETE { <%2$s> %3$s ?o. } WHERE { <%2$s> %3$s ?o.};"
            + "INSERT DATA { GRAPH <%1$s> {  <%2$s> %3$s %4$s } }";

    private final String SELECTOVANJE_FILTER = VOCABULAR + " SELECT DISTINCT  ?s \n" +
            "FROM <http://localhost:3030/Zis/data/zdravstveni_kartoni>\n" +
            "FROM <http://localhost:3030/Zis/data/recepti>\n" +
            "FROM <http://localhost:3030/Zis/data/izvestaji>\n" +
            "FROM <http://localhost:3030/Zis/data/uputi>\n" +
            "FROM <http://localhost:3030/Zis/data/lekari>\n" +
            "FROM <http://localhost:3030/Zis/data/izbori>" +
            "WHERE { %1$s }";

    /* Plain text RDF serialization format */
    public static final String NTRIPLES = "N-TRIPLES";

    /* An XML serialization format for RDF data */
    public static final String RDF_XML = "RDF/XML";

    public static final String JSON = "JSON";

    public String unesiPodatke(String graphURI, String ntriples) {
        return String.format(AZURIRANJE_TEMPLEJT_GRAF, graphURI, ntriples);
    }

    public String zameniPodatke(String graphURI, String nodeURI, String ntriples) {
        return String.format(ZAMENA_TEMPLEJT_GRAF, graphURI, nodeURI, ntriples);
    }

    public String selektujPodatke(String graphURI, String sparqlCondition) {
        return String.format(SELEKTOVANJE_TEMPLEJT_GRAF, graphURI, sparqlCondition);
    }

    public String selektujFilter(String sparqlCondition) {
        return String.format(SELECTOVANJE_FILTER, sparqlCondition);
    }

    public String zameniPolje(String graphURI, String id, String field, String ntriples) {
        return String.format(ZAMENA_POLJA, graphURI, id, field, ntriples);
    }

}
