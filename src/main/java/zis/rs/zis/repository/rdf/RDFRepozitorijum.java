package zis.rs.zis.repository.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import zis.rs.zis.util.KonfiguracijaKonekcija;
import zis.rs.zis.util.Maper;
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

    @Autowired
    private Maper maper;

    public void sacuvaj(String sadrzaj, String graf, boolean postProcesiranje) {

        ByteArrayInputStream rdf = ekstraktor.ekstraktujMetaPodatke(new ByteArrayInputStream(sadrzaj.getBytes()),
                new ByteArrayOutputStream());
        Model model = ModelFactory.createDefaultModel();
        model.read(rdf, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (postProcesiranje) {
            this.postProcesiranje(model, sadrzaj);
        }
        model.write(output, SPARQLMaper.NTRIPLES);
        String sparqlUpit = SPARQLMaper.insertData(konekcija.getDataEndpoint() + "/" + graf,
                new String(output.toByteArray()));
        System.out.println(sparqlUpit);
        UpdateRequest update = UpdateFactory.create(sparqlUpit);
        UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, konekcija.getUpdateEndpoint());
        processor.execute();
    }

    private void postProcesiranje(Model model, String karton) {
        Document dok = maper.konvertujUDokument(karton);
        String id = dok.getFirstChild().getAttributes().getNamedItem("id").getNodeValue();
        String jmbg = dok.getFirstChild().getAttributes().getNamedItem("jmbg").getNodeValue();
        String voc = maper.dobaviURI("vokabular"); //"http://somewhere/else#";
        Resource koren =  model.getResource(id);

        //String nsB = "http://nowhere/else#";
        //Resource root = model.createResource( voc + "root" );
        Property jm = model.createProperty( voc +  "jmbg");
        koren.addProperty(jm, jmbg);
//        Property lbo = model.createProperty( nsB + "Q" );
//        Resource x = model.createResource( nsA + "x" );
//        Resource y = model.createResource( nsA + "y" );
//        Resource z = model.createResource( nsA + "z" );
//        model.add( root, P, x ).add( root, P, y ).add( y, Q, z );
//        System.out.println( "# -- no special prefixes defined" );
//        model.write( System.out );
//        System.out.println( "# -- nsA defined" );
//        model.setNsPrefix( "nsA", nsA );
//        model.write( System.out );
//        System.out.println( "# -- nsA and cat defined" );
//        model.setNsPrefix( "cat", nsB );
        model.write( System.out );
    }
}