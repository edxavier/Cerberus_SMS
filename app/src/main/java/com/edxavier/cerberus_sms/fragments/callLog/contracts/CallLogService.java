package com.edxavier.cerberus_sms.fragments.callLog.contracts;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public interface CallLogService {
    boolean hasReadCalllogPermission();
    boolean hasPhoneCalllogEntries();
    boolean hasDatabaseCalllogEntries();

    FlowQueryList<CallLog> getCallLog();
    void readPhoneCalllog();

    List<CallLogResume> getCallsResume();

}
