package com.edxavier.cerberus_sms.interfaces;

import com.edxavier.cerberus_sms.db.models.Call_Log;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public interface CallLogInterface {
    void onProgres(Call_Log call_log);
}
