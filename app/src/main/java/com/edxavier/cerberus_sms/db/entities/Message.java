package com.edxavier.cerberus_sms.db.entities;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
@Table(database = OperatorDB.class)
public class Message extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    String sender_name;
    @Column
    String sender_number;
    @Column
    String msg_body;
    @Column
    int msg_type;
    @Column
    int msg_read;
    @Column
    Date msg_date;

    public Message() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_number() {
        return sender_number;
    }

    public void setSender_number(String sender_number) {
        this.sender_number = sender_number;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getMsg_read() {
        return msg_read;
    }

    public void setMsg_read(int msg_read) {
        this.msg_read = msg_read;
    }

    public Date getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(Date msg_date) {
        this.msg_date = msg_date;
    }
}
