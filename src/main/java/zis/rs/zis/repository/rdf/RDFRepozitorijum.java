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

    @Autowired
    private SPARQLMaper sparqlMaper;

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
        String sparqlUpit = sparqlMaper.unesiPodatke(konekcija.getDataEndpoint() + "/" + graf,
                new String(output.toByteArray()));
        System.out.println(sparqlUpit);
        UpdateRequest izmena = UpdateFactory.create(sparqlUpit);
        UpdateProcessor procesor = UpdateExecutionFactory.createRemote(izmena, konekcija.getUpdateEndpoint());
        procesor.execute();
    }

    public void izmeni(String sadrzaj, String graf, boolean postProcesiranje) {

        ByteArrayInputStream rdf = ekstraktor.ekstraktujMetaPodatke(new ByteArrayInputStream(sadrzaj.getBytes()),
                new ByteArrayOutputStream());
        Model model = ModelFactory.createDefaultModel();
        model.read(rdf, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (postProcesiranje) {
            this.postProcesiranje(model, sadrzaj);
        }
        model.write(output, SPARQLMaper.NTRIPLES);
        String sparqlUpit = sparqlMaper.zameniPodatke(konekcija.getDataEndpoint() + "/" + graf,
                dobaviId(sadrzaj),new String(output.toByteArray()));
        System.out.println(sparqlUpit);
        UpdateRequest izmena = UpdateFactory.create(sparqlUpit);
        UpdateProcessor procesor = UpdateExecutionFactory.createRemote(izmena, konekcija.getUpdateEndpoint());
        procesor.execute();
    }

    public void izmeniPoljeUKartonu(String karton , String polje, String vrednost) {
        String sparqlUpit = sparqlMaper.zameniPolje(konekcija.getDataEndpoint() + "/" +
                maper.dobaviGraf("zdravstveni_kartoni"), karton, polje , vrednost);
        System.out.println(sparqlUpit);
        UpdateRequest izmena = UpdateFactory.create(sparqlUpit);
        UpdateProcessor procesor = UpdateExecutionFactory.createRemote(izmena, konekcija.getUpdateEndpoint());
        procesor.execute();
    }

    private void postProcesiranje(Model model, String karton) {
        Document dok = maper.konvertujUDokument(karton);
        String id = dok.getFirstChild().getAttributes().getNamedItem("id").getNodeValue();
        String jmbg = dok.getFirstChild().getAttributes().getNamedItem("jmbg").getNodeValue();
        String lbo = dok.getFirstChild().getAttributes().getNamedItem("lbo").getNodeValue();
        String br_knjizice = dok.getFirstChild().getAttributes().getNamedItem("broj_zdr_knjizice").getNodeValue();
        String br_kartona = dok.getFirstChild().getAttributes().getNamedItem("broj_kartona").getNodeValue();
        String voc = maper.dobaviURI("vokabular");
        Resource koren =  model.getResource(id);

        Property jm = model.createProperty( voc +  "jmbg");
        Property lb = model.createProperty( voc +  "lbo");
        Property br_kn = model.createProperty( voc +  "broj_zdr_knjizice");
        Property br_kr = model.createProperty( voc +  "broj_kartona");
        koren.addProperty(jm, jmbg);
        koren.addProperty(lb, lbo);
        koren.addProperty(br_kn, br_knjizice);
        koren.addProperty(br_kr, br_kartona);
    }

    private String dobaviId(String sadrzaj) {
        Document dok = maper.konvertujUDokument(sadrzaj);
        return dok.getFirstChild().getAttributes().getNamedItem("id").getNodeValue();
    }
}