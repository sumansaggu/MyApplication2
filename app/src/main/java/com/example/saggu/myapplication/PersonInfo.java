package com.example.saggu.myapplication;

public class PersonInfo {
    //Private variables
    private int _id;
    private String _name;
    private String _phone_no;
    private float _cust_no;
    private int _fees;
    private int _balance;

    // Empty Consturctor
    public PersonInfo() {
    }

    //Constructor
    public PersonInfo(int id, String name, String phone_no) {
        this._id = id;
        this._name = name;
        this._phone_no = phone_no;
    }
    //Constructor
    public PersonInfo(String name, String phone_no, int cust_no, int fees) {
        this._name = name;
        this._phone_no = phone_no;
        this._cust_no = cust_no;
        this._fees = fees;

    }

    //Constructor
    public PersonInfo(String name, String phone_no, float cust_no, int fees, int balance) {
        this._name = name;
        this._phone_no = phone_no;
        this._cust_no = cust_no;
        this._fees = fees;
        this._balance= balance;
    }

    public PersonInfo(int id, String name, String phone_no, float cust_no, int fees, int balance) {
        this._id = id;
        this._name = name;
        this._phone_no = phone_no;
        this._cust_no = cust_no;
        this._fees = fees;
        this._balance= balance;
    }

    public PersonInfo(int id, int balance) {
        this._id = id;
        this._balance= balance;
    }

    //getting id
    public int getID() {
        return this._id;
    }
    //getting name
    public String getName() {
        return this._name;
    }
    // getting phone number
    public String getPhoneNumber() {
        return this._phone_no;
    }
    //geeting custmer no
    public float get_cust_no(){return this._cust_no;}
    //geeting fees
    public int get_fees(){return this._fees;}
    //getting balance
    public int get_balance(){return this._balance;}


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



}
