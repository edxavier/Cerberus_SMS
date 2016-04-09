package com.edxavier.cerberus_sms.tareas;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.interfaces.ContactListenerInterface;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsyncContacts extends AsyncTask<Context, Contactos, Void> {
    public ContactListenerInterface delegate = null;

    @Override
    protected Void doInBackground(Context... params) {
        // Create Inbox box URI
        Context ctx = params[0];
        //ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        String temp_number="";
        try {
            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    String haveNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                    if (Integer.parseInt(haveNumber) > 0) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumber = Utils.formatPhoneNumber(phoneNumber);
                        Contactos contacto = new Contactos(name, phoneNumber);
                        if(!phoneNumber.equals(temp_number)) {
                            publishProgress(contacto);
                            contacto.save();
                            temp_number=phoneNumber;
                        }

                    }
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Contactos... values) {
        //super.onProgressUpdate(values[0]);
        try {
            delegate.onProgres(values[0]);
        }catch (Exception ingnored){}
    }
}
