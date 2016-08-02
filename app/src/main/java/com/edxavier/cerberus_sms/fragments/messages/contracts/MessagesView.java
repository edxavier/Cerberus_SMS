package com.edxavier.cerberus_sms.fragments.messages.contracts;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
public interface MessagesView {
    void setupInjection();
    void loadSMS();
    void setupAds();
    void showEmptyMsg(boolean show);
    void showProgress(boolean show);
    void setMessages(List<MessagesResume> messages);
}
