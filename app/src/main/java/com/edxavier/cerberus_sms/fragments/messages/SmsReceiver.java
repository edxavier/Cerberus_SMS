package com.edxavier.cerberus_sms.fragments.messages;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.DetailConversation;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.Constans;
import com.edxavier.cerberus_sms.helpers.NotificationPendindIntentSetupDefaultSMS;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 24/10/2015.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String MSG_TYPE=intent.getAction();
        Log.e("EDER", MSG_TYPE);
        if(MSG_TYPE.equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();

            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[0];
            if (messages != null) {
                smsMessage = new SmsMessage[messages.length];
            }
            for (int n = 0; n < messages.length; n++)
            {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }
            String incomingNum = Utils.formatPhoneNumber(smsMessage[0].getOriginatingAddress());
            String sender;
            sender = Utils.getContactName(incomingNum);
            Message message = new Message();
            message.setMsg_type(Constans.MESSAGE_TYPE_INBOX);
            message.setMsg_date(new Date());
            message.setMsg_body(smsMessage[0].getMessageBody());
            message.setSender_number(incomingNum);
            message.setSender_name(sender);
            message.save();
             //abortBroadcast();
        }
        else if(MSG_TYPE.equals("android.provider.Telephony.SEND_SMS"))
        {
            Toast toast = Toast.makeText(context,"SMS SENT: "+MSG_TYPE, Toast.LENGTH_LONG);
            toast.show();
            //abortBroadcast();
        }
        else
        {
            abortBroadcast();
        }

    }
}

