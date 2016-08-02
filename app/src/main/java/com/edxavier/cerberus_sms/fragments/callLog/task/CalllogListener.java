package com.edxavier.cerberus_sms.fragments.callLog.task;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
public interface CalllogListener {
    void onReadCallLogFinish(ArrayList<CallLog> callLogs);
}
