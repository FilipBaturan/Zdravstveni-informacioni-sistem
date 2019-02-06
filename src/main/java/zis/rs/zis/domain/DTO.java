package zis.rs.zis.domain;

import java.util.ArrayList;
import java.util.Arrays;

public class DTO {

    private ArrayList<String> listaId;

    public DTO()
    {
        this.listaId = new ArrayList<>();
    }

    public DTO(String tekst)
    {
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
