package zis.rs.zis.repository.rdf;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
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

        String uslovi = konstruisiUpit(upitPretrage);

        String sparqlUpit = sparqlMaper.selektujFilter(uslovi);

        // Create a QueryExecution that will access a SPARQL service over HTTP
        QueryExecution upit = QueryExecutionFactory.sparqlService(konekcija.getQueryEndpoint(), sparqlUpit);

        // Query the SPARQL endpoint, iterate over the result set...
        ResultSet rezultati = upit.execSelect();

        String naziv;
        RDFNode vrednost;

        StringBuilder rez = new StringBuilder();

        while (rezultati.hasNext()) {

            // A single answer from a SELECT query
            QuerySolution rezultat = rezultati.next();
            Iterator<String> bajdinzi = rezultat.varNames();

            // Retrieve variable bindings
            while (bajdinzi.hasNext()) {

                naziv = bajdinzi.next();
                vrednost = rezultat.get(naziv);
                rez.append(vrednost.toString());
                rez.append("-");
                System.out.println(naziv + ": " + vrednost);
            }
            System.out.println();
        }

        rez.deleteCharAt(rez.length() - 1);

        upit.close();

        return rez.toString();

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
    }
}
