package com.edxavier.cerberus_sms.db.entities;


import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
@Table(database = OperatorDB.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.REPLACE)})
public class ContactEntry extends BaseModel {


    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String name;

    //@Unique(unique = false, uniqueGroups = 1)
    @Column
    @Index
    private String number;


    public ContactEntry(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public ContactEntry() {
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
}
