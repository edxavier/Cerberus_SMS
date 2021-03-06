package com.edxavier.cerberus_sms.interfaces;

import com.edxavier.cerberus_sms.db.models.Conversation;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public interface TaskResponse {
    void loadConversationFinish(ArrayList<Conversation> conversation);
    void loadCCallLogFinish();
    void onProgres(Conversation conversation);
}
