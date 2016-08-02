package com.edxavier.cerberus_sms.fragments;

import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.raizlabs.android.dbflow.list.FlowQueryList;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public class CheckOperatorEvent {
    public static int CONTACTS_EVENT = 0;
    public static int PHONE_CONTACTS_EMPTY = 1;
    public static boolean OK = true;
    public static boolean NOK = false;



    private boolean ok = true;
    private String error = null;
    int errorType;
    private FlowQueryList<PersonalContact> contacts = null;

    public CheckOperatorEvent(boolean ok, String error, FlowQueryList<PersonalContact> contacts) {
        this.ok = ok;
        this.error = error;
        this.contacts = contacts;
    }

    public FlowQueryList<PersonalContact> getContacts() {
        return contacts;
    }

    public void setContacts(FlowQueryList<PersonalContact> contacts) {
        this.contacts = contacts;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
}
