package com.edxavier.cerberus_sms.db.entities;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 23/07/2016.
 */
@QueryModel(database = OperatorDB.class)
public class CallLogResume extends BaseQueryModel {
    @Column
    String name;
    @Column
    String number;
    @Column
    int callDirection;
    @Column
    Date callDate;
    @Column
    int callDuration;
    @Column
    int number_calls;

    public CallLogResume() {
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

    public int getNumber_calls() {
        return number_calls;
    }

    public void setNumber_calls(int number_calls) {
        this.number_calls = number_calls;
    }
}
