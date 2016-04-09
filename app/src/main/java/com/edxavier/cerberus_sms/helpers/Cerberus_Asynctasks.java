package com.edxavier.cerberus_sms.helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.AreaCode;

/**
 * Created by Eder Xavier Rojas on 20/10/2015.
 */
public class Cerberus_Asynctasks extends AsyncTask<Context, Void, Void> {


    @Override
    protected Void doInBackground(Context... params) {

        if(new Select().from(AreaCode.class).count()<=0) {
            //Utils.loadContacts(params[0]);
            AreaCodeHelper.initAreaCodes(params[0]);
        }
        //new Delete().from(Contactos.class).execute();
        //new Delete().from(Call_Log.class).execute();
        //Utils.loadInboxContacts(params[0]);
        //Utils.getCallLogs(params[0]);
        return null;
    }

}
