package com.edxavier.cerberus_sms.db.entities;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
@Table(database = OperatorDB.class)
public class AreaCode extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column @Index
    private String country_code;

    @Column @Index
    private String area_code;
    @Column
    private String country_name;
    @Column
    private String area_name;
    @Column
    private String area_operator;
    @Column
    private int flag;


    public AreaCode() {
    }

    public AreaCode(String country_code, String area_code, String country_name, String area_name, String area_operator, int flag) {
        this.country_code = country_code;
        this.area_code = area_code;
        this.country_name = country_name;
        this.area_name = area_name;
        this.area_operator = area_operator;
        this.flag = flag;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_operator() {
        return area_operator;
    }

    public void setArea_operator(String area_operator) {
        this.area_operator = area_operator;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
