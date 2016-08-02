package com.edxavier.cerberus_sms.fragments.messages;

import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 21/07/2016.
 */
public class MessageEvent {
    List<MessagesResume> messages;
    boolean ok;
    public MessageEvent(List<MessagesResume> messages, boolean b) {
        this.messages = messages;
        this.ok = b;
    }

    public List<MessagesResume> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesResume> messages) {
        this.messages = messages;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
