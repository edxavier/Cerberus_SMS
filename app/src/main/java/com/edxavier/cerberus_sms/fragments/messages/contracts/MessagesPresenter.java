package com.edxavier.cerberus_sms.fragments.messages.contracts;

import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.edxavier.cerberus_sms.fragments.messages.MessageEvent;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
public interface MessagesPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    boolean hasReadSMSPermission();
    void readSMSfromPhone();
    FlowQueryList<Message> getMessages();
    boolean hasPhoneSMSEntries();
    boolean hasDatabaseSMSEntries();
    List<MessagesResume> getMessagesResume();
    void onEventMainThread(MessageEvent event);

}
