package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 26/10/2015.
 */
public class Conversation extends Model{
    @Column(index = true)
    int thread_id;
    @Column
    int msg_count;
    @Column
    int new_msg_count;
    @Column
    String name;
    @Column
    String number;
    @Column
    String snippet;
    @Column
    Date date;
    @Column
    int send_failure;
    @Column
    int unreads_sms ;

    public Conversation() {
    }

    public Conversation(int thread_id, int msg_count, String snippet, Date date, int send_failure, int unreads_sms) {
        this.thread_id = thread_id;
        this.msg_count = msg_count;
        this.snippet = snippet;
        this.date = date;
        this.send_failure = send_failure;
        this.unreads_sms = unreads_sms;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getNew_msg_count() {
        return new_msg_count;
    }

    public void setNew_msg_count(int new_msg_count) {
        this.new_msg_count = new_msg_count;
    }

    public int getSend_failure() {
        return send_failure;
    }

    public void setSend_failure(int send_failure) {
        this.send_failure = send_failure;
    }

    public int getUnreads_sms() {
        return unreads_sms;
    }

    public void setUnreads_sms(int unreads_sms) {
        this.unreads_sms = unreads_sms;
    }
}
