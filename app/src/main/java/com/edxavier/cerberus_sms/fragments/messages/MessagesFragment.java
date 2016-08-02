package com.edxavier.cerberus_sms.fragments.messages;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.AppOperator;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.edxavier.cerberus_sms.fragments.callLog.adapter.AdapterCallLog;
import com.edxavier.cerberus_sms.fragments.callLog.di.CallLogComponent;
import com.edxavier.cerberus_sms.fragments.contacts.ContactFragment;
import com.edxavier.cerberus_sms.fragments.messages.adapter.AdapterMessages;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesPresenter;
import com.edxavier.cerberus_sms.fragments.messages.contracts.MessagesView;
import com.edxavier.cerberus_sms.fragments.messages.di.MessageComponent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements MessagesView {

    private static final int PERMISSIONS_REQUEST_READ_SMS = 1;
    @Inject
    MessagesPresenter presenter;
    @Inject
    FirebaseAnalytics analytics;
    Bundle analitycParams;


    @Bind(R.id.recyclerMessages)
    RecyclerView recyclerMessages;
    @Bind(R.id.swipeMessages)
    SwipeRefreshLayout swipeMessages;
    @Bind(R.id.loading_layout)
    LinearLayout loadingLayout;
    @Bind(R.id.empty_list_layout)
    LinearLayout emptyListLayout;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.container)
    FrameLayout container;

    AdapterMessages adapter;
    SlideInBottomAnimationAdapter slideAdapter;


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeMessages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSMS();
            }
        });
        setupInjection();
        loadSMS();
        setupAds();
        analitycParams = new Bundle();
        analitycParams.putString("messages_fragmen", "mensajes");
        analytics.logEvent("messages", analitycParams);

    }

    @Override
    public void setupInjection() {
        AppOperator app = (AppOperator) getActivity().getApplication();
        MessageComponent component = app.getMessageComponent(this, this);
        component.inject(this);
    }

    @Override
    public void loadSMS() {
        if(presenter.hasReadSMSPermission()){
            if(presenter.hasDatabaseSMSEntries()){
                setMessages(presenter.getMessagesResume());
            }else if(presenter.hasPhoneSMSEntries()){
                presenter.readSMSfromPhone();
            }else {
                showProgress(false);
                showEmptyMsg(true);
            }
        }
        else {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.save_info)
                    .content(R.string.sms_perms_info)
                    .positiveText(R.string.accept)
                    .negativeText(R.string.cancelar)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                                    PERMISSIONS_REQUEST_READ_SMS);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showEmptyMsg(true);
                            Snackbar.make(container,getResources().getString(R.string.no_read_perms),
                                    Snackbar.LENGTH_INDEFINITE).show();
                        }
                    })
                    .show();

        }
    }

    @Override
    public void setupAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    @Override
    public void showEmptyMsg(boolean show) {
        if (show) {
            emptyListLayout.setVisibility(View.VISIBLE);
        } else {
            emptyListLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            loadingLayout.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }
    }
    @Override
    public void setMessages(List<MessagesResume> messages) {

        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            //Normal
            recyclerMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else{
            //code for landscape mode Acostado
            recyclerMessages.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerMessages.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        }
        //recyclerMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMessages.setHasFixedSize(true);
        adapter = new AdapterMessages(messages);
        slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(1f));
        recyclerMessages.setItemAnimator(new FlipInBottomXAnimator());
        recyclerMessages.getItemAnimator().setAddDuration(500);
        recyclerMessages.setAdapter(slideAdapter);
        swipeMessages.setRefreshing(false);
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Snackbar.make(container, getResources().getString(R.string.sync_msg), Snackbar.LENGTH_SHORT).show();
                    loadSMS();
                }else {
                    showEmptyMsg(true);
                    Snackbar.make(container, getResources().getString(R.string.perm_denied), Snackbar.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_delete_records, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_records:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.delete_records)
                        .content(R.string.delete_records_warning)
                        .positiveText(R.string.continue_delete)
                        .negativeText(R.string.cancelar)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                try{
                                SQLite.delete(Message.class).execute();
                                adapter.removeAll();
                                adapter.notifyDataSetChanged();
                                slideAdapter.notifyDataSetChanged();}
                                catch (Exception ignored){}
                                showEmptyMsg(true);
                            }
                        })
                        .show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
