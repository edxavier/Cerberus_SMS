package com.edxavier.cerberus_sms.db.entities;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 12/07/2016.
 */
@Table(database = OperatorDB.class)
public class PersonalContact extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String contact_id;

    @Column
    private String contact_name;

    @Column @Index @Unique(onUniqueConflict = ConflictAction.REPLACE)
    private String contact_phone_number;

    public PersonalContact( String contact_name, String contact_phone_number, String id) {
        this.contact_name = contact_name;
        this.contact_phone_number = contact_phone_number;
        this.contact_id = id;
    }

    public PersonalContact() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone_number() {
        return contact_phone_number;
    }

    public void setContact_phone_number(String contact_phone_number) {
        this.contact_phone_number = contact_phone_number;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }
}
