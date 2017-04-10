package com.example.saggu.myapplication;

import android.util.Log;
import android.widget.EditText;

public class PersonInfo {
    //Private variables
    private int _id;
    private String _name;
    private String _phone_no;
    private float _cust_no;
    private int _fees;
    private int _balance;
    private int _area;
    private String _date;
    private String _cstatus;


    // Empty Consturctor
    public PersonInfo() {
    }

    //Constructor


    //Constructors
    public PersonInfo(String name, String phone_no, float cust_no, int fees, int balance, int area, String date) {
        this._name = name;
        this._phone_no = phone_no;
        this._cust_no = cust_no;
        this._fees = fees;
        this._balance= balance;
        this._area=area;
        this._date= date;
    }

    public PersonInfo(int id,  String name, String phone_no, float cust_no, int fees, int balance,int area, String date) {
        this._id = id;
        this._name = name;
        this._phone_no = phone_no;
        this._cust_no = cust_no;
        this._fees = fees;
        this._balance= balance;
        this._area = area;
        this._date= date;

    }

    public PersonInfo(int id, int balance) {
        this._id = id;
        this._balance= balance;
    }

    public PersonInfo(String name , String startdate) {

    }

    public PersonInfo(int Id) {
        this._id=Id;
    }


    //getters
    public int getID() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public String getPhoneNumber() {
        return this._phone_no;
    }

    public float get_cust_no(){return this._cust_no;}


    public int get_fees(){return this._fees;}

    public int get_balance(){return this._balance;}
    public int get_area() {return this._area; }
    public String get_startdate(){return  this._date;}

    public String get_cstatus(){return this._cstatus;}


    //setting id
    public void setID(int id) {
        this._id = id;
    }
    //setting name
    public void setName(String name) {
        this._name = name;
    }
    // setting phone number
    public void setPhoneNumber(String phone_no) {
        this._phone_no = phone_no;
    }
    //settin customer no
    public void setCustNo(float cust_no){this._cust_no=cust_no;}
    //seeting fees
    public void setFees(int fees){this._fees = fees;}
  //setting balance
    public void setBalance(int balance){this._balance = balance;}
    public void setArea(int area){this._area =area;}
    public void setStartDate(String date){this._date = date;}



}
