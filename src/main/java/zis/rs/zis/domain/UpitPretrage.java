package zis.rs.zis.domain;

import java.util.ArrayList;

public class UpitPretrage {

    private ArrayList<ParametarPretrage> parametriPretrage;

    public ArrayList<ParametarPretrage> getParametriPretrage() {
        return parametriPretrage;
    }

    public void setParametriPretrage(ArrayList<ParametarPretrage> parametriPretrage) {
        this.parametriPretrage = parametriPretrage;
    }

    public UpitPretrage() {
    }

    public UpitPretrage(ArrayList<ParametarPretrage> parametriPretrage) {
        this.parametriPretrage = parametriPretrage;
    }
}
