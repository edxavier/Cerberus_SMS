package com.edxavier.cerberus_sms.fragments.contacts.impl;

import android.util.Log;

import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsPresenter;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsService;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsView;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
public class ContactsPresenterImpl implements ContactsPresenter {
    private EventBusIface eventBus;
    private ContactsView view;
    private ContactsService service;

    public ContactsPresenterImpl(EventBusIface eventBus, ContactsView view, ContactsService service) {
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
        Log.e("EDER", "onEventMainThread init");
        try {
            if(event.isOk() && event.getErrorType() == CheckOperatorEvent.CONTACTS_EVENT){
                view.showLoadingMessage(false);
                if(event.getContacts().size()<=0 || event.getContacts() == null){
                    view.showEmptyListMessage(true);
                    Log.e("EDER", "onEventMainThread Empty Result");
                }else {
                    view.showEmptyListMessage(false);
                    Log.e("EDER", "onEventMainThread Not Empty Result");
                    view.setContacts(event.getContacts());
                }
            }
        }catch (Exception ignored){
        }

    }

    @Override
    public void loadContacts(boolean swipeLoading) {
        if(!swipeLoading)
            view.showLoadingMessage(true);
        service.loadContacts();
    }

    @Override
    public void searchContact(String contactName) {
        view.setContacts(service.searchContact(contactName));
    }



    @Override
    public boolean saveContact(PersonalContact contact, boolean persistOnPhone) {
        return service.saveContact(contact, persistOnPhone);
    }

    @Override
    public boolean deleteContact(PersonalContact contact, boolean hardDelete) {
        return service.deleteContact(contact, hardDelete);
    }

    @Override
    public boolean hasRecords() {
        return service.hasRecords();
    }

    @Override
    public boolean canReadContacts() {
        return service.canReadContacts();
    }

    @Override
    public boolean canWriteContacts() {
        return service.canWriteContacts();
    }

    @Override
    public void getAllContacts() {
        view.setContacts(service.getAllContacts());
    }

    @Override
    public boolean hasPhonebookRecords() {
        return service.hasPhonebookRecords();
    }
}
