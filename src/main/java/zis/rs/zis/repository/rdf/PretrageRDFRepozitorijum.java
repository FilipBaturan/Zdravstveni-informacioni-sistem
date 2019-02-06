package zis.rs.zis.repository.rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import zis.rs.zis.domain.ParametarPretrage;
import zis.rs.zis.domain.UpitPretrage;
import zis.rs.zis.util.*;

import java.util.Iterator;

@Repository
public class PretrageRDFRepozitorijum extends IOStrimer {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private Maper maper;

    @Autowired
    private SPARQLMaper sparqlMaper;

    public PretrageRDFRepozitorijum() {
    }

    public String opstiUpit(UpitPretrage upitPretrage) {

//        String sparqlQuery = sparqlMaper.selectData(konekcija.getDataEndpoint() + "/"
//                + maper.dobaviGraf("recepti"), "?s ?p ?o");
        String uslovi = konstruisiUpit(upitPretrage);

        String sparqlQuery = sparqlMaper.selectFilter(uslovi);

        // Create a QueryExecution that will access a SPARQL service over HTTP
        QueryExecution query = QueryExecutionFactory.sparqlService(konekcija.getQueryEndpoint(), sparqlQuery);

        // Query the SPARQL endpoint, iterate over the result set...
        ResultSet results = query.execSelect();

        String varName;
        RDFNode varValue;

        while (results.hasNext()) {

            // A single answer from a SELECT query
            QuerySolution querySolution = results.next();
            Iterator<String> variableBindings = querySolution.varNames();

            // Retrieve variable bindings
            while (variableBindings.hasNext()) {

                varName = variableBindings.next();
                varValue = querySolution.get(varName);

                System.out.println(varName + ": " + varValue);
            }
            System.out.println();
        }

//        // Querying the other named graph
//        System.out.println("[INFO] Selecting the triples from the named graph \"" + PERSON_NAMED_GRAPH_URI + "\".");
//        sparqlQuery = SparqlUtil.selectData(conn.dataEndpoint + PERSON_NAMED_GRAPH_URI, "?s ?p ?o");
//
//        // Create a QueryExecution that will access a SPARQL service over HTTP
//        query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
//
//
//        // Query the collection, dump output response as XML
//        results = query.execSelect();

        ResultSetFormatter.outputAsJSON(System.out, results);

        query.close();

        System.out.println("[INFO] End.");


//        String sparqlFilePath = "data/sparql/query1.rq";
//
//        // Querying the named graph with a referenced SPARQL query
//        System.out.println("[INFO] Loading SPARQL query from file \"" + sparqlFilePath + "\"");
//        String sparqlQuery = String.format(,
//                konekcija.getDataEndpoint() + SPARQL_NAMED_GRAPH_URI);
//
//        System.out.println(sparqlQuery);
//
//        // Create a QueryExecution that will access a SPARQL service over HTTP
//        QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
//
//        // Query the SPARQL endpoint, iterate over the result set...
//        System.out.println("[INFO] Showing the results for SPARQL query using the result handler.\n");
//        ResultSet results = query.execSelect();
//
//        String varName;
//        RDFNode varValue;
//
//        while (results.hasNext()) {
//
//            // A single answer from a SELECT query
//            QuerySolution querySolution = results.next();
//            Iterator<String> variableBindings = querySolution.varNames();
//
//            // Retrieve variable bindings
//            while (variableBindings.hasNext()) {
//
//                varName = variableBindings.next();
//                varValue = querySolution.get(varName);
//
//                System.out.println(varName + ": " + varValue);
//            }
//            System.out.println();
//        }
//
//        // Issuing the same query once again...
//
//        // Create a QueryExecution that will access a SPARQL service over HTTP
//        query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
//
//        // Query the collection, dump output response as XML
//        System.out.println("[INFO] Showing the results for SPARQL query in native SPARQL XML format.\n");
//        results = query.execSelect();
//
//        //ResultSetFormatter.outputAsXML(System.out, results);
//        ResultSetFormatter.out(System.out, results);
//
//        query.close();
//
//        System.out.println("[INFO] End.");

        return "smece";

    }

    private String konstruisiUpit(UpitPretrage upitPretrage) {
        StringBuilder sb = new StringBuilder();
        boolean obradjenPrvi = false;
        int brojUpita = upitPretrage.getParametriPretrage().size();
        String operator;
        int j = 0;
        if (brojUpita == 1) {
            sb.append("{");
        } else {
            for (int i = 0; i < brojUpita - 1; i++) {
                sb.append("{");
            }
        }
        for (ParametarPretrage parametar :
                upitPretrage.getParametriPretrage()) {
            sb.append("{ ");
            sb.append(" ?s ");
            sb.append("voc:");
            sb.append(parametar.getImeAtributa());
            sb.append(" \"");
            if (parametar.getImeAtributa().equals("datum") ||
                parametar.getImeAtributa().equals("datumRodjenja")) {
                sb.append(parametar.getVrednost());
                sb.append("\"^^xsd:date");
            } else {
                sb.append(parametar.getVrednost());
                sb.append("\"");
            }


            if (!obradjenPrvi) {
                sb.append("} ");
                obradjenPrvi = true;
            } else {
                sb.append("}} ");
            }
            if (parametar.getOperator().equals("AND")) {
                operator = ". ";
            } else if (parametar.getOperator().equals("OR")) {
                operator = " UNION ";
            } else {
                throw new ValidacioniIzuzetak("Nevalidno prosledjen operator!");
            }
            if (j != brojUpita - 1) {
                sb.append(operator);
            }

            sb.append(" ");
            ++j;
        }
        return sb.toString();
        //return rezultat.substring(0, rezultat.length() - duzinaParametra + 1) + "}";
    }
}
