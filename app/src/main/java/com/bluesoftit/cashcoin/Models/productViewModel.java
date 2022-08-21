package com.bluesoftit.cashcoin.Models;

public class productViewModel {
    private String pName, pBrif,pImg;
    private int pPrice;
    private int id;



    public productViewModel(String pName, String pBrif,String pImg, int pPrice, int id) {
        this.pName = pName;
        this.pBrif = pBrif;
        this.pImg = pImg;
        this.pPrice = pPrice;
        this.id = id;
    }

    public productViewModel() {
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpBrif() {
        return pBrif;
    }

    public void setpBrif(String pBrif) {
        this.pBrif = pBrif;
    }

    public String getpImg(){
        return pImg;
    }
    public void setpImg(String pImg){
        this.pImg = pImg;
    }

    public int getpPrice() {
        return pPrice;
    }

    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
