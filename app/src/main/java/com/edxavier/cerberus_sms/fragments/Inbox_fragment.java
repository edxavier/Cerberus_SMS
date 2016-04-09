package com.edxavier.cerberus_sms.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.AnalyticsTrackers;
import com.edxavier.cerberus_sms.CreateMessageActivity;
import com.edxavier.cerberus_sms.DetailConversation;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.AdapterConversation;
import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.interfaces.TaskResponse;
import com.edxavier.cerberus_sms.tareas.AsyncReadConversations;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Eder Xavier Rojas on 15/10/2015.
 */
public class Inbox_fragment extends Fragment implements TaskResponse, AdapterConversation.ConversationClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Conversation> sms_list;
    private AdapterConversation adapter;
    ScaleInAnimationAdapter scaleInAnimationAdapter;
    AsyncReadConversations asyncReadConversations = new AsyncReadConversations();
    MaterialDialog pgd;
    CardView cardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Tracker tracker = ((AnalyticsTrackers) getActivity().getApplication()).getTracker();
        tracker.enableAdvertisingIdCollection(true);

        asyncReadConversations.delegate = this;
        asyncReadConversations.execute(getActivity());

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id._recycler_contacts_list);
        //cardView = (CardView) getActivity().findViewById(R.id.sms_warning_cardviewRow);

        //cardView.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((getActivity()),LinearLayoutManager.VERTICAL, true);

        sms_list = new ArrayList<>();
        //sms_list = Utils.loadInboxContacts(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new AdapterConversation(sms_list, R.layout.row_inbox, getActivity(), this);
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleInAnimationAdapter);

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                    !Telephony.Sms.getDefaultSmsPackage(getContext()).equals(getContext().getPackageName())) {
              //  cardView.setVisibility(View.VISIBLE);
            }//else
                //cardView.setVisibility(View.GONE);
        }catch (Exception ignored){
        }



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_msg:
                Intent intent = new Intent(getContext(), CreateMessageActivity.class);
                getActivity().startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadConversationFinish(ArrayList<Conversation> conversations) {

    }

    @Override
    public void loadCCallLogFinish() {
    }

    @Override
    public void onProgres(Conversation conversation) {
        //pgd.setProgress(progress);
        adapter.addConversation(conversation);
        adapter.notifyItemInserted(0);
        scaleInAnimationAdapter.notifyItemInserted(0);
        mRecyclerView.scrollToPosition(adapter.getItemCount());
    }

    @Override
    public void OnConversationClick(Conversation conversation, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
            Intent intent = new Intent(getContext(), DetailConversation.class);
            intent.putExtra("numero", conversation.getNumber());
            intent.putExtra("nombre", conversation.getName());
            intent.putExtra("thread", String.valueOf(conversation.getThread_id()));
            getActivity().startActivity(intent, options.toBundle());
        }else {
            Intent intent = new Intent(getContext(), DetailConversation.class);
            intent.putExtra("numero", conversation.getNumber());
            intent.putExtra("nombre", conversation.getName());
            intent.putExtra("thread", String.valueOf(conversation.getThread_id()));
            getActivity().startActivity(intent);
        }
        conversation.setNew_msg_count(0);
        adapter.updateAdapterConversation(index, conversation);
        adapter.notifyItemChanged(index);
        scaleInAnimationAdapter.notifyItemChanged(index);
    }

    @Override
    public void OnConversationDelete(int index) {
        adapter.notifyItemRemoved(index);
        scaleInAnimationAdapter.notifyItemRemoved(index);
    }


    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                !Telephony.Sms.getDefaultSmsPackage(getContext()).equals(getContext().getPackageName())) {
            //startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0)
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
            startActivity(intent);
            //cardView.setVisibility(View.GONE);
        }
    }
}
