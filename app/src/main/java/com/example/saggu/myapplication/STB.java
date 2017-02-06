package com.example.saggu.myapplication;

import android.content.Intent;

/**
 * Created by Saggu on 1/23/2017.
 */

public class STB {
    private int _id;
    private String _serialNo;
    private String _vcNo;
    private String _status;
    private int _assigned;

    //empty cunstuctor
    public STB() {

    }


    public STB(int id, String serialNo, String vcNo, String status) {

        this._id = id;
        this._serialNo = serialNo;
        this._vcNo = vcNo;
        this._status = status;
    }

    public STB(String serialNo, String vcNo) {
        this._serialNo = serialNo;
        this._vcNo = vcNo;
    }

    public STB(int id, String serialNo, String vcNo) {
        this._id = id;
        this._serialNo = serialNo;
        this._vcNo = vcNo;


    }

    public STB(String sn, String vc, String stbStatus) {
        this._serialNo = sn;
        this._vcNo = vc;
        this._status = stbStatus;
    }
    public STB( int stbID,int assigned){
        this._id= stbID;
        this._assigned = assigned;
    }


    //getters
    public int getId() {
        return this._id;
    }

    public String getSerialNo() {
        return this._serialNo;
    }

    public String getVcNo() {
        return this._vcNo;
    }

    public String getStatus() {
        return this._status;
    }

    public int getAssigned(){return this._assigned;}


    //setting id
    public void setID(int id) {
        this._id = id;
    }

    //setting sn
    public void setSerialNo(String serialNo) {
        this._serialNo = serialNo;
    }

    // setting vc
    public void setVcNo(String vcNo) {
        this._vcNo = vcNo;
    }
}
