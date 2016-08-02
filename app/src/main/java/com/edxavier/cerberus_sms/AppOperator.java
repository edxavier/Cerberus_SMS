package com.edxavier.cerberus_sms;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogView;
import com.edxavier.cerberus_sms.fragments.callLog.di.CallLogComponent;
import com.edxavier.cerberus_sms.fragments.callLog.di.CallLogModule;
import com.edxavier.cerberus_sms.fragments.callLog.di.DaggerCallLogComponent;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorView;
import com.edxavier.cerberus_sms.fragments.checkOperator.di.CheckOperatorModule;
import com.edxavier.cerberus_sms.fragments.checkOperator.di.CheckoperatorComponent;
import com.edxavier.cerberus_sms.fragments.checkOperator.di.DaggerCheckoperatorComponent;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsView;
import com.edxavier.cerberus_sms.fragments.contacts.di.ContactsComponent;
import com.edxavier.cerberus_sms.fragments.contacts.di.ContactsModule;
import com.edxavier.cerberus_sms.fragments.contacts.di.DaggerContactsComponent;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesView;
import com.edxavier.cerberus_sms.fragments.messages.di.DaggerMessageComponent;
import com.edxavier.cerberus_sms.fragments.messages.di.MessageComponent;
import com.edxavier.cerberus_sms.fragments.messages.di.MessageModule;
import com.edxavier.cerberus_sms.mLibs.di.LibsModule;
import com.google.android.gms.ads.MobileAds;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public class AppOperator extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        MobileAds.initialize(this, "ca-app-pub-9964109306515647~3887839019");
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }
    public CheckoperatorComponent getCheckoperatorComponent(Fragment fragment, CheckOperatorView view){
        return DaggerCheckoperatorComponent.builder()
                .libsModule(new LibsModule(fragment))
                .checkOperatorModule(new CheckOperatorModule(view))
                .build();
    }

    public ContactsComponent getContactComponent(Fragment fragment, ContactsView view){
        return DaggerContactsComponent.builder()
                .libsModule(new LibsModule(fragment))
                .contactsModule(new ContactsModule(view))
                .build();
    }

    public CallLogComponent getCallLogComponent(Fragment fragment, CallLogView view){
        return DaggerCallLogComponent.builder()
                .libsModule(new LibsModule(fragment))
                .callLogModule(new CallLogModule(view)).build();
    }
    public MessageComponent getMessageComponent(Fragment fragment, MessagesView view){
        return DaggerMessageComponent.builder()
                .libsModule(new LibsModule(fragment))
                .messageModule(new MessageModule(view)).build();
    }

    /*
*/
}
