package com.edxavier.cerberus_sms.fragments.callLog.contracts;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;
import com.edxavier.cerberus_sms.fragments.callLog.CallLogEvent;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public interface CallLogPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    boolean hasReadCalllogPermission();
    void readPhoneCalllog();
    boolean hasPhoneCalllogEntries();
    boolean hasDatabaseCalllogEntries();

    void onEventMainThread(CallLogEvent event);

    FlowQueryList<CallLog> getCallLog();
    List<CallLogResume> getCallsResume();

}
