package com.example.saggu.myapplication;

/**
 * Created by Saggu on 2/13/2017.
 */


public class Area {
    private int _id;
    private int _areaNo;
    private String _areaName;


    public Area() {

    }

    public Area(int id, int areano, String areaname) {
        this._id = id;
        this._areaNo = areano;
        this._areaName = areaname;
    }


    public int get_id(){
        return this._id;
    }
    public int get_areaNo(){
        return this._areaNo;
    }
    public String get_areaName(){
        return this._areaName;
    }



    public void set_id(int id){
        this._id=id;
    }
    public void set_areaNo(int areaNo){
        this._areaNo=areaNo;
    }
    public void set_areaName(String areaName){
        this._areaName=areaName;
    }


}
