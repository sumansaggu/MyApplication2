package com.example.saggu.myapplication;

/**
 * Created by Saggu on 7/29/2017.
 */

public class ImportValues {
    private String names;
    private String mobNo;
    private double conNo;


    private double fees;
    private double balance;



    private String nickName;


    public ImportValues(String names, String mobNo, double conNo, double fees, double balance,String nickName) {
        this.names = names;
        this.mobNo = mobNo;
        this.conNo = conNo;
        this.fees = fees;
        this.balance = balance;
        this.nickName= nickName;

    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public double getConNo() {
        return conNo;
    }

    public void setConNo(int conNo) {
        this.conNo = conNo;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    private double x;
    private double y;

    public ImportValues(double x, double y) {
        this.x = x;
        this.y = y;
    }


}
