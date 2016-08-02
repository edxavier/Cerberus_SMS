package com.edxavier.cerberus_sms.fragments.contacts;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.AppOperator;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.contacts.adapter.AdapterPersonalContact;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsPresenter;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsService;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsView;
import com.edxavier.cerberus_sms.fragments.contacts.di.ContactsComponent;
import com.edxavier.cerberus_sms.fragments.contacts.impl.ContactsServiceImpl;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.melnykov.fab.FloatingActionButton;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements ContactsView, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    AlphaInAnimationAdapter slideAdapter;

    @Bind(R.id.contacts_container)
    FrameLayout contactsContainer;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    // custom view dialog
    private EditText contact_number;
    private EditText contact_name;
    private View positiveAction;
    CheckBox persistCheck;

    @Inject
    ContactsPresenter presenter;
    @Inject
    FirebaseAnalytics analytics;
    Bundle analitycParams;

    @Bind(R.id._recycler_contacts_list)
    RecyclerView RecyclerContactsList;
    @Bind(R.id.loading_layout)
    LinearLayout loadingLayout;
    @Bind(R.id.empty_list_layout)
    LinearLayout emptyListLayout;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    FlowQueryList<PersonalContact> contacts;

    FrameLayout frameLayout;
    private AdapterPersonalContact adapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_contactos, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupInjection();
        setupRecycler();
        getContacts(false);
        setupAds();
        fab.attachToRecyclerView(RecyclerContactsList);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               syncContacts(swipeContainer.isRefreshing());
            }
        });

        frameLayout = (FrameLayout) getActivity().findViewById(R.id.fragmentContainer);
        analitycParams = new Bundle();
        analitycParams.putString("contacts_Fragment", "Fragmento Contacts");
        analytics.logEvent("contacts", analitycParams);
    }

    private void setupRecycler() {
        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            //Normal
            RecyclerContactsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else{
            //code for landscape mode Acostado
            RecyclerContactsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            RecyclerContactsList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        }
        RecyclerContactsList.setHasFixedSize(true);
        adapter = new AdapterPersonalContact(new ArrayList<PersonalContact>(), getActivity(), presenter);
        RecyclerContactsList.setAdapter(slideAdapter);
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    private void setupInjection() {
        AppOperator app = (AppOperator) getActivity().getApplication();
        ContactsComponent component = app.getContactComponent(this, this);
        component.inject(this);
    }


    @Override
    public void showLoadingMessage(boolean show) {
        if (show) {
            loadingLayout.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyListMessage(boolean show_empty_msg) {
        if (show_empty_msg) {
            emptyListLayout.setVisibility(View.VISIBLE);
        } else {
            emptyListLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setContacts(FlowQueryList<PersonalContact> contacts_list) {
        contacts = contacts_list;

        adapter = new AdapterPersonalContact(contacts, getActivity(), presenter);
        slideAdapter = new AlphaInAnimationAdapter(adapter);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(1f));
        slideAdapter.setFirstOnly(false);
        RecyclerContactsList.setItemAnimator(new FlipInBottomXAnimator());
        RecyclerContactsList.getItemAnimator().setAddDuration(500);
        RecyclerContactsList.setAdapter(slideAdapter);
        swipeContainer.setRefreshing(false);
    }


    public void syncContacts(boolean isRefreshing){
        if(presenter.canReadContacts()) {
            if(presenter.hasPhonebookRecords()) {
                presenter.loadContacts(isRefreshing);
            }else {
                showEmptyListMessage(true);
                Snackbar.make(contactsContainer,getResources().getString(R.string.empty_contacts_list),
                        Snackbar.LENGTH_LONG).show();
            }
        }else {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.save_info)
                    .content(R.string.contact_perms_info)
                    .positiveText(R.string.accept)
                    .negativeText(R.string.cancelar)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                    PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showEmptyListMessage(true);
                            Snackbar.make(contactsContainer,getResources().getString(R.string.no_read_perms),
                                    Snackbar.LENGTH_INDEFINITE).show();
                        }
                    })
                    .show();

        }

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
                    Snackbar.make(contactsContainer, getResources().getString(R.string.sync_contacts_msg), Snackbar.LENGTH_SHORT).show();
                    syncContacts(false);
                }
                break;
            }

        }
    }

    @Override
    public void getContacts(boolean isRefreshing) {
            if(presenter.hasRecords()) {
                presenter.getAllContacts();
            }else {
                syncContacts(isRefreshing);
            }

    }

    @Override
    public void searchContact(String contactName) {
        presenter.searchContact(contactName);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_contact, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getActivity().getResources().getString(R.string.search_contact_toolbar));
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 1) {
            searchContact(newText);
        } else {
            getContacts(false);
        }
        return false;
    }

    @OnClick(R.id.fab)
    public void onClick() {
        if(presenter.canWriteContacts()) {
            MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.new_contact)
                    .customView(R.layout.custom_dialog, true)
                    .positiveText(R.string.save)
                    .positiveColor(getResources().getColor(R.color.md_green_700))
                    .negativeText(android.R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            saveContact();
                        }
                    }).build();

            positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
            //noinspection ConstantConditions
            contact_number = (EditText) dialog.getCustomView().findViewById(R.id.dialog_contact_number);
            contact_name = (EditText) dialog.getCustomView().findViewById(R.id.dialog_contact_name);
            persistCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.persistCheck);

            contact_number.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
            contact_name.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));

            contact_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    positiveAction.setEnabled(s.toString().trim().length() > 3 && s.toString().trim().length() <= 20);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            dialog.show();
            positiveAction.setEnabled(false); // disabled by default
        }else {
            Snackbar.make(contactsContainer,getResources().getString(R.string.no_write_permissions),
                    Snackbar.LENGTH_INDEFINITE).show();
        }
    }


    private void saveContact() {
        if(ContactsServiceImpl.contactExist(Utils.formatPhoneNumber(contact_number.getText().toString().trim()))){
            Snackbar.make(frameLayout, getResources().getString(R.string.contact_exist), Snackbar.LENGTH_LONG).show();
            return;
        }
        String name = "";
        if (contact_name.getText().length() <= 0) {
            name = contact_number.getText().toString().trim();
        } else {
            name = contact_name.getText().toString().trim();
        }
        PersonalContact newContact = new PersonalContact(name, contact_number.getText().toString().trim(), "-1");
        if(presenter.hasRecords()) {
            //newContact.save();
            if(presenter.saveContact(newContact, persistCheck.isChecked())) {
                int index = adapter.refresh(newContact);
                if (index >= 0) {
                    slideAdapter.notifyItemInserted(index);
                }
            }
        }else {
            if(presenter.saveContact(newContact, false)) {
                getContacts(true);
            }
        }
        Snackbar.make(frameLayout, getResources().getString(R.string.contact_saved_msg), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onClose() {
        return false;
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
