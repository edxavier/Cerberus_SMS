package com.edxavier.cerberus_sms.fragments.checkOperator;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.AppOperator;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.ContactAdapter;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorPresenter;
import com.edxavier.cerberus_sms.fragments.checkOperator.contracts.CheckOperatorView;
import com.edxavier.cerberus_sms.fragments.checkOperator.di.CheckoperatorComponent;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.melnykov.fab.FloatingActionButton;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOperatorFrg extends Fragment implements CheckOperatorView, TextWatcher {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 2;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 3;
    @Inject
    CheckOperatorPresenter presenter;
    @Inject
    FirebaseAnalytics analytics;
    Bundle analitycParams;

    @Bind(R.id.autoCompleDestiny)
    AutoCompleteTextView autoCompleDestiny;
    @Bind(R.id.lbl_num_operador)
    TextViewHelper lblNumOperador;
    @Bind(R.id.lbl_num_area)
    TextViewHelper lblNumArea;
    @Bind(R.id.lbl_num_country)
    TextViewHelper lblNumCountry;
    @Bind(R.id.adViewNative)
    NativeExpressAdView adViewNative;
    @Bind(R.id.flagImg)
    ImageView flagImg;
    @Bind(R.id.card_check_result)
    CardView cardCheckResult;
    @Bind(R.id.CheckContainer)
    LinearLayout CheckContainer;
    @Bind(R.id.share_fab)
    FloatingActionButton shareFab;
    @Bind(R.id.rate_fab)
    FloatingActionButton rateFab;
    boolean outGoingCall = false;

    public CheckOperatorFrg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_check_operador, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        analitycParams = new Bundle();
        setupAds();
        setupInjection();
        verifyDBinitialization();
        checkPermissions();
        checkCallPerms();
        autoCompleDestiny.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf"));
    }

    void checkCallPerms(){
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            outGoingCall = false;
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            analitycParams.putString("perms", "Verificar permiso para Android M");
            analytics.logEvent("android_m_request_perm", analitycParams);
            if (!Settings.canDrawOverlays(getActivity())) {
                new MaterialDialog.Builder(getContext())
                        .title(R.string.info)
                        .content(R.string.alert_info)
                        .positiveText(R.string.accept)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getActivity().getPackageName()));
                                startActivityForResult(intent, REQUEST_SYSTEM_ALERT_WINDOW);
                            }
                        })
                        .show();
            }
        }
    }

    private void initContacts() {
        if (presenter.isReadContactsPermsGranted()) {
            presenter.syncronizeContacts();
        } else {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.save_info)
                    .content(R.string.contact_perms_info)
                    .positiveText(R.string.accept)
                    .negativeText(R.string.cancelar)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    })
                    .show();
        }
    }

    private void verifyDBinitialization() {
        if (!presenter.checkDBinitialization()) {
            presenter.dbInitialization();
            initContacts();
            setupAutocomplete();
        } else {
            initContacts();
            setupAutocomplete();
        }
    }

    private void setupAutocomplete() {

        List<PersonalContact> result = SQLite.select().from(PersonalContact.class).queryList();

        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,result);
        ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.create_message_activity, R.id.lbl_contact_name_auto_complete, result);
        autoCompleDestiny.setThreshold(2);
        autoCompleDestiny.setAdapter(adapter);
        autoCompleDestiny.addTextChangedListener(this);
    }

    private void setupInjection() {
        AppOperator app = (AppOperator) getActivity().getApplication();
        CheckoperatorComponent component = app.getCheckoperatorComponent(this, this);
        component.inject(this);
    }

    @Override
    public AreaCode checkOperator(String phoneNumber) {
        return presenter.checkOperator(phoneNumber);
    }

    @Override
    public void showElements(boolean show) {

    }

    @Override
    public void setResult() {

    }

    //#####################
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_msg:
                if (autoCompleDestiny.getText().toString().length() > 2) {
                    Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + autoCompleDestiny.getText().toString().replaceAll("\\s+", "")));
                    intent2.putExtra("sms_body", "");
                    startActivity(intent2);
                } else {
                    snackbar(getResources().getString(R.string.sms_msg_atempt));
                }
                return true;

            case R.id.action_new_call:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            outGoingCall = true;
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                PERMISSIONS_REQUEST_CALL_PHONE);
                        break;
                    }
                }
                if (autoCompleDestiny.getText().toString().length() >= 3) {
                    String uri = "tel:" + autoCompleDestiny.getText().toString().replaceAll("\\s+", "");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } else {
                    snackbar(getResources().getString(R.string.call_msg_atempt));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void snackbar(String msg) {
        Snackbar.make(CheckContainer, msg, Snackbar.LENGTH_LONG).show();
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewNative.loadAd(adRequest);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        AreaCode res = checkOperator(autoCompleDestiny.getText().toString());
        if (res != null) {
            if (cardCheckResult.getVisibility() == View.GONE) {
                showAnimation();
            }
            flagImg.setImageDrawable(getResources().getDrawable(res.getFlag()));
            lblNumOperador.setText(res.getArea_operator());
            lblNumArea.setText(res.getArea_name());
            lblNumCountry.setText(res.getCountry_name());
            if (autoCompleDestiny.getText().toString().startsWith("+505") && autoCompleDestiny.getText().toString().length() > 7)
                lblNumArea.setText(res.getArea_name());
            if (autoCompleDestiny.getText().toString().length() > 3 && !autoCompleDestiny.getText().toString().startsWith("+505"))
                lblNumArea.setText(res.getArea_name());

            switch (res.getArea_operator()) {
                case "Claro":
                    lblNumOperador.setTextColor(getResources().getColor(R.color.md_red_600));
                    break;
                case "Movistar":
                    lblNumOperador.setTextColor(getResources().getColor(R.color.md_green_700));
                    break;
                case "CooTel":
                    lblNumOperador.setTextColor(getResources().getColor(R.color.md_amber_700));
                    break;
            }

        } else {
            if (cardCheckResult.getVisibility() == View.VISIBLE) {
                hideAnimation();
            }
            lblNumOperador.setText("");
            lblNumArea.setText("");
            lblNumCountry.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }


    void showAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_save);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardCheckResult.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        cardCheckResult.startAnimation(anim);
    }

    void hideAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_dismiss);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardCheckResult.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        cardCheckResult.startAnimation(anim);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Snackbar.make(CheckContainer, getResources().getString(R.string.sync_contacts_msg), Snackbar.LENGTH_SHORT).show();
                    presenter.syncronizeContacts();
                }
                break;
            }
            case PERMISSIONS_REQUEST_CALL_PHONE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(outGoingCall) {
                        if (autoCompleDestiny.getText().toString().length() >= 3) {
                            String uri = "tel:" + autoCompleDestiny.getText().toString().replaceAll("\\s+", "");
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            startActivity(intent);
                        } else {
                            snackbar(getResources().getString(R.string.call_msg_atempt));
                        }
                    }
                }
                break;
            }

        }
    }

    @OnClick({R.id.share_fab, R.id.rate_fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_fab:
                try {
                    analitycParams.putString("share_fab", "Compartir desde Fab Btn");
                    analytics.logEvent("share_fab_button", analitycParams);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = getResources().getString(R.string.share_app_msg);
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+ getContext().getPackageName()+" \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, getResources().getString(R.string.share_using)));
                }
                catch(Exception e)
                { //e.toString();
                    Log.e("EDER", e.getMessage());
                }
                break;
            case R.id.rate_fab:
                analitycParams.putString("rate_fab", "Valorar desde Fab Btn");
                analytics.logEvent("rate_fab_button", analitycParams);
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to refresh following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
                break;
        }
    }
}
