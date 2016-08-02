package com.edxavier.cerberus_sms.fragments.contacts.impl;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.OperatorDB;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.ContactEntry;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.db.entities.PersonalContact_Table;
import com.edxavier.cerberus_sms.fragments.CheckOperatorEvent;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsService;
import com.edxavier.cerberus_sms.fragments.contacts.tasks.OnContactsReadyListener;
import com.edxavier.cerberus_sms.fragments.contacts.tasks.TaskReadContacts;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.mLibs.EventBusIface;
import com.edxavier.cerberus_sms.mLibs.GrobotEventbus;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 09/07/2016.
 */
public class ContactsServiceImpl implements ContactsService, OnContactsReadyListener {
    private EventBusIface eventBus;
    private Context context;

    public ContactsServiceImpl(EventBusIface eventBus, Context context) {
        this.eventBus = eventBus;
        this.context = context;
    }

    @Override
    public void loadContacts() {
            TaskReadContacts taskReadContacts = new TaskReadContacts();
            taskReadContacts.listener = this;
            taskReadContacts.execute(context);
    }

    @Override
    public FlowQueryList<PersonalContact> searchContact(String contactName) {
        return SQLite.select()
                .from(PersonalContact.class)
                .where(PersonalContact_Table.contact_name.like(contactName+"%"))
                .flowQueryList();
    }

    @Override
    public FlowQueryList<PersonalContact> getAllContacts() {
        return SQLite.select()
                .from(PersonalContact.class)
                .flowQueryList();
    }

    @Override
    public boolean saveContact(PersonalContact contact, boolean persistOnPhone) {
        contact.setContact_phone_number(
                Utils.formatPhoneNumber(contact.getContact_phone_number()));
        if(!hasRecords()&&!persistOnPhone){
            return saveContactonPhone(contact);
        }else if(hasRecords() && persistOnPhone){
            boolean saved = saveContactonPhone(contact);
            if(saved) {
                contact.save();
            }
            return saved;
        }else {
            contact.save();
            return true;
        }
    }

    @Override
    public boolean deleteContact(PersonalContact contact, boolean hardDelete) {
        if(hardDelete) {
            final ArrayList ops = new ArrayList();
            final ContentResolver cr = context.getContentResolver();
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contact.getContact_id()})
                    .build());
            try {
                cr.applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
                contact.delete();
                return true;
            } catch (OperationApplicationException e) {
                alert("Operation Application Exception");
                return false;
            } catch (RemoteException e) {
                alert("Remote Exception");
                return false;
            }catch (Exception e){
                alert(context.getResources().getString(R.string.delete_contact_error));
                return false;
            }

        }else {
            contact.delete();
            return true;
        }
    }

    @Override
    public boolean hasRecords() {
        List<PersonalContact> contacts = SQLite.select().from(PersonalContact.class).queryList();
        return !contacts.isEmpty();
    }

    @Override
    public boolean hasPhonebookRecords() {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        boolean hasRecords =(cursor != null ? cursor.getCount() : 0) >0;
        if (cursor != null) {
            cursor.close();
        }
        return hasRecords;
    }

    @Override
    public boolean canReadContacts() {
        int hasPerm = context.getPackageManager().checkPermission(Manifest.permission.READ_CONTACTS,
                        context.getPackageName());
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean canWriteContacts() {
        int hasPerm = context.getPackageManager().checkPermission(Manifest.permission.WRITE_CONTACTS,
                context.getPackageName());
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onContactsReady(ArrayList<PersonalContact> contacts) {
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
                if(current+1==total){
                    postContacts();
                }else if(total==0){
                    postContacts();
                }
            }
        }).addAll(contacts).build();
        if(!contacts.isEmpty()) {
            Transaction transaction = FlowManager.getDatabase(OperatorDB.class).beginTransactionAsync(modelTransaction).build();
            transaction.execute();
        }
    }

    private void postContacts(){
        FlowQueryList<PersonalContact> list = null;
        if(!SQLite.select().from(PersonalContact.class).queryList().isEmpty()) {
            try {
                list = SQLite.select()
                        .from(PersonalContact.class).orderBy(PersonalContact_Table.contact_name, true)
                        .flowQueryList();
            } catch (ExceptionInInitializerError | Exception ignored) {
            }
        }
        CheckOperatorEvent event = new CheckOperatorEvent(CheckOperatorEvent.OK,null, list);
        event.setErrorType(CheckOperatorEvent.CONTACTS_EVENT);
        eventBus.post(event);
    }

    void alert(String content){
        new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.delete_contact_warning))
                .content(content)
                .positiveText(context.getResources().getString(R.string.dialog_ok))
                .show();
    }

    boolean saveContactonPhone(PersonalContact contact){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        contact.getContact_name()).build());

        //------------------------------------------------------ Mobile Number
        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getContact_phone_number())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        // Asking the Contact provider to create a new contact
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean contactExist(String number){
        return SQLite.select()
                .from(PersonalContact.class)
                .where(PersonalContact_Table.contact_phone_number.eq(number))
                .querySingle() != null;
    }
}

