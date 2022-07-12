package com.bluesoftit.cashcoin.Models;

public class DsCatModel {
    private String dsCatId, dsCatName, dsCatIcon;

    public DsCatModel(String dsCatId, String dsCatName, String dsCatIcon) {
        this.dsCatId = dsCatId;
        this.dsCatName = dsCatName;
        this.dsCatIcon = dsCatIcon;
    }
    public DsCatModel(){}

    public String getDsCatId() {
        return dsCatId;
    }

    public void setDsCatId(String dsCatId) {
        this.dsCatId = dsCatId;
    }

    public String getDsCatName() {
        return dsCatName;
    }

    public void setDsCatName(String dsCatName) {
        this.dsCatName = dsCatName;
    }

    public String getDsCatIcon() {
        return dsCatIcon;
    }

    public void setDsCatIcon(String dsCatIcon) {
        this.dsCatIcon = dsCatIcon;
    }
}
