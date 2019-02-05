package zis.rs.zis.domain;

public class ParametarPretrage {

    private String imeAtributa;
    private String vrednost;
    private String operator;

    public String getImeAtributa() {
        return imeAtributa;
    }

    public void setImeAtributa(String imeAtributa) {
        this.imeAtributa = imeAtributa;
    }

    public String getVrednost() {
        return vrednost;
    }

    public void setVrednost(String vrednost) {
        this.vrednost = vrednost;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ParametarPretrage() {
    }

    public ParametarPretrage(String imeAtributa, String vrednost, String operator) {
        this.imeAtributa = imeAtributa;
        this.vrednost = vrednost;
        this.operator = operator;
    }
}
