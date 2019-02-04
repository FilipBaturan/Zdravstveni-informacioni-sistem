package zis.rs.zis.repository.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import zis.rs.zis.util.KonfiguracijaKonekcija;
import zis.rs.zis.util.MetaPodaciEkstraktor;
import zis.rs.zis.util.SPARQLMaper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Repository
public class RDFRepozitorijum {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    @Autowired
    private MetaPodaciEkstraktor ekstraktor;

    public void sacuvaj(String sadrzaj) {

        ByteArrayInputStream rdf = ekstraktor.ekstraktujMetaPodatke(new ByteArrayInputStream(sadrzaj.getBytes()),
                new ByteArrayOutputStream());

        Model model = ModelFactory.createDefaultModel();
        model.read(rdf, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        model.write(out, SPARQLMaper.NTRIPLES);

//        System.out.println("[INFO] Extracted metadata as RDF/XML...");
//        model.write(System.out, SparqlUtil.RDF_XML);


        // Writing the named graph
        //System.out.println("[INFO] Populating named graph \"" + SPARQL_NAMED_GRAPH_URI + "\" with extracted metadata.");
        String sparqlUpit = SPARQLMaper.insertData(konekcija.getDataEndpoint() + "/radnici",
                new String(out.toByteArray()));
        System.out.println(sparqlUpit);

        // UpdateRequest represents a unit of execution
        UpdateRequest update = UpdateFactory.create(sparqlUpit);

        UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, konekcija.getUpdateEndpoint());
        processor.execute();
    }
}