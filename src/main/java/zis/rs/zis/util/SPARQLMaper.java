package zis.rs.zis.util;

import org.springframework.stereotype.Component;

@Component
public class SPARQLMaper {

    /* The following operation causes all of the triples in all of the graphs to be deleted */
    private final String DROP_ALL = "DROP ALL";

    /* Removes all of the triples from a named graphed */
    private final String DROP_GRAPH_TEMPLATE = "DROP GRAPH <%s>";

    /**
     * A template for creating SPARUL (SPARQL Update) query can be found here:
     * https://www.w3.org/TR/sparql11-update/
     */
    /* Insert RDF data into the default graph */
    private final String UPDATE_TEMPLATE = "INSERT DATA { %s }";

    /* Insert RDF data to an arbitrary named graph */
    private final String UPDATE_TEMPLATE_NAMED_GRAPH = "INSERT DATA { GRAPH <%1$s> { %2$s } }";


    /* Simple SPARQL query on a named graph */
    private final String SELECT_NAMED_GRAPH_TEMPLATE = "SELECT * FROM <%1$s> WHERE { %2$s }";

    private final String REPLACE_NAMED_GRAPH_TEMPLATE = "WITH <%1$s> DELETE { <%2$s> ?p ?o. } WHERE { <%2$s> ?p ?o.};"
            + "INSERT DATA { GRAPH <%1$s> { %3$s } }";

    private final String VOCABULARY = "PREFIX voc: <http://www.zis.rs/rdf/voc#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";

    private final String SELECT_FILTER = VOCABULARY + " SELECT DISTINCT  ?s \n" +
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

    public String dropAll() {
        return DROP_ALL;
    }

    public String dropGraph(String graphURI) {
        return String.format(DROP_GRAPH_TEMPLATE, graphURI);
    }

    /* Inserts data to the default graph */
    public String insertData(String ntriples) {
        return String.format(UPDATE_TEMPLATE, ntriples);
    }

    public String insertData(String graphURI, String ntriples) {
        return String.format(UPDATE_TEMPLATE_NAMED_GRAPH, graphURI, ntriples);
    }

    public String replaceData(String graphURI, String nodeURI, String ntriples) {
        return String.format(REPLACE_NAMED_GRAPH_TEMPLATE, graphURI, nodeURI, ntriples);
    }

    public String selectData(String graphURI, String sparqlCondition) {
        return String.format(SELECT_NAMED_GRAPH_TEMPLATE, graphURI, sparqlCondition);
    }

    public String selectFilter(String sparqlCondition) {
        return String.format(SELECT_FILTER, sparqlCondition);
    }

}
