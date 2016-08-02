package com.edxavier.cerberus_sms.fragments.callLog.impl;

import android.util.Log;

import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.fragments.callLog.CallLogEvent;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogPresenter;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogService;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogView;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public class CalllogPresenterImpl implements CallLogPresenter {
    private EventBusIface eventBus;
    private CallLogService service;
    private CallLogView view;

    public CalllogPresenterImpl(EventBusIface eventBus, CallLogService service, CallLogView view) {
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
    }

    @Override
    public void onResume() {
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public boolean hasReadCalllogPermission() {
        return service.hasReadCalllogPermission();
    }

    @Override
    public void readPhoneCalllog() {
        view.showProgress(true);
        service.readPhoneCalllog();
    }

    @Override
    public boolean hasPhoneCalllogEntries() {
        return service.hasPhoneCalllogEntries();
    }

    @Override
    public boolean hasDatabaseCalllogEntries() {
        return service.hasDatabaseCalllogEntries();
    }

    @Override
    @Subscribe
    public void onEventMainThread(CallLogEvent event) {
        view.showProgress(false);
        if(!event.isHasError()){
            view.setCalllogList(event.getCalls());
        }
    }

    @Override
    public FlowQueryList<CallLog> getCallLog() {
        return service.getCallLog();
    }

    @Override
    public List<CallLogResume> getCallsResume() {
        return service.getCallsResume();
    }
}
