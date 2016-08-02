package com.edxavier.cerberus_sms.fragments.messages.impl;

import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.edxavier.cerberus_sms.fragments.messages.MessageEvent;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesPresenter;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesService;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesView;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 21/07/2016.
 */
public class MessagePresenterImpl implements MessagesPresenter {
    private EventBusIface eventBus;
    private MessagesService service;
    private MessagesView view;

    public MessagePresenterImpl(EventBusIface eventBus, MessagesService service, MessagesView view) {
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
    public boolean hasReadSMSPermission() {
        return service.hasReadSMSPermission();
    }

    @Override
    public void readSMSfromPhone() {
        view.showProgress(true);
        service.readSMSfromPhone();
    }

    @Override
    public List<MessagesResume> getMessagesResume() {
        return service.getMessagesResume();
    }

    @Override
    public FlowQueryList<Message> getMessages() {
        return service.getMessagesfromDB();
    }

    @Override
    public boolean hasPhoneSMSEntries() {
        return service.hasPhoneSMSEntries();
    }

    @Override
    public boolean hasDatabaseSMSEntries() {
        return service.hasDatabaseSMSEntries();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        view.showProgress(false);
        if(!event.getMessages().isEmpty()){
            view.setMessages(event.getMessages());
        }else {
            view.showEmptyMsg(true);
        }
    }
}
