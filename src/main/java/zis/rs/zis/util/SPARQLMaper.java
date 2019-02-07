package zis.rs.zis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SPARQLMaper {

    @Autowired
    private KonfiguracijaKonekcija konekcija;

    public SPARQLMaper() {

    }

    @PostConstruct
    public void init() {
        this.prefiks = konekcija.getDataEndpoint() + "/";
        SELEKTOVANJE_FILTER = VOCABULAR + " SELECT DISTINCT  ?s \n" +
                "FROM <" + this.dobaviPrefiks() + "zdravstveni_kartoni>\n" +
                "FROM <" + this.dobaviPrefiks() + "recepti>\n" +
                "FROM <" + this.dobaviPrefiks() + "izvestaji>\n" +
                "FROM <" + this.dobaviPrefiks() + "uputi>\n" +
                "FROM <" + this.dobaviPrefiks() + "lekari>\n" +
                "FROM <" + this.dobaviPrefiks() + "izbori>" +
                "WHERE { %1$s }";
        SELEKTOVANJE_LINKOVA = VOCABULAR +
                "SELECT DISTINCT  ?s\n" +
                "FROM <" + this.dobaviPrefiks() + "zdravstveni_kartoni>\n" +
                "FROM <" + this.dobaviPrefiks() + "recepti>\n" +
                "FROM <" + this.dobaviPrefiks() + "izvestaji>\n" +
                "FROM <" + this.dobaviPrefiks() + "uputi>\n" +
                "FROM <" + this.dobaviPrefiks() + "lekari>\n" +
                "FROM <" + this.dobaviPrefiks() + "izbori>" +
                "WHERE {\n" +
                "  ?s ?p <%1$s>\n" +
                "  }";
        BRISANJE_TEMPLEJT_GRAF = VOCABULAR +
                "WITH <%1$s> DELETE { <%2$s> ?p ?o. } WHERE { <%2$s> ?p ?o.};\n" +
                "WITH <" + this.dobaviPrefiks() + "zdravstveni_kartoni> DELETE { ?s ?p <%2$s>. } WHERE { ?s ?p <%2$s>.};\n" +
                "WITH <" + this.dobaviPrefiks() + "recepti> DELETE { ?s ?p <%2$s>. } WHERE { ?s ?p <%2$s>.};\n" +
                "WITH <" + this.dobaviPrefiks() + "izvestaji> DELETE { ?s ?p <%2$s>. } WHERE { ?s ?p <%2$s>.};\n" +
                "WITH <" + this.dobaviPrefiks() + "uputi> DELETE { ?s ?p <%2$s>. } WHERE { ?s ?p <%2$s>.};\n" +
                "WITH <" + this.dobaviPrefiks() + "izbori> DELETE { ?s ?p <%2$s>. } WHERE { ?s ?p <%2$s>.};\n" ;
    }

    private String prefiks;

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

    private String SELEKTOVANJE_FILTER;

    private String SELEKTOVANJE_LINKOVA;

    private String BRISANJE_TEMPLEJT_GRAF;

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

    public String brisanjePodataka(String grafURI, String cvorURI) {
        return String.format(BRISANJE_TEMPLEJT_GRAF, grafURI, cvorURI);
    }

    public String dobaviPrefiks() {
        if (this.prefiks == null) {
            this.prefiks = konekcija.getDataEndpoint() + "/";
        }
        return this.prefiks;
    }
}
