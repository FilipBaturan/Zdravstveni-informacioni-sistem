package zis.rs.zis.util;

import org.exist.xmldb.EXistResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.transform.OutputKeys;

@Configuration
@ConfigurationProperties(prefix = "conn")
public class KonfiguracijaKonekcija {

    private String connectionUri = "xmldb:exist://%1$s:%2$s/exist/xmlrpc";

    private String host;
    private int port;
    private String user;
    private String password;
    private String driver;
    private String endpoint;
    private String dataset;
    private String query;
    private String update;
    private String data;

    public ResursiBaze uspostaviKonekciju(String kolekcijaId, String dokumentId) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, XMLDBException {
        System.out.println("[INFO] Loading driver class: " + this.getDriver());
        Class<?> cl = Class.forName(this.getDriver());

        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        DatabaseManager.registerDatabase(database);

        Collection kolekcija = null;
        XMLResource resurs = null;

        try {
            kolekcija = DatabaseManager.getCollection(this.getUri() + kolekcijaId);
            kolekcija.setProperty(OutputKeys.INDENT, "yes");

            resurs = (XMLResource) kolekcija.getResource(dokumentId);

            if (resurs == null) {
                this.oslobodiResurse(kolekcija, null);
                throw new XMLDBException();
            } else {
                return new ResursiBaze(kolekcija, resurs);
            }

        } finally {
            this.oslobodiResurse(kolekcija, resurs);
        }
    }

    private void oslobodiResurse(Collection kolekcija, XMLResource resurs) {
        if (resurs != null) {
            try {
                ((EXistResource) resurs).freeResources();
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }

        if (kolekcija != null) {
            try {
                kolekcija.close();
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }
    }

    public void oslobodiResurse(ResursiBaze resursi) {
        if (resursi != null) {
            this.oslobodiResurse(resursi.getKolekcija(), resursi.getXmlResurs());
        }
    }

    public String getConnectionUri() {
        return connectionUri;
    }

    public void setConnectionUri(String connectionUri) {
        this.connectionUri = connectionUri;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUri() {
        return String.format(this.connectionUri, this.host, this.port);
    }
}
