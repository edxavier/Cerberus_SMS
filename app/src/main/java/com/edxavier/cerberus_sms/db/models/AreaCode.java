package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AreaCode extends Model{

    @Column(index = true)
    private String country_code;
    @Column(index = true)
    private String area_code;
    @Column
    private String country_name;
    @Column
    private String area_name;
    @Column
    private String area_operator;

    public AreaCode() {
    }

    public AreaCode(String country_code,String area_code, String country_name, String area_name, String area_operator) {
        this.country_code = country_code;
        this.area_code = area_code;
        this.country_name = country_name;
        this.area_name = area_name;
        this.area_operator = area_operator;
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

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
