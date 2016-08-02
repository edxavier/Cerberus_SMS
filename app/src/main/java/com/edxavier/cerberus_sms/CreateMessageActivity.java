package com.edxavier.cerberus_sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.db.models.InboxSms;
import com.edxavier.cerberus_sms.helpers.Constans;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class CreateMessageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView autoComplete;
    private EditText msgBody;

    String mensaje, destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_message_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleDestiny);
        msgBody = (EditText) findViewById(R.id.msg_body);

        try {
            Tracker tracker = ((AnalyticsTrackers) getApplication()).getTracker();
            GoogleAnalytics.getInstance(this).reportActivityStart(this);

            tracker.setScreenName("New message activity");
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = getIntent();

        try {
            String number = intent.getDataString().split(":")[1];
            String msg = intent.getStringExtra("sms_body");
            autoComplete.setText(number);
            destino = number;
            mensaje = msg;
            msgBody.setText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Contactos> result = (new Select().from(Contactos.class).orderBy("nombre ASC").execute());

        //for(Contactos c: result)

        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,result);
        //ContactAdapter adapter = new ContactAdapter(this,R.layout.create_message_activity,R.id.lbl_contact_name,result);

        autoComplete.setThreshold(2);
        //autoComplete.setAdapter(adapter);
        autoComplete.setOnItemClickListener(this);
        autoComplete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getKeyCode() == 67) {
                    if(autoComplete.getText().toString().length()<2)
                        destino = "";
                }

                return false;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contactos contacto = (Contactos) parent.getItemAtPosition(position);
        destino = contacto.getNumero();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds  items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_delete_contact:
                if(Utils.isInteger(autoComplete.getText().toString().replace("+",""))
                        && autoComplete.getText().toString().length()>=2) {
                    destino = autoComplete.getText().toString();
                }
                if(destino != null && destino.length() > 1 ){
                    String SENT = "SMS_SENT";
                    final Context cntx = getApplicationContext();

                    PendingIntent sentPI = PendingIntent.getBroadcast(cntx, 0,
                            new Intent(SENT), 0);
                    //---when the SMS has been sent---
                    registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context arg0, Intent intent) {
                            InboxSms sms;
                            String MSG_TYPE = intent.getAction();
                            int type = -1;
                            ContentValues values = new ContentValues();
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    Toast.makeText(getBaseContext(), "Mensaje enviado",
                                            Toast.LENGTH_SHORT).show();
                                    type = Constans.MESSAGE_TYPE_SENT;
                                    values.put("address", destino.replaceAll("\\s+", ""));
                                    values.put("body", mensaje);
                                    values.put("type", String.valueOf(type));
                                    values.put("date", System.currentTimeMillis());
                                    getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                                    break;
                                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                    Toast.makeText(getBaseContext(), "Fallo al enviar",
                                            Toast.LENGTH_LONG).show();
                                    type = Constans.MESSAGE_TYPE_FAILED;
                                    values.put("address", destino.replaceAll("\\s+", ""));
                                    values.put("body", mensaje);
                                    values.put("type", String.valueOf(type));
                                    values.put("date", System.currentTimeMillis());
                                    getContentResolver().insert(Uri.parse("content://sms/failed"), values);
                                    break;
                                case SmsManager.RESULT_ERROR_NO_SERVICE:
                                    Toast.makeText(getBaseContext(), "No service",
                                            Toast.LENGTH_SHORT).show();
                                    type = Constans.MESSAGE_TYPE_FAILED;
                                    values.put("address", destino.replaceAll("\\s+", ""));
                                    values.put("body", mensaje);
                                    values.put("type", String.valueOf(type));
                                    values.put("date", System.currentTimeMillis());
                                    getContentResolver().insert(Uri.parse("content://sms/failed"), values);
                                    break;
                                case SmsManager.RESULT_ERROR_NULL_PDU:
                                    Toast.makeText(getBaseContext(), "Fallo al enviar",
                                            Toast.LENGTH_SHORT).show();
                                    type = Constans.MESSAGE_TYPE_FAILED;
                                    values.put("address", destino.replaceAll("\\s+", ""));
                                    values.put("body", mensaje);
                                    values.put("type", String.valueOf(type));
                                    values.put("date", System.currentTimeMillis());
                                    getContentResolver().insert(Uri.parse("content://sms/failed"), values);
                                    break;
                                case SmsManager.RESULT_ERROR_RADIO_OFF:
                                    Toast.makeText(getBaseContext(), "Fallo al enviar",
                                            Toast.LENGTH_SHORT).show();
                                    type = Constans.MESSAGE_TYPE_FAILED;
                                    values.put("address", destino.replaceAll("\\s+", ""));
                                    values.put("body", mensaje);
                                    values.put("type", String.valueOf(type));
                                    values.put("date", System.currentTimeMillis());
                                    getContentResolver().insert(Uri.parse("content://sms/failed"), values);
                                    break;
                            }

                        }
                    }, new IntentFilter(SENT));

                    mensaje  = msgBody.getText().toString();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(destino.replaceAll("\\s+", ""), null, mensaje, sentPI, null);
                    msgBody.setText("");
                    autoComplete.setText("");
                    destino="";
                    mensaje="";
                }else {
                    Toast.makeText(this,"Especifique el destinatario", Toast.LENGTH_LONG).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
