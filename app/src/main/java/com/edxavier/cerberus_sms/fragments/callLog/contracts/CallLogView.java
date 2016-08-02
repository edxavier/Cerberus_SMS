package com.edxavier.cerberus_sms.fragments.callLog.contracts;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public interface CallLogView {
    void setupInjection();
    void setupRecycler(List<CallLogResume> calls);
    void loadCalllog();
    void setupAds();
    void showEmptyMsg(boolean show);
    void showProgress(boolean show);
    void setCalllogList(List<CallLogResume> calls);


}
