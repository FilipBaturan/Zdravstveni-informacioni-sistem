package zis.rs.zis.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zis.rs.zis.domain.enums.TipKorisnika;

import javax.print.Doc;

@Component
public class GeneratorMetaPodataka {

    /**
     * @param dokument nad kojem se dodaju metapodaci korisnika
     */
    public void dodajMetaPodatkeKorisniku(Document dokument) {
        NodeList elementi = dokument.getFirstChild().getChildNodes();
        Element element;
        int count = 0;
        for (int i = 0; i < elementi.getLength() && count < 3; i++) {
            element = (Element) elementi.item(i);
            switch (element.getLocalName()) {
                case "ime":
                    element.setAttribute("property", "voc:ime");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
                case "prezime":
                    element.setAttribute("property", "voc:prezime");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
                case "jmbg":
                    element.setAttribute("property", "voc:jmbg");
                    element.setAttribute("datatype", "xs:string");
                    ++count;
                    break;
            }
        }
    }

    /**
     * @param dokument     nad kojem se dodaju metapodaci osobe
     * @param tipKorisnika tip korisnika
     */
    public void dodajMetaPodatkeOsobi(Document dokument, TipKorisnika tipKorisnika, String id) {
        NodeList elementi = dokument.getFirstChild().getChildNodes();
        Element koren = ((Element) dokument.getFirstChild());
        Element element;
        int count = 0;
        switch (tipKorisnika) {
            case LEKAR:
                koren.setAttribute("about", id);
                koren.setAttribute("typeof", "voc:Lekar");
                for (int i = 0; i < elementi.getLength() && count < 2; i++) {
                    element = (Element) elementi.item(i);
                    switch (element.getLocalName()) {
                        case "tip":
                            element.setAttribute("property", "voc:tipLekara");
                            element.setAttribute("datatype", "xs:string");
                            ++count;
                            break;
                        case "oblast_zastite":
                            element.setAttribute("property", "voc:oblastZastite");
                            element.setAttribute("datatype", "xs:string");
                            ++count;
                            break;
                        case "broj_pacijenata":
                            element.setAttribute("property", "voc:brojPacijenata");
                            element.setAttribute("datatype", "xs:integer");
                            ++count;
                            break;
                    }
                }
                break;
            case PACIJENT:
                koren.setAttribute("about", id);
                koren.setAttribute("typeof", "voc:ZdrastveniKarton");
                for (int i = 0; i < elementi.getLength() && count < 2; i++) {
                    element = (Element) elementi.item(i);
                    switch (element.getLocalName()) {
                        case "osigurano_lice":
                            this.dodajMetaPodatkePacijentu(element.getChildNodes(), id);
                            ++count;
                            break;
                        case "odabrani_lekar":
                            String lekarId = element.getAttributes().item(0).getNodeValue();
                            element.setAttribute("rel", "voc:odabraniLekar");
                            element.setAttribute("href", lekarId);
                            ++count;
                            break;
                    }
                }
                break;
        }
    }

    /**
     * @param elementi kojima je potrebno dodati meta podatke
     * @param id       zdravstvenog kartona
     */
    private void dodajMetaPodatkePacijentu(NodeList elementi, String id) {
        String[] urls = this.generisiURL(elementi);
        String adresaURL = urls[0];
        String opstinaURL = urls[1];
        int brojac = 0;
        Element element;
        for (int i = 0; i < elementi.getLength() && brojac < 6; i++) {
            try {
                element = (Element) elementi.item(i);
            } catch (ClassCastException e) {
                break;
            }
            switch (element.getLocalName()) {
                case "ime":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:ime");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "prezime":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:prezime");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "jmbg":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:jmbg");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "pol":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:pol");
                    element.setAttribute("datatype", "xs:string");
                    ++brojac;
                    break;
                case "datum_rodjenja":
                    element.setAttribute("about", id);
                    element.setAttribute("property", "voc:datum_rodjenja");
                    element.setAttribute("datatype", "xs:date");
                    ++brojac;
                    break;
                case "adresa":
                    element.setAttribute("about", id);
                    element.setAttribute("rel", "voc:stanuje");
                    element.setAttribute("href", adresaURL);
                    ++brojac;
                    NodeList adresa = element.getChildNodes();
                    Element el;
                    int adresaBrojac = 0;
                    for (int j = 0; j < adresa.getLength() && adresaBrojac < 5; j++) {
                        el = (Element) adresa.item(j);
                        switch (el.getLocalName()) {
                            case "ulica":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:ulica");
                                el.setAttribute("datatype", "xs:string");
                                ++adresaBrojac;
                                break;
                            case "broj":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:broj");
                                el.setAttribute("datatype", "xs:integer");
                                ++adresaBrojac;
                                break;
                            case "broj_stana":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:brojStana");
                                el.setAttribute("datatype", "xs:integer");
                                ++adresaBrojac;
                                break;
                            case "mesto":
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("property", "voc:mesto");
                                el.setAttribute("datatype", "xs:string");
                                ++adresaBrojac;
                                break;
                            case "opstina":
                                ++adresaBrojac;
                                el.setAttribute("about", adresaURL);
                                el.setAttribute("rel", "voc:opstina");
                                el.setAttribute("href", opstinaURL);
                                NodeList opstina = el.getChildNodes();
                                Element e;
                                int opstinaBrojac = 0;
                                for (int k = 0; k < opstina.getLength() && opstinaBrojac < 2; k++) {
                                    e = (Element) opstina.item(k);
                                    switch (e.getLocalName()) {
                                        case "naziv":
                                            e.setAttribute("about", opstinaURL);
                                            e.setAttribute("property", "voc:nazivOpstine");
                                            e.setAttribute("datatype", "xs:string");
                                            ++opstinaBrojac;
                                            break;
                                        case "postanski_broj":
                                            e.setAttribute("about", opstinaURL);
                                            e.setAttribute("property", "voc:postanskiBroj");
                                            e.setAttribute("datatype", "xs:integer");
                                            ++opstinaBrojac;
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    }


    /**
     * @param elementi nad kojem se dodaju metapodaci leku
     */
    public NodeList dodajMetaPodatkeLeku(NodeList elementi, String id)
    {
        Element element;
        int count = 0;
        for (int i = 0; i < elementi.getLength() && count < 1; i++) {
            if ( !elementi.item(i).getTextContent().equals("\n")) {
                element = (Element) elementi.item(i);
                switch (element.getTagName().split(":")[1]) {
                    case "naziv":
                        element.setAttribute("property", "voc:naziv");
                        element.setAttribute("datatype", "xs:string");
                        ++count;
                        break;
                }
            }
        }
        return elementi;
    }

    /**
     * @param elementi nad kojem se dodaju metapodaci izvestaju
     */
    public NodeList dodajMetaPodatkeIzvestaju(NodeList elementi, String id)
    {
        Element element;
        int brojac = 0;
        for (int i = 0; i < elementi.getLength() && brojac < 3; i++) {
            if ( !elementi.item(i).getTextContent().equals("\n")) {
                element = (Element) elementi.item(i);
                switch (element.getTagName().split(":")[1]) {
                    case "datum":
                        Element roditelj = (Element) element.getParentNode();
                        roditelj.setAttribute("about", id);
                        element.setAttribute("property", "voc:datum");
                        element.setAttribute("datatype", "xs:date");
                        ++brojac;
                        break;
                    case "osigurano_lice":
                        String liceId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:osiguranoLice");
                        element.setAttribute("href", liceId);
                        ++brojac;
                        break;
                    case "lekar":
                        String lekarId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:lekar");
                        element.setAttribute("href", lekarId);
                        ++brojac;
                        break;
                }
            }
        }
        return elementi;
    }

    /**
     * @param elementi nad kojima se dodaju metapodaci uputu
     */
    public NodeList dodajMetaPodatkeUputu(NodeList elementi, String id)
    {
        Element element;
        int brojac = 0;
        for (int i = 0; i < elementi.getLength() && brojac < 4; i++) {
            if ( !elementi.item(i).getTextContent().equals("\n")) {
                element = (Element) elementi.item(i);
                switch (element.getTagName().split(":")[1]) {
                    case "datum":
                        Element roditelj = (Element) element.getParentNode();
                        roditelj.setAttribute("about", id);
                        element.setAttribute("property", "voc:datum");
                        element.setAttribute("datatype", "xs:date");
                        ++brojac;
                        break;
                    case "osigurano_lice":
                        String liceId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:osiguranoLice");
                        element.setAttribute("href", liceId);
                        ++brojac;
                        break;
                    case "lekar":
                        String lekarId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:lekar");
                        element.setAttribute("href", lekarId);
                        ++brojac;
                        break;
                    case "specialista":
                        String specId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:specijalista");
                        element.setAttribute("href", specId);
                        ++brojac;
                        break;
                }
            }
        }
        return elementi;
    }

    public NodeList dodajMetaPodatkeReceptu(NodeList elementi, String id)
    {
        Element element;
        int brojac = 0;
        for (int i = 0; i < elementi.getLength() && brojac < 4; i++) {
            if ( !elementi.item(i).getTextContent().equals("\n")) {
                element = (Element) elementi.item(i);
                switch (element.getTagName().split(":")[1]) {
                    case "datum":
                        Element roditelj = (Element) element.getParentNode();
                        roditelj.setAttribute("about", id);
                        element.setAttribute("property", "voc:datum");
                        element.setAttribute("datatype", "xs:date");
                        ++brojac;
                        break;
                    case "osigurano_lice":
                        String liceId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:osiguranoLice");
                        element.setAttribute("href", liceId);
                        ++brojac;
                        break;
                    case "lekar":
                        String lekarId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:lekar");
                        element.setAttribute("href", lekarId);
                        ++brojac;
                        break;
                    case "propisani_lek":
                        String lekId = element.getAttributes().item(0).getNodeValue();
                        element.setAttribute("rel", "voc:lek");
                        element.setAttribute("href", lekId);
                        ++brojac;
                        break;
                }
            }
        }
        return elementi;
    }


    /**
     * @param elementi lista elemenata kojoj je potrebno izgenerisati
     *                 URL da ne bi bili prazni cvorovi
     * @return niz generisanih URL
     */
    private String[] generisiURL(NodeList elementi) {
        StringBuilder sba = new StringBuilder("http://www.zis.rs/adresa/");
        StringBuilder sbo = new StringBuilder("http://www.zis.rs/opstina/");
        for (int i = 0; i < elementi.getLength(); i++) {
            Element element;
            try {
                element = (Element) elementi.item(i);
            } catch (ClassCastException e) {
                break;
            }
            if (element.getLocalName().equals("adresa")) {
                NodeList adresa = element.getChildNodes();
                int adresaBrojac = 0;
                for (int j = 0; j < adresa.getLength() && adresaBrojac < 5; j++) {
                    switch (adresa.item(j).getLocalName()) {
                        case "ulica":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "broj":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "broj_stana":
                            sba.append(adresa.item(j).getTextContent());
                            sba.append("/");
                            ++adresaBrojac;
                            break;
                        case "mesto":
                            sba.append(adresa.item(j).getTextContent());
                            ++adresaBrojac;
                            break;
                        case "opstina":
                            ++adresaBrojac;
                            NodeList opstina = adresa.item(j).getChildNodes();
                            int opstinaBrojac = 0;
                            for (int k = 0; k < opstina.getLength() && opstinaBrojac < 2; k++) {
                                switch (opstina.item(k).getLocalName()) {
                                    case "naziv":
                                        sbo.append(opstina.item(k).getTextContent());
                                        sbo.append("/");
                                        ++opstinaBrojac;
                                        break;
                                    case "postanski_broj":
                                        sbo.append(opstina.item(k).getTextContent());
                                        ++opstinaBrojac;
                                        break;
                                }
                            }
                            break;
                    }
                }
            }
        }
        return new String[]{sba.toString(), sbo.toString()};
    }


}
