package com.edxavier.cerberus_sms.fragments.contacts.tasks;

import com.edxavier.cerberus_sms.db.entities.ContactEntry;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
public interface OnContactsReadyListener {
    void onContactsReady(ArrayList<PersonalContact> contacts);
}
