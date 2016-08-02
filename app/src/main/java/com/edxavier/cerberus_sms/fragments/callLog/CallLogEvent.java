package com.edxavier.cerberus_sms.fragments.callLog;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public class CallLogEvent {
    List<CallLogResume> calls;
    boolean hasError = false;

    public CallLogEvent(List<CallLogResume> calls, boolean hasError) {
        this.calls = calls;
        this.hasError = hasError;
    }

    public List<CallLogResume> getCalls() {
        return calls;
    }

    public void setCalls(List<CallLogResume> calls) {
        this.calls = calls;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}
