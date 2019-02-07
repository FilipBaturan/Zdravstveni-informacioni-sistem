package zis.rs.zis.domain.DTO;

import java.util.ArrayList;
import java.util.Arrays;

public class RezultatPretrage {

    private ArrayList<String> listaId;

    public RezultatPretrage() {
        this.listaId = new ArrayList<>();
    }

    public RezultatPretrage(String tekst) {
        this.listaId = new ArrayList<>();
        this.listaId.addAll(Arrays.asList(tekst.split("-")));
    }

    public ArrayList<String> getText() {
        return listaId;
    }

    public void setText(ArrayList<String> text) {
        this.listaId = text;
    }
}
