package com.edxavier.cerberus_sms.tareas;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.Call_Log;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.interfaces.CallLogInterface;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsyncReadCallLogs extends AsyncTask<Context, Call_Log, Void> {
    public CallLogInterface delegate = null;

    @Override
    protected Void doInBackground(Context... params) {
        // Create Inbox box URI
        Context ctx = params[0];
        //ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        try{
            ContentResolver cr = ctx.getContentResolver();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 300";
            Uri callUri = Uri.parse("content://call_log/calls");
            Cursor cur = cr.query(callUri, null, null, null, strOrder);
            // loop through cursor
            while (cur.moveToNext()) {

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
                String originalNum = callNumber;
                Contactos c = new Select().from(Contactos.class).where("numero = ? ", callNumber).executeSingle();
                String name = Utils.getContactName(callNumber);
                Call_Log call_log = new Call_Log(name, callNumber, callType, fecha, duration);
                //call_log.save();
                publishProgress(call_log);
            }
            cur.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Call_Log... values) {
        super.onProgressUpdate(values[0]);
        delegate.onProgres(values[0]);
    }
}
