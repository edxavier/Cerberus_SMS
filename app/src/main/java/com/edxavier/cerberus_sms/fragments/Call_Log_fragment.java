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

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.MainActivity;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.AdapterCallLog;
import com.edxavier.cerberus_sms.db.models.Call_Log;
import com.edxavier.cerberus_sms.interfaces.CallLogInterface;
import com.edxavier.cerberus_sms.tareas.AsyncReadCallLogs;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;

/**
 * Created by Eder Xavier Rojas on 15/10/2015.
 */
public class Call_Log_fragment extends Fragment implements CallLogInterface {

    private RecyclerView mRecyclerView;
    private ArrayList<Call_Log> call_list;
    private AdapterCallLog adapter;
    SlideInRightAnimationAdapter slideinRight;
    AsyncReadCallLogs asyncReadCallLogs = new AsyncReadCallLogs();
    MaterialDialog pgd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Tracker tracker = ((MainActivity)getActivity()).getTracker();
        tracker.setScreenName("Call history fragment");
        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        asyncReadCallLogs.delegate = this;
        asyncReadCallLogs.execute(getActivity());

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id._recycler_contacts_list);
        //emptyMessage = (TextViewHelper) getActivity().findViewById(R.id.empty_blaclist_message);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        //List<Call_Log> result = (new Select().from(Call_Log.class).orderBy("fecha DESC").execute());
        call_list = new ArrayList<Call_Log>();
        adapter = new AdapterCallLog(call_list, R.layout.row_call_log);
        slideinRight = new SlideInRightAnimationAdapter(adapter);
        slideinRight.setDuration(800);
        slideinRight.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(slideinRight);

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //registerForContextMenu(mRecyclerView);
        //if(contactos.size()<=0)
        //  emptyMessage.setVisibility(View.VISIBLE);
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
    public void onProgres(Call_Log call_log) {
        adapter.addCall(call_log);
        slideinRight.notifyItemInserted(0);
    }
}
