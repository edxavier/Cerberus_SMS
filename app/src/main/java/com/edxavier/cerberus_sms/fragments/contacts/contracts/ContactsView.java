package com.edxavier.cerberus_sms.fragments.contacts.contracts;

import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 07/07/2016.
 */
public interface ContactsView {
    void showLoadingMessage(boolean show);
    void showEmptyListMessage(boolean show);
    void setContacts(FlowQueryList<PersonalContact> contacts);
    void getContacts(boolean isRefreshing);
    void searchContact(String contactName);

}
