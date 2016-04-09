package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 20/10/2015.
 */
public class Call_Log extends Model{
    @Column
    String name;
    @Column
    String number;
    @Column
    int type;
    @Column
    Date fecha;
    @Column
    int duration;

    public Call_Log() {
    }

    public Call_Log(String name, String number, int type, Date fecha, int duration) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.fecha = fecha;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
