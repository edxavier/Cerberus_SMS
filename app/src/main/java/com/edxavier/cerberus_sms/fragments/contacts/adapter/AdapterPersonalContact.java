package com.edxavier.cerberus_sms.fragments.contacts.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.fragments.contacts.contracts.ContactsPresenter;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eder Xavier Rojas on 12/07/2016.
 */
public class AdapterPersonalContact extends RecyclerView.Adapter<AdapterPersonalContact.ViewHolder>  {
    FlowQueryList<PersonalContact> fqList = null;
    ArrayList<PersonalContact> sList = null;
    boolean usingFlowQuery = true;
    Activity activity;
    private static final int PICK_CONTACT = 1;
    ContactsPresenter presenter;


    public AdapterPersonalContact(FlowQueryList<PersonalContact> list, Activity activity, ContactsPresenter presenter) {
        this.activity = activity;
        this.fqList = list;
        this.presenter = presenter;
    }
    public AdapterPersonalContact(ArrayList<PersonalContact> list, Activity activity, ContactsPresenter presenter) {
        this.activity = activity;
        this.sList = list;
        this.presenter = presenter;
        this.usingFlowQuery = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.contactFlagImg)
        ImageView contactFlagImg;
        @Bind(R.id.contact_avatar)
        ImageView contactAvatar;
        @Bind(R.id.lbl_contact_name)
        TextViewHelper lblContactName;
        @Bind(R.id.lbl_contact_number)
        TextViewHelper lblContactNumber;
        @Bind(R.id.lbl_contact_country)
        TextViewHelper lblContactCountry;
        @Bind(R.id.lbl_contact_operator)
        TextViewHelper lblContactOperator;
        @Bind(R.id.vert_popup)
        ImageView vertPopup;
        @Bind(R.id.sms_bl_cardviewRow)
        CardView smsBlCardviewRow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PersonalContact contact = getPersonalContact(position);

        holder.contactAvatar.setImageDrawable(Utils.getAvatar(contact.getContact_name()));
        AreaCode areaCode = Utils.getOperadoraV3(contact.getContact_phone_number(), holder.itemView.getContext());
        if (areaCode != null) {
            holder.lblContactOperator.setText(areaCode.getArea_operator());
            if (areaCode.getArea_operator().equals("Claro"))
                holder.lblContactOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_600));
            else if (areaCode.getArea_operator().equals("Movistar"))
                holder.lblContactOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_600));
            else if (areaCode.getArea_operator().equals("CooTel"))
                holder.lblContactOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_amber_700));


            if (areaCode.getArea_name().length() > 0) {
                holder.lblContactCountry.setText(areaCode.getArea_name() + ", " + areaCode.getCountry_name());
                holder.contactFlagImg.setImageDrawable(holder.itemView.getResources().getDrawable(areaCode.getFlag()));
            } else if (contact.getContact_phone_number().replaceAll("\\s+", "").length() >= 8) {
                holder.lblContactCountry.setText(areaCode.getCountry_name());
                holder.contactFlagImg.setImageDrawable(holder.itemView.getResources().getDrawable(areaCode.getFlag()));
            } else {
                holder.lblContactCountry.setText("");
                holder.contactFlagImg.setImageDrawable(null);
            }
        } else {
            holder.lblContactCountry.setText("");
            holder.lblContactOperator.setText("");
        }
        holder.lblContactName.setText(contact.getContact_name());
        holder.lblContactNumber.setText(contact.getContact_phone_number());


        holder.vertPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showPopupMenu(holder.vertPopup, getPersonalContact(holder.getAdapterPosition()), holder.smsBlCardviewRow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(final View view, final PersonalContact contact, final View card) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contact_menu, popup.getMenu());
        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        card.setAlpha((float) 0.6);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_call_contact:
                        String uri = "tel:" + contact.getContact_phone_number().replaceAll("\\s+", "");
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return true;
                        }
                        activity.startActivity(intent);
                        card.setAlpha((float) 1);
                        return true;
                    case R.id.action_write_sms:
                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact.getContact_phone_number().replaceAll("\\s+", "")));
                        intent2.putExtra("sms_body", "");
                        view.getContext().startActivity(intent2);
                        card.setAlpha((float) 1);
                        return true;
                    case R.id.action_share_contact:
                        try
                        { Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "Contacto");
                            String sAux = contact.getContact_name()+" \n";
                            sAux = sAux + contact.getContact_phone_number()+"\n";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            view.getContext().startActivity(Intent.createChooser(i,
                                    view.getContext().getResources().getString(R.string.share_using)));
                        }
                        catch(Exception ignored) {}
                        return true;
                    case R.id.action_delete_contact:
                        String msg = view.getContext().getString(R.string.delete_contact);
                        new MaterialDialog.Builder(view.getContext())
                                .title( msg + " " + contact.getContact_name()+"?")
                                .content(R.string.delete_contact_warning)
                                .items(R.array.delete_contact_options)
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {return true;}
                                })
                                .positiveColor(Color.RED)
                                .positiveText(R.string.agree)
                                .negativeText(R.string.cancelar)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        boolean hardDelete = false;
                                        if(dialog.getSelectedIndex()>=0){hardDelete = true;}
                                        int i = indexOfContact(contact);
                                        if(presenter.deleteContact(contact,  hardDelete)){
                                            fqList.refresh();
                                            notifyItemRemoved(i);
                                        }
                                    }
                                })
                                .show();
                    default:
                }
                return false;
            }
        });

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                card.setAlpha((float) 1);
            }
        });

        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
            popup.show();
        } catch (Exception e) {
            popup.show();
        }

    }

    @Override
    public int getItemCount() {
        if (fqList != null) {
            return fqList.size();
        } else {
            return 0;
        }
    }

    public int refresh(PersonalContact contact){
        int i = 0;
        if(usingFlowQuery) {
            fqList.refresh();
            if (fqList.contains(contact)) {
                i = indexOfContact(contact);
            } else {
                i = -1;
            }
        }else {
            sList.add(0,contact);
            i = 0;
        }
        return i;
    }

    int indexOfContact(PersonalContact contact){
        int i = -1;
        if(usingFlowQuery) {
            for (PersonalContact c : fqList) {
                i++;
                if (c.getContact_phone_number().equals(contact.getContact_phone_number())) {
                    break;
                }
            }
        }else {
            i = sList.indexOf(contact);
        }
        return i;
    }

    private PersonalContact getPersonalContact(int position){
        if(usingFlowQuery){
            return fqList.get(position);
        }else {
            return sList.get(position);
        }
    }
}
