package com.edxavier.cerberus_sms.fragments.callLog.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.db.entities.PersonalContact_Table;
import com.edxavier.cerberus_sms.db.models.Call_Log;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public class TaskReadCallLog extends AsyncTask<Context, Void, Void> {
    public CalllogListener listener;

    @Override
    protected Void doInBackground(Context... params) {
        Context ctx = params[0];
        ArrayList<CallLog> calls = new ArrayList<>();
        Log.e("EDER", "Hilo1");
        try{
            ContentResolver cr = ctx.getContentResolver();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 300";
            Uri callUri = Uri.parse("content://call_log/calls");
            Cursor cur = cr.query(callUri, null, null, null, strOrder);
            // loop through cursor
            while (cur != null && cur.moveToNext()) {

                String callNumber = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                long callDate = cur.getLong(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DATE));
                Date fecha = new Date(callDate);
                int callType = cur.getInt(cur
                        .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                int duration = cur.getInt(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DURATION));

                callNumber = Utils.formatPhoneNumber(callNumber);
                String name = Utils.getContactName(callNumber);
                CallLog callLog = new CallLog(name, callNumber, callType, fecha,duration);
                calls.add(callLog);
            }
            if (cur != null) {
                cur.close();
            }
            listener.onReadCallLogFinish(calls);
        }catch (Exception e){
            listener.onReadCallLogFinish(null);
        }
        return null;
    }
}
