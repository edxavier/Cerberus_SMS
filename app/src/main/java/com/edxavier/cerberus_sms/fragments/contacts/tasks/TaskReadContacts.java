package com.edxavier.cerberus_sms.fragments.contacts.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.edxavier.cerberus_sms.db.entities.ContactEntry;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.contacts.impl.ContactsServiceImpl;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
public class TaskReadContacts extends AsyncTask<Context, Void, Void> {
    public OnContactsReadyListener listener = null;

    @Override
    protected Void doInBackground(Context... params) {
        Context ctx = params[0];
        String temp_number="";
        ArrayList<PersonalContact> contacts = new ArrayList<>();

        ContentResolver cr = ctx.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String haveNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                if (Integer.parseInt(haveNumber) > 0) {
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumber = Utils.formatPhoneNumber(phoneNumber);
                    PersonalContact contacto = new PersonalContact(name, phoneNumber, contact_id);
                    if(!phoneNumber.equals(temp_number)) {
                        contacts.add(contacto);
                        temp_number=phoneNumber;
                    }
                }
            }
            cursor.close();

        }
        if(listener!=null) {
            listener.onContactsReady(contacts);
        }else {
            ProcessModelTransaction<PersonalContact> modelTransaction = new ProcessModelTransaction.Builder<>(
                    new ProcessModelTransaction.ProcessModel<PersonalContact>(){
                        @Override
                        public void processModel(PersonalContact model) {
                            if(!ContactsServiceImpl.contactExist(model.getContact_phone_number())){
                                model.save();
                            }
                        }
                    }).processListener(new ProcessModelTransaction.OnModelProcessListener<PersonalContact>() {
                @Override
                public void onModelProcessed(long current, long total, PersonalContact modifiedModel) {

                }
            }).addAll(contacts).build();
            if(!contacts.isEmpty()) {
                Transaction transaction = FlowManager.getDatabase(OperatorDB.class).beginTransactionAsync(modelTransaction).build();
                transaction.execute();
            }
        }
        return null;
    }
}
