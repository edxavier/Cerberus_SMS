package com.edxavier.cerberus_sms.tareas;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.interfaces.ContactListenerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsyncContacts_DB extends AsyncTask<Context, Void, ArrayList<Contactos>> {
    public ContactListenerInterface delegate = null;

    @Override
    protected ArrayList<Contactos> doInBackground(Context... params) {
        // Create Inbox box URI
        Context ctx = params[0];
        ArrayList<Contactos> contacts = new ArrayList<Contactos>();
        List<Contactos> result = (new Select().from(Contactos.class).orderBy("nombre ASC").execute());
        contacts = (ArrayList<Contactos>) result;
        return contacts;
    }

    @Override
    protected void onPostExecute(ArrayList<Contactos> contactoses) {
        super.onPostExecute(contactoses);
        delegate.readContactsFinish(contactoses);
    }
}
