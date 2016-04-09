package com.edxavier.cerberus_sms.tareas;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.interfaces.TaskResponse;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsyncReadConversations extends AsyncTask<Context, Conversation, ArrayList<Conversation>> {
    public TaskResponse delegate = null;

    @Override
    protected ArrayList<Conversation> doInBackground(Context... params) {
        // Create Inbox box URI
        Context ctx = params[0];
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        //ArrayList<Conversation> conversations_temp = new ArrayList<Conversation>();
        try {
            Uri allSmsURI = Uri.parse("content://sms/");
            Uri inboxURI = Uri.parse("content://sms/inbox");
            Uri failedURI = Uri.parse("content://sms/failed");
            Uri conversationURI = Uri.parse("content://sms/conversations");

            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(conversationURI, null, null, null, "_id desc");
            Conversation conversation;
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    conversation = new Conversation();
                    String thread_id = cursor.getString(cursor.getColumnIndex("thread_id"));
                    conversation.setThread_id(cursor.getInt(cursor.getColumnIndex("thread_id")));
                    conversation.setMsg_count(cursor.getInt(cursor.getColumnIndex("msg_count")));
                    conversation.setSnippet(cursor.getString(cursor.getColumnIndex("snippet")));
                    Cursor cursor2 = cr.query(allSmsURI, null, "thread_id='" + thread_id + "'", null, "date desc");
                    if (cursor2 != null && cursor2.moveToFirst()) {
                            try {
                                String strAddress = Utils.formatPhoneNumber(cursor2.getString(cursor2.getColumnIndex("address")));
                                String originalNumber = strAddress;
                                long datelong = cursor2.getLong(cursor2.getColumnIndex("date"));

                                conversation.setName(Utils.getContactName(strAddress));
                                conversation.setNumber(originalNumber);
                                conversation.setDate(new Date(datelong));
                            } catch (Exception ignored) {
                                Log.d("EDER_1",ignored.getMessage());
                            }
                            //Verificar si existen mensajes sin leer en la conversacion
                            /*try {
                                cursor2 = cr.query(inboxURI, null, "thread_id='" + thread_id + "' and read='0'", null, "date desc");
                                if (cursor2 != null && cursor2.getCount() > 0) {
                                    conversation.setNew_msg_count(cursor2.getCount());
                                }
                            } catch (Exception ignored) {
                                Log.d("EDER_2",ignored.getMessage());
                            }

                            try{
                                //Verificar si existen mensajes sin enviar en la conversacion
                                cursor2 = cr.query(failedURI, null, "thread_id='" + thread_id + "' and type='5'", null, "date desc");
                                if (cursor2 != null && cursor2.getCount() > 0) {
                                    cursor2.moveToFirst();
                                    conversation.setSend_failure(cursor2.getInt(cursor2.getColumnIndex("type")));
                                }
                            } catch (Exception ignored) {
                                Log.d("EDER_3",ignored.getMessage());
                            }
                            */
                        cursor2.close();
                    }
                        //(conversation.save();
                        conversations.add(conversation);
                        //conversations_temp.add(conversation);
                        //conversations_temp.add(0,conversation);
                    publishProgress(conversation);
                }
                cursor.close();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("EDER2", e.getMessage());
        }
        //for(int i=conversations_temp.size()-1;i>=0;i--)
        //  conversations.add(conversations_temp.get(i));
        return conversations;
        //return conversations_temp;
        //return null;
    }

    @Override
    protected void onProgressUpdate(Conversation... values) {
        super.onProgressUpdate(values[0]);
        delegate.onProgres(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Conversation> result) {
        delegate.loadConversationFinish(result);
    }

}
