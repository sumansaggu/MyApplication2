package com.example.saggu.myapplication;

/**
 * Created by Saggu on 12/18/2016.
 */

public class Fees {
    private int _id;
    private int _no;
    private int _fees;
    private String _date;
    private String _remark;

    //empty cunstuctor
    public Fees() {

    }

    public Fees(int no, int id, int fees, String date) {

        this._no = no;
        this._id = id;
        this._fees = fees;
        this._date = date;
    }

    public Fees(int id, int fees, String date) {

        this._id = id;
        this._fees = fees;
        this._date = date;
    }

    public Fees(int id, int fees, String datefromEditText, String remark) {
        this._id = id;
        this._fees = fees;
        this._date = datefromEditText;
        this._remark = remark;
    }


    //getters
    public int getNo() {
        return this._no;
    }

    public int getId() {
        return this._id;
    }

    public int getFees() {
        return this._fees;
    }

    public String getDate() {
        return this._date;
    }

    public String getRemark() {
        return this._remark;
    }


    //setters
    public void setNo(int no) {
        this._no = no;
    }

    public void setID(int id) {
        this._id = id;
    }

    public void setFees(int fees) {
        this._fees = fees;
    }

    public void setDate(String date) {
        this._date = date;
    }


}
