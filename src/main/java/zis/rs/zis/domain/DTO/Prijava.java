package zis.rs.zis.domain.DTO;

public class Prijava {

    private String id;
    private String tip;

    public Prijava() {
    }

    public Prijava(String token) {
        String[] tokeni = token.split("-");
        this.id = tokeni[0];
        this.tip = tokeni[1];
    }

    public Prijava(String id, String tip) {
        this.id = id;
        this.tip = tip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
