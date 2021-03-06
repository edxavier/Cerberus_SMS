package com.edxavier.cerberus_sms.fragments.callLog;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.db.entities.Message;
import com.edxavier.cerberus_sms.fragments.callLog.adapter.AdapterCallLog;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogPresenter;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogView;
import com.edxavier.cerberus_sms.fragments.callLog.di.CallLogComponent;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
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
public class CallLogFragment extends Fragment implements CallLogView {

    private static final int PERMISSIONS_REQUEST_READ_CALLS = 1;
    @Inject
    CallLogPresenter presenter;
    @Inject
    FirebaseAnalytics analytics;
    Bundle analitycParams;


    AdapterCallLog adapter;
    SlideInBottomAnimationAdapter slideAdapter;


    @Bind(R.id.swipeCallLog)
    SwipeRefreshLayout swipeCallLog;
    @Bind(R.id.empty_call_log_message)
    TextViewHelper emptyCallLogMessage;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.empty_list_layout)
    LinearLayout emptyListLayout;
    @Bind(R.id.recycler_call_list)
    RecyclerView recyclerCallList;
    @Bind(R.id.loading_layout)
    LinearLayout loadingLayout;
    @Bind(R.id.container)
    FrameLayout container;

    public CallLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeCallLog.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCalllog();
            }
        });
        setupInjection();
        loadCalllog();
        setupAds();
        analitycParams = new Bundle();
        analitycParams.putString("Fragment", "Fragmento Call log");
        analytics.logEvent("Call_log", analitycParams);
    }

    @Override
    public void setupInjection() {
        AppOperator app = (AppOperator) getActivity().getApplication();
        CallLogComponent component = app.getCallLogComponent(this, this);
        component.inject(this);
    }

    @Override
    public void setupRecycler(List<CallLogResume> callLogs) {
        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            //Normal
            recyclerCallList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else{
            //code for landscape mode Acostado
            recyclerCallList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerCallList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        }

        //recyclerCallList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCallList.setHasFixedSize(true);
        adapter = new AdapterCallLog(callLogs, getActivity(), presenter);
        slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(1f));
        recyclerCallList.setItemAnimator(new FlipInBottomXAnimator());
        recyclerCallList.getItemAnimator().setAddDuration(500);
        recyclerCallList.setAdapter(slideAdapter);
        swipeCallLog.setRefreshing(false);
    }

    @Override
    public void loadCalllog() {
        if (presenter.hasReadCalllogPermission()) {
            if (!presenter.hasDatabaseCalllogEntries()) {
                if (presenter.hasPhoneCalllogEntries()) {
                    presenter.readPhoneCalllog();
                } else {
                    showProgress(false);
                    showEmptyMsg(true);
                }
            } else {
                setCalllogList(presenter.getCallsResume());
            }
        } else {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.info)
                    .content(R.string.calls_perms_info)
                    .positiveText(R.string.accept)
                    .negativeText(R.string.cancelar)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.READ_CALL_LOG},
                                    PERMISSIONS_REQUEST_READ_CALLS);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showEmptyMsg(true);
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
    public void setCalllogList(List<CallLogResume> calls) {
        setupRecycler(calls);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALLS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Snackbar.make(container, getResources().getString(R.string.sync_msg), Snackbar.LENGTH_SHORT).show();
                    loadCalllog();
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

                analitycParams.putString("Fragment", "Clear Call log");
                analytics.logEvent("Call_log_clear_record", analitycParams);
                new MaterialDialog.Builder(getContext())
                        .title(R.string.delete_records)
                        .content(R.string.delete_records_warning)
                        .positiveText(R.string.continue_delete)
                        .negativeText(R.string.cancelar)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                try {
                                    SQLite.delete(CallLog.class).execute();
                                    adapter.removeAll();
                                    adapter.notifyDataSetChanged();
                                    slideAdapter.notifyDataSetChanged();
                                }catch (Exception ignored){}
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
