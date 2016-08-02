package com.edxavier.cerberus_sms.fragments.messages.task;

import com.edxavier.cerberus_sms.db.entities.Message;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
public interface ReadMessagesReady {
    void onReadMessagesReady(ArrayList<Message> messages);
}
