package com.edxavier.cerberus_sms.fragments.contacts.contracts;

import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;
import com.raizlabs.android.dbflow.list.FlowQueryList;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public interface ContactsPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    void onEventMainThread(CheckOperatorEvent event);
    void loadContacts(boolean swipeLoading);
    void searchContact(String contactName);
    boolean saveContact(PersonalContact contact, boolean persistOnPhone);
    boolean deleteContact(PersonalContact contact, boolean hardDelete);

    boolean hasRecords();
    boolean canReadContacts();
    boolean canWriteContacts();
    void getAllContacts();
    boolean hasPhonebookRecords();

}
