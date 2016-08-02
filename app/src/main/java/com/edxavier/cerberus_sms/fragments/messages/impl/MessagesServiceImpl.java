package com.edxavier.cerberus_sms.fragments.messages.impl;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.db.entities.CallLog_Table;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.Message_Table;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.edxavier.cerberus_sms.fragments.callLog.CallLogEvent;
import com.edxavier.cerberus_sms.fragments.messages.MessageEvent;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesService;
import com.edxavier.cerberus_sms.fragments.messages.task.ReadMessagesReady;
import com.edxavier.cerberus_sms.fragments.messages.task.TaskReadMessages;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 20/07/2016.
 */
public class MessagesServiceImpl implements MessagesService, ReadMessagesReady {
    private Context context;
    private EventBusIface eventBus;

    public MessagesServiceImpl(Context context, EventBusIface eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    @Override
    public boolean hasReadSMSPermission() {
        int hasPerm = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            hasPerm = context.getPackageManager().checkPermission(Manifest.permission.READ_SMS,
                    context.getPackageName());
        }
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void readSMSfromPhone() {
        TaskReadMessages task = new TaskReadMessages();
        task.listener = this;
        task.execute(context);
    }

    @Override
    public boolean hasPhoneSMSEntries() {
        Uri conversationURI = Uri.parse("content://sms/conversations");
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(conversationURI, null, null, null, "_id desc");
        boolean hasRecords = false;
        if((cursor != null ? cursor.getCount() : 0) >0){
            hasRecords = true;
            if (cursor != null) {
                cursor.close();
            }
        }
        return hasRecords;
    }

    @Override
    public boolean hasDatabaseSMSEntries() {
        List<Message> messages = SQLite.select().from(Message.class).queryList();
        return !messages.isEmpty();
    }

    @Override
    public FlowQueryList<Message> getMessagesfromDB() {
        return SQLite.select().from(Message.class)
                .orderBy(Message_Table.msg_date, false).flowQueryList();
    }

    @Override
    public void onReadMessagesReady(ArrayList<Message> messages) {
        ProcessModelTransaction<Message> modelTransaction = new ProcessModelTransaction.Builder<>(
                new ProcessModelTransaction.ProcessModel<Message>(){
                    @Override
                    public void processModel(Message model) {
                        model.save();
                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<Message>() {
            @Override
            public void onModelProcessed(long current, long total, Message modifiedModel) {
                if(current+1==total){
                    eventBus.post(new MessageEvent(getMessagesResume(), true));
                }
            }
        }).addAll(messages).build();
        if(!messages.isEmpty()) {
            Transaction transaction = FlowManager.getDatabase(OperatorDB.class).beginTransactionAsync(modelTransaction).build();
            transaction.execute();
        }
    }


    @Override
    public  List<MessagesResume> getMessagesResume() {
        return SQLite.select(Message_Table.sender_name, Message_Table.sender_number,
                Message_Table.msg_date, Message_Table.msg_body,
                Method.count(Message_Table.sender_number).as("messages_count")
        ).from(Message.class)
                .orderBy(Message_Table.msg_date, false)
                .groupBy(Message_Table.sender_number)
                .queryCustomList(MessagesResume.class);
    }

}
