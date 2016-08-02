package com.edxavier.cerberus_sms.fragments.checkOperator.contracts;

import com.edxavier.cerberus_sms.db.entities.AreaCode;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public interface CheckOperatorService {
    AreaCode checkOperator(String phoneNumber);
    void initAutocomplete();
    boolean checkDBinitialization();
    void dbInitialization();

    boolean isReadContactsPermsGranted();
    boolean checkContactPhonebookSync();
    void syncronizeContacts();

    boolean systemAlertWindowPermsGranted();


}
