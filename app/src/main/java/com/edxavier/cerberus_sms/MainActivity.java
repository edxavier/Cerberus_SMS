package com.edxavier.cerberus_sms;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.db.models.AreaCode;
import com.edxavier.cerberus_sms.fragments.AcercadeFragment;
import com.edxavier.cerberus_sms.fragments.Call_Log_fragment;
import com.edxavier.cerberus_sms.fragments.Contacts_fragment;
import com.edxavier.cerberus_sms.fragments.Inbox_fragment;
import com.edxavier.cerberus_sms.fragments.OperadorFragment;
import com.edxavier.cerberus_sms.helpers.Cerberus_Asynctasks;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private OperadorFragment frgsmsOS = null;

    ActionBar actionBar;
    TextViewHelper textView;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            Transition exitTrans = new Fade();
            getWindow().setExitTransition(exitTrans);
            Transition reenterTrans = new Fade();
            getWindow().setReenterTransition(reenterTrans);

        }




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.drawer_operator_menu);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);

        fragmentManager = getSupportFragmentManager();
        frgsmsOS = new OperadorFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, frgsmsOS, "clima")
                .commit();

        /*
        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);

        String countryCodeValue = tm.getNetworkCountryIso();
        String simCodeValue = tm.getSimCountryIso();
        String simCodeValue2 = tm.getSimOperator();
        String simCodeValue3 = tm.getSimOperatorName();

        Log.d("EDER_CCV", countryCodeValue);
        Log.d("EDER_CCV2", simCodeValue);
        Log.d("EDER_CCV3", simCodeValue2);
        Log.d("EDER_CCV4", simCodeValue3);
        Log.d("EDER_CCV4", simCodeValue3);*/

        //Cerberus_Asynctasks tarea = new Cerberus_Asynctasks();
        if(new Select().from(AreaCode.class).count()<=0) {
            //AsyncContacts asyncContacts = new AsyncContacts();
            Cerberus_Asynctasks asynctasks = new Cerberus_Asynctasks();
            Toast.makeText(this,"Inicializando datos de aplicacion...",Toast.LENGTH_LONG).show();
            asynctasks.execute(this);
            //asyncContacts.execute(this);
            /*SweetAlertDialog pgdialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pgdialog.setTitleText("Inicializando...");
            pgdialog.setCancelable(false);
            pgdialog.setContentText("Espera por favor...");
            pgdialog.show();*/

            //AreaCodeHelper.initAreaCodes(this, pgdialog);
        }

        tracker = ((AnalyticsTrackers) getApplication()).getTracker();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        tracker.enableAdvertisingIdCollection(true);

        // Start a new session with the hit.
        tracker.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //textView = (TextViewHelper) findViewById(R.id.textView2);
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                toolbar.setTitle(menuItem.getTitle());
                                Inbox_fragment inbox = new Inbox_fragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, inbox)
                                        .commit();
                                return true;

                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Contacts_fragment contacts_fragment = new Contacts_fragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, contacts_fragment)
                                        .commit();
                                //Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                //startActivity(intent);
                                return true;
                            /*case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                 SmsBlackListFRG smsBlackListFRG = new SmsBlackListFRG();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, smsBlackListFRG)
                                        .commit();
                                return true;*/
                            case R.id.item_navigation_drawer_call_log:
                                menuItem.setChecked(true);
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Call_Log_fragment callLogFragment = new Call_Log_fragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, callLogFragment)
                                        .commit();
                                return true;

                            case R.id.item_navigation_drawer_operador:
                                menuItem.setChecked(true);
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                OperadorFragment operadorFragment = new OperadorFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, operadorFragment)
                                        .commit();
                                return true;

                            case R.id.item_navigation_drawer_acerca_de:
                                menuItem.setChecked(true);
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                AcercadeFragment acercadeFragment = new AcercadeFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainer, acercadeFragment)
                                        .commit();
                                return true;

                        }
                        return true;
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isDefaultSmsApp(Context context) {
        return context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context));
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public Tracker getTracker() {
        return tracker;
    }
}

