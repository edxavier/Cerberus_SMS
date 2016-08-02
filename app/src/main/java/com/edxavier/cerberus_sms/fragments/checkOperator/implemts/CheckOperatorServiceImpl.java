package com.edxavier.cerberus_sms.fragments.checkOperator.implemts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorService;
import com.edxavier.cerberus_sms.fragments.contacts.tasks.TaskReadContacts;
import com.edxavier.cerberus_sms.helpers.AreaCodeHelper;
import com.edxavier.cerberus_sms.helpers.InitDatabaseAsynctasks;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 08/07/2016.
 */
public class CheckOperatorServiceImpl implements CheckOperatorService {
    private EventBusIface eventBus;
    private Context context;

    public CheckOperatorServiceImpl(EventBusIface eventBus, Context context) {
        this.eventBus = eventBus;
        this.context = context;
    }

    @Override
    public AreaCode checkOperator(String phoneNumber) {
        return Utils.getOperadoraV3(phoneNumber, context);
    }

    @Override
    public void initAutocomplete() {

    }

    @Override
    public boolean checkDBinitialization() {
        return !SQLite.select().from(AreaCode.class).queryList().isEmpty();
    }

    @Override
    public void dbInitialization() {
        AreaCodeHelper.initAreaCodes(context);
    }

    @Override
    public boolean isReadContactsPermsGranted() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean systemAlertWindowPermsGranted() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean checkContactPhonebookSync() {
        List<PersonalContact> contacts = SQLite.select().from(PersonalContact.class).queryList();
        return !contacts.isEmpty();
    }

    @Override
    public void syncronizeContacts() {
        TaskReadContacts taskReadContacts = new TaskReadContacts();
        taskReadContacts.execute(context);
    }
}
