package com.edxavier.cerberus_sms.tareas;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.interfaces.ContactListenerInterface;
import com.edxavier.cerberus_sms.interfaces.SmsBlackListsInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsyncBlackListSMS extends AsyncTask<Context, Void, ArrayList<Sms_Lock>> {
    public SmsBlackListsInterface delegate = null;

    @Override
    protected ArrayList<Sms_Lock> doInBackground(Context... params) {
        List<Sms_Lock> result = (new Select().from(Sms_Lock.class).execute());
        return (ArrayList<Sms_Lock>) result;
    }

    @Override
    protected void onPostExecute(ArrayList<Sms_Lock> sms_locks) {
        super.onPostExecute(sms_locks);
        delegate.loadBlackListFinish(sms_locks);
    }
}
