package com.example.saggu.myapplication;

/**
 * Created by Saggu on 12/18/2016.
 */

public class Fees {
    private int _id;
    private int _no;
    private String _date;
    private int _reciept;
    private int _lBalance;
    private int _curBalance;



    private String _remark;

    //empty cunstuctor
    public Fees() {

    }

    public Fees(int no, int id, int fees, String date) {

        this._no = no;
        this._id = id;
        this._reciept = fees;
        this._date = date;
    }

    public Fees(int id, int fees, String date) {

        this._id = id;
        this._reciept = fees;
        this._date = date;
    }



    public Fees(int _id, String _date, int _reciept, int _lBalance, int _curBalance, String _remark) {
        this._id = _id;
        this._date = _date;
        this._reciept = _reciept;
        this._lBalance = _lBalance;
        this._curBalance = _curBalance;
        this._remark = _remark;
    }

    //getters
    public int getNo() {
        return this._no;
    }

    public int getId() {
        return this._id;
    }

    public int getFees() {
        return this._reciept;
    }

    public String getDate() {
        return this._date;
    }
    public int get_lBalance() {
        return _lBalance;
    }

    public int get_curBalance() {
        return _curBalance;
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
        this._reciept = fees;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public void set_lBalance(int _lBalance) {
        this._lBalance = _lBalance;
    }

    public void set_curBalance(int _curBalance) {
        this._curBalance = _curBalance;
    }
}
