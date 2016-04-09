package com.edxavier.cerberus_sms.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.MainActivity;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.adapters.BlackListAdapter;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.edxavier.cerberus_sms.interfaces.SmsBlackListsInterface;
import com.edxavier.cerberus_sms.tareas.AsyncBlackListSMS;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmsBlackListFRG extends Fragment implements View.OnClickListener, SmsBlackListsInterface, BlackListAdapter.AdapterInterfaceListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Sms_Lock> contactos;
    private BlackListAdapter adapter;
    AlphaInAnimationAdapter alphaAdapter;
    private TextViewHelper emptyMessage;
    private static final int  PICK_CONTACT = 1;
    AsyncBlackListSMS asyncBlackListSMS =  new AsyncBlackListSMS();
    MaterialDialog pgd;


    public SmsBlackListFRG() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_sms_black_list_frg, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Tracker tracker = ((MainActivity)getActivity()).getTracker();
        tracker.setScreenName("block list fragment");
        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        pgd = new MaterialDialog.Builder(getContext())
                //.title(R.string.progress_dialog)
                .content(getResources().getString(R.string.cargando))
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();

        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = Utils.md5(android_id).toUpperCase();

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("45D8AEB3B66116F8F24E001927292BD5").build();
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("3C02B326B054DB3B2ECAB1E1B23DBFC0").build();
        mAdView.loadAd(adRequest);

        asyncBlackListSMS.delegate = this;
        asyncBlackListSMS.execute(getActivity());

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id._recycler_sms_black_list);
        emptyMessage = (TextViewHelper) getActivity().findViewById(R.id.empty_blaclist_message);
        mRecyclerView.setHasFixedSize(true);
        FadeInRightAnimator animator = new FadeInRightAnimator();
        animator.setAddDuration(400);
        animator.setMoveDuration(500);
        mRecyclerView.setItemAnimator(animator);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

            mRecyclerView.setLayoutManager(mLayoutManager);


            //registerForContextMenu(mRecyclerView);
            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.attachToRecyclerView(mRecyclerView);
            fab.setOnClickListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_inbox, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            //case R.id.action_search:
              //  ((HomeActivity)getActivity()).showDatepickerDialog();
                //Intent intent = new Intent(getActivity(), SearchByDate.class);
                //startActivity(intent);
        }

            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Tracker tracker = ((MainActivity)getActivity()).getTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("Add from contacts intent")
                .build());
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT);
        /*
        String [] choices = getActivity().getResources().getStringArray(R.array.bl_entry_arrays);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getResources().getString(R.string.add_bl_tittle));

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (which) {
                    case 0:
                        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(pickContactIntent, PICK_CONTACT);
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(android.provider.CallLog.Calls.CONTENT_URI);
                        startActivityForResult(intent, 9);
                        break;
                }
            }
        });
        builder.show();
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the URI that points to the selected contact
                    Uri contactUri = data.getData();
                    // We only need the NUMBER column, because there will be only one row in the result
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    if(cursor!=null) {
                        cursor.moveToFirst();

                        // Retrieve the phone number from the NUMBER column
                        int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int column2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String phoneNumber = cursor.getString(column);
                        String name = cursor.getString(column2);
                        phoneNumber = phoneNumber.replaceAll("-", "").replaceAll("\\s+", "");
                        if (phoneNumber.length() == 12)
                            phoneNumber = String.format("%s %s %s", phoneNumber.substring(0, 4), phoneNumber.substring(4, 8),
                                    phoneNumber.substring(8, 12));
                        else if (phoneNumber.length() == 8)
                            phoneNumber = String.format("%s %s", phoneNumber.substring(0, 4), phoneNumber.substring(4, 8));

                        Sms_Lock lock = new Sms_Lock(name, phoneNumber);
                        if (lock.save() >= 1) {
                            adapter.addItem(lock);
                            emptyMessage.setVisibility(View.GONE);
                            alphaAdapter.notifyItemInserted(0);
                        }
                        else
                            Toast.makeText(getContext(), name+" "+ getActivity().getResources().getString(R.string.added_blacklist), Toast.LENGTH_LONG).show();
                        emptyMessage.setVisibility(View.GONE);
                    }

                }
                break;
        }
    }

    @Override
    public void loadBlackListFinish(ArrayList<Sms_Lock> sms_locks) {
        contactos  = sms_locks;
        if(contactos.size()<=0)
            emptyMessage.setVisibility(View.VISIBLE);

        adapter = new BlackListAdapter(contactos, R.layout.sms_black_list_row, this);
        alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(500);
        mRecyclerView.setAdapter(alphaAdapter);
        pgd.dismiss();
    }

    @Override
    public void OnAdapterItemClick(int position) {
        String  nombre  = adapter.removeItem(position);
        alphaAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(),getContext().getResources().getString(R.string.delete_blacklist)+" "+nombre, Toast.LENGTH_LONG).show();
        if(adapter.getItemCount()<=0)
            emptyMessage.setVisibility(View.VISIBLE);
    }
}
