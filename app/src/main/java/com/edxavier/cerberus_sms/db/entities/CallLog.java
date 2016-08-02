package com.edxavier.cerberus_sms.db.entities;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
@Table(database = OperatorDB.class)
public class CallLog extends BaseModel{
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String name;
    @Column
    private String number;
    @Column
    private int callDirection;
    @Column
    private Date callDate;
    @Column
    private int callDuration;

    public CallLog() {
    }

    public CallLog(String name, String number, int callDirection, Date callDate, int callDuration) {
        this.name = name;
        this.number = number;
        this.callDirection = callDirection;
        this.callDate = callDate;
        this.callDuration = callDuration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getCallDirection() {
        return callDirection;
    }

    public void setCallDirection(int callDirection) {
        this.callDirection = callDirection;
    }

    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }
}
