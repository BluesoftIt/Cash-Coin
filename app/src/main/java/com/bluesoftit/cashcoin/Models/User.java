package com.bluesoftit.cashcoin.Models;

public class User {
    private String name, email, pass,profile, referCode, myRefer,userId;
    private long coins = 25;

    public User() {
    }

    public User(String name, String email, String pass, String referCode, String myRefer, String userId) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.referCode = referCode;
        this.myRefer=myRefer;
        this.userId=userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getMyRefer(){return myRefer;}
    public void setMyRefer(String myRefer){this.myRefer=myRefer;}

    //--------->>>>>>
    public String getUserId(){return userId;}
    public void setUserId(String userId){this.userId=userId;}

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
