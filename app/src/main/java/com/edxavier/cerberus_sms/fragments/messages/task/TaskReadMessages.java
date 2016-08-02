package com.edxavier.cerberus_sms.fragments.messages.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.db.models.InboxSms;
import com.edxavier.cerberus_sms.fragments.messages.task.ReadMessagesReady;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
public class TaskReadMessages extends AsyncTask<Context, Void, Void> {
    public ReadMessagesReady listener = null;
    @Override
    protected Void doInBackground(Context... params) {
        Context ctx = params[0];
        try {
            ArrayList<Message> messages = new ArrayList<>();
            Uri allSmsURI = Uri.parse("content://sms/");
            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(allSmsURI, null, null, null, "date desc");
            while (cursor != null && cursor.moveToNext()){
                Message message = new Message();
                String service_center = cursor.getString(cursor.getColumnIndex("service_center"));
                String number = cursor.getString(cursor.getColumnIndex("address"));
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String read = cursor.getString(cursor.getColumnIndex("read"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                number = Utils.formatPhoneNumber(number);

                message.setSender_name(Utils.getContactName(number));
                message.setSender_number(number);
                message.setMsg_body(body);
                message.setMsg_date(new Date(date));
                message.setMsg_read(Integer.parseInt(read));
                message.setMsg_type(type);
                messages.add(message);
                listener.onReadMessagesReady(messages);
            }
            if (cursor != null) {
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
