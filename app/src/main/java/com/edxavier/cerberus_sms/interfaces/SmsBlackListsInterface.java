package com.edxavier.cerberus_sms.interfaces;

import com.edxavier.cerberus_sms.db.models.Sms_Lock;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public interface SmsBlackListsInterface {
    void loadBlackListFinish(ArrayList<Sms_Lock> sms_locks);
}
