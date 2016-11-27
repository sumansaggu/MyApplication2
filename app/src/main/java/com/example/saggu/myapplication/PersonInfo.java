package com.example.saggu.myapplication;

public class PersonInfo {
    //Private variables
   private int    _id;
   private String _name;
   private String _phone_no;

    // Empty Consturctor
    public PersonInfo(){
    }
    //Constructor
    public PersonInfo(int id, String name, String phone_no){
        this._id = id;
        this._name = name;
        this._phone_no = phone_no;
    }
    //Constructor
    public PersonInfo(String name, String phone_no){
        this._name = name;
        this._phone_no = phone_no;
    }

     //getting id
    public int getID(){
        return this._id;
    }
    //setting id
    public void setID(int id){
        this._id = id;
    }

        //getting name
    public String getName(){
        return this._name;
    }
    //setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_no;
    }

    // setting phone number
    public void setPhoneNumber(String phone_no){
        this._phone_no = phone_no;
    }

}
