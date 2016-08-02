package com.edxavier.cerberus_sms.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.MainActivity;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.ContactAdapter;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 15/10/2015.
 */
public class OperadorFragment extends Fragment implements TextWatcher {

    EditText numero;
    private AutoCompleteTextView autoComplete;
    TextViewHelper operador, pais, area;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_check_operador, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Tracker tracker = ((MainActivity)getActivity()).getTracker();
        GoogleAnalytics.getInstance(getContext()).reportActivityStart(getActivity());

        tracker.setScreenName("Operador fragment");
        autoComplete = (AutoCompleteTextView)  getActivity().findViewById(R.id.autoCompleDestiny);
        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        //numero = (EditText) getActivity().findViewById(R.id.text_phone_num);
        operador = (TextViewHelper) getActivity().findViewById(R.id.lbl_num_operador);
        pais = (TextViewHelper) getActivity().findViewById(R.id.lbl_num_country);
        area = (TextViewHelper) getActivity().findViewById(R.id.lbl_num_area);
        operador.setRobotoBold();
        //numero.addTextChangedListener(this);

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        List<Contactos> result = (new Select().from(Contactos.class).orderBy("nombre ASC").execute());

        //for(Contactos c: result)

        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,result);
        //ContactAdapter adapter = new ContactAdapter(getActivity(),R.layout.create_message_activity,R.id.lbl_contact_name,result);

        autoComplete.setThreshold(2);
        //autoComplete.setAdapter(adapter);
        autoComplete.addTextChangedListener(this);
        //autoComplete.setOnItemClickListener(getActivity());


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_msg:
                if(autoComplete.getText().toString().length()>2) {
                    Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + autoComplete.getText().toString().replaceAll("\\s+", "")));
                    intent2.putExtra("sms_body", "");
                    startActivity(intent2);
                }else{
                    Toast.makeText(getActivity(),"Ingrese al menos 3 digitos", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.action_new_call:
                if(autoComplete.getText().toString().length()>=3) {
                    String uri = "tel:" + autoComplete.getText().toString().replaceAll("\\s+", "");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"Ingrese al menos 3 digitos", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(getContext()).reportActivityStop(getActivity());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        AreaCode res = Utils.getOperadoraV3(autoComplete.getText().toString(), getContext());
        if(res!=null) {
            Tracker tracker = ((MainActivity)getActivity()).getTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Operator search")
                    .setAction("write number")
                    .setLabel("Operator search")
                    .build());

            operador.setText(res.getArea_operator());
            pais.setText(res.getCountry_name());
            if(autoComplete.getText().toString().startsWith("+505") && autoComplete.getText().toString().length()>7)
                area.setText(res.getArea_name());
            if(autoComplete.getText().toString().length()>3 && !autoComplete.getText().toString().startsWith("+505"))
                    area.setText(res.getArea_name());

            if(res.getArea_operator().equals("Claro"))
                operador.setTextColor(getResources().getColor(R.color.md_red_500));
            else if(res.getArea_operator().equals("Movistar"))
                operador.setTextColor(getResources().getColor(R.color.md_green_500));
        }
        else {
            operador.setText("");
            pais.setText("");
            area.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
