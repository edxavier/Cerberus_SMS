package com.edxavier.cerberus_sms.fragments.checkOperator.contracts;

import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public interface CheckOperatorPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    void onEventMainThread(CheckOperatorEvent event);
    AreaCode checkOperator(String phoneNumber);
    boolean checkDBinitialization();
    void dbInitialization();
    void initAutocomplete();

    boolean isReadContactsPermsGranted();
    boolean checkContactPhonebookSync();
    void syncronizeContacts();
    boolean systemAlertWindowPermsGranted();

}
