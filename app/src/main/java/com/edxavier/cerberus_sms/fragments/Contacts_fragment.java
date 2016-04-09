package com.edxavier.cerberus_sms.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.activeandroid.query.Select;
import com.edxavier.cerberus_sms.MainActivity;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.AdapterContactos;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.interfaces.ContactListenerInterface;
import com.edxavier.cerberus_sms.tareas.AsyncContacts;
import com.edxavier.cerberus_sms.tareas.AsyncContacts_DB;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;


/**
 * Created by Eder Xavier Rojas on 15/10/2015.
 */
public class Contacts_fragment extends Fragment implements ContactListenerInterface {
    private RecyclerView mRecyclerView;
    private ArrayList<Contactos> contactos_list;
    private AdapterContactos adapter;
    SlideInBottomAnimationAdapter slideAdapter;
    AsyncContacts asyncContacts = new AsyncContacts();
    int initialContacts=0;
    SweetAlertDialog pgdialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contactos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Tracker tracker = ((MainActivity)getActivity()).getTracker();
        tracker.setScreenName("Contacts fragment");
        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.enableAdvertisingIdCollection(true);

        contactos_list = new ArrayList<Contactos>();
        RecyclerView.LayoutManager mLayoutManager = null;
        initialContacts = new Select().from(Contactos.class).count();
        if(initialContacts<=0) {
            asyncContacts.delegate = this;
            asyncContacts.execute(getActivity());
            mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, true);
        }else{
            AsyncContacts_DB contacts_db = new AsyncContacts_DB();
            pgdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pgdialog.setTitleText("Cargando contactos...");
            pgdialog.setCancelable(false);
            pgdialog.setContentText("Espera por favor...");
            pgdialog.show();
            contacts_db.delegate=this;
            asyncContacts.delegate = this;
            asyncContacts.execute(getActivity());
            contacts_db.execute(getActivity());
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id._recycler_contacts_list);
        //emptyMessage = (TextViewHelper) getActivity().findViewById(R.id.empty_blaclist_message);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new AdapterContactos(contactos_list, R.layout.row_contacts);
        slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(slideAdapter);

        //registerForContextMenu(mRecyclerView);
        //if(contactos.size()<=0)
          //  emptyMessage.setVisibility(View.VISIBLE);
        //mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ((HomeActivity)getActivity()).loadRecentEntries(true);
                return true;
        }
                        */

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void readContactsFinish(ArrayList<Contactos> contactos_list) {
        //List<Contactos> result = (new Select().from(Contactos.class).orderBy("nombre ASC").execute());

        adapter = new AdapterContactos(contactos_list, R.layout.row_contacts);
        slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(slideAdapter);
        pgdialog.dismissWithAnimation();

    }

    @Override
    public void onProgres(Contactos contacto) {
        if(initialContacts<=0) {
            adapter.addContacto(contacto);
            //adapter.notifyItemInserted(0);
            slideAdapter.notifyItemInserted(0);
        }
    }
}
