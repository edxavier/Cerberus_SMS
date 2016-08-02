package com.edxavier.cerberus_sms.fragments.contacts.contracts;

import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public interface ContactsService {
    void loadContacts();
    FlowQueryList<PersonalContact> searchContact(String contactName);
    FlowQueryList<PersonalContact> getAllContacts();

    boolean saveContact(PersonalContact contact, boolean persistOnPhone);
    boolean deleteContact(PersonalContact contact, boolean hardDelete);


    boolean hasRecords();
    boolean hasPhonebookRecords();
    boolean canReadContacts();
    boolean canWriteContacts();

}
