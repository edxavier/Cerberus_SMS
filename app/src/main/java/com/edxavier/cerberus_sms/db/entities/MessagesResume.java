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
public class MessagesResume extends BaseQueryModel {
    @Column
    String sender_name;
    @Column
    String sender_number;
    @Column
    String msg_body;
    @Column
    int messages_count;
    @Column
    Date msg_date;

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

    public int getMessages_count() {
        return messages_count;
    }

    public void setMessages_count(int messages_count) {
        this.messages_count = messages_count;
    }

    public Date getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(Date msg_date) {
        this.msg_date = msg_date;
    }
}
