package com.edxavier.cerberus_sms.fragments.checkOperator.implemts;

import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorPresenter;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorService;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorView;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Eder Xavier Rojas on 08/07/2016.
 */
public class CheckOperatorPresenterImpl implements CheckOperatorPresenter {
    private EventBusIface eventBus;
    private CheckOperatorView view;
    private CheckOperatorService service;

    public CheckOperatorPresenterImpl(EventBusIface eventBus, CheckOperatorView view, CheckOperatorService service) {
        this.eventBus = eventBus;
        this.view = view;
        this.service = service;
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
    @Subscribe
    public void onEventMainThread(CheckOperatorEvent event) {

    }

    @Override
    public AreaCode checkOperator(String phoneNumber) {
        return service.checkOperator(phoneNumber);
    }

    @Override
    public boolean checkDBinitialization() {
        return service.checkDBinitialization();
    }

    @Override
    public void dbInitialization() {
        service.dbInitialization();
    }

    @Override
    public void initAutocomplete() {
        service.initAutocomplete();
    }

    @Override
    public boolean isReadContactsPermsGranted() {
        return service.isReadContactsPermsGranted();
    }

    @Override
    public boolean checkContactPhonebookSync() {
        return service.checkContactPhonebookSync();
    }

    @Override
    public void syncronizeContacts() {
        service.syncronizeContacts();
    }

    @Override
    public boolean systemAlertWindowPermsGranted() {
        return service.systemAlertWindowPermsGranted();
    }
}
