package com.bluesoftit.cashcoin.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
public class WithdrawRequest {
    private String userId;
    private String withdrawMethode;
    private String requestedBy;
    private long withdrawAmmount;

    public WithdrawRequest() {

    }

    public WithdrawRequest(String userId, String withdrawMethode, String requestedBy, long withdrawAmmount) {
        this.userId = userId;
        this.withdrawMethode = withdrawMethode;
        this.requestedBy = requestedBy;
        this.withdrawAmmount = withdrawAmmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWithdrawMethode() {
        return withdrawMethode;
    }

    public void setWithdrawMethode(String withdrawMethode) {
        this.withdrawMethode = withdrawMethode;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public double getWithdrawAmmount() {
        return withdrawAmmount;
    }

    public void setWithdrawAmmount(long withdrawAmmount) {
        this.withdrawAmmount = withdrawAmmount;
    }

    @ServerTimestamp
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}