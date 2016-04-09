package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 17/10/2015.
 */
public class InboxSms extends Model {
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
    int msg_thread;
    @Column
    Date fecha;

    public InboxSms() {
    }

    public InboxSms(String sender_name, String sender_number, String msg_body,  Date fecha, int msg_type, int msg_read) {
        this.sender_name = sender_name;
        this.sender_number = sender_number;
        this.msg_body = msg_body;
        this.msg_type = msg_type;
        this.msg_read = msg_read;
        this.fecha = fecha;
    }

    public int getMsg_thread() {
        return msg_thread;
    }

    public void setMsg_thread(int msg_thread) {
        this.msg_thread = msg_thread;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
}
