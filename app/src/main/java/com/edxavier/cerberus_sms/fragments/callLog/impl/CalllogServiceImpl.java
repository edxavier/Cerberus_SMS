package com.edxavier.cerberus_sms.fragments.callLog.impl;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.edxavier.cerberus_sms.db.OperatorDB;
import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.CallLog_Table;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.fragments.callLog.CallLogEvent;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogService;
import com.edxavier.cerberus_sms.fragments.callLog.task.CalllogListener;
import com.edxavier.cerberus_sms.fragments.callLog.task.TaskReadCallLog;
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
 * Created by Eder Xavier Rojas on 18/07/2016.
 */
public class CalllogServiceImpl implements CallLogService, CalllogListener{
    private EventBusIface eventBus;
    private Context context;

    public CalllogServiceImpl(EventBusIface eventBus, Context context) {
        this.eventBus = eventBus;
        this.context = context;
    }

    @Override
    public boolean hasReadCalllogPermission() {
        int hasPerm = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            hasPerm = context.getPackageManager().checkPermission(Manifest.permission.READ_CALL_LOG,
                    context.getPackageName());
        }
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean hasPhoneCalllogEntries() {
        ContentResolver cr = context.getContentResolver();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 300";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor cur = cr.query(callUri, null, null, null, strOrder);
        boolean hasEntries = (cur != null ? cur.getCount() : 0) >0;
        if (cur != null) {
            cur.close();
        }
        return hasEntries;
    }

    @Override
    public boolean hasDatabaseCalllogEntries() {
        List<CallLog> contacts = SQLite.select().from(CallLog.class).queryList();
        return !contacts.isEmpty();
    }

    @Override
    public FlowQueryList<CallLog> getCallLog() {
        return SQLite.select().from(CallLog.class)
                .orderBy(CallLog_Table.callDate, false).flowQueryList();
    }

    @Override
    public void readPhoneCalllog() {
        TaskReadCallLog taskReadCallLog = new TaskReadCallLog();
        taskReadCallLog.listener = this;
        taskReadCallLog.execute(context);
    }

    @Override
    public  List<CallLogResume> getCallsResume() {
        return SQLite.select(CallLog_Table.name, CallLog_Table.callDate,
                CallLog_Table.callDuration, CallLog_Table.callDirection, CallLog_Table.number,
                Method.count(CallLog_Table.number).as("number_calls")
                ).from(CallLog.class)
                .orderBy(CallLog_Table.callDate, false)
                .groupBy(CallLog_Table.name)
                .queryCustomList(CallLogResume.class);
    }

    @Override
    public void onReadCallLogFinish(ArrayList<CallLog> callLogs) {
        ProcessModelTransaction<CallLog> modelTransaction = new ProcessModelTransaction.Builder<>(
                new ProcessModelTransaction.ProcessModel<CallLog>(){
                    @Override
                    public void processModel(CallLog model) {
                            model.save();
                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<CallLog>() {
            @Override
            public void onModelProcessed(long current, long total, CallLog modifiedModel) {
                if(current+1==total){

                    eventBus.post(new CallLogEvent(getCallsResume(), false));
                }
            }
        }).addAll(callLogs).build();
        if(!callLogs.isEmpty()) {
            Transaction transaction = FlowManager.getDatabase(OperatorDB.class).beginTransactionAsync(modelTransaction).build();
            transaction.execute();
        }
    }
}
