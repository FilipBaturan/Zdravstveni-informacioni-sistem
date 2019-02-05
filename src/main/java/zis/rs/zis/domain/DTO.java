package zis.rs.zis.domain;

import java.util.ArrayList;

public class DTO {

    private ArrayList<String> listaId;

    public DTO()
    {
        this.listaId = new ArrayList<>();
    }

    public DTO(String tekst)
    {
        this.listaId = new ArrayList<>();
        for (String id: tekst.split("-")) {
            this.listaId.add(id);
        }
    }

    public ArrayList<String> getText() {
        return listaId;
    }

    public void setText(ArrayList<String> text) {
        this.listaId = text;
    }
}
