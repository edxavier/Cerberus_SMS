package com.edxavier.cerberus_sms.db.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.edxavier.cerberus_sms.DetailConversation;
import com.edxavier.cerberus_sms.MainActivity;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.db.models.InboxSms;
import com.edxavier.cerberus_sms.helpers.Constans;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AdapterInbox extends RecyclerView.Adapter<AdapterInbox.ViewHolder> {

    private ArrayList<InboxSms> inboxSms_list;
    private int rowView;
    private Activity activity;
    private RelativeLayout selected;
    private RelativeLayout oldselected;
    private int adapterPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextViewHelper txtFecha;
        TextViewHelper txtBody;
        ImageView avatar;
        ImageView fail_send;
        CardView cardView;

        public ViewHolder(final View viewLayout) {
            super(viewLayout);

            txtBody  = (TextViewHelper) viewLayout.findViewById(R.id.txt_sms_body);
            txtFecha  = (TextViewHelper) viewLayout.findViewById(R.id.txt_sms_time);
            //txtTime  = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_time);
            avatar = (ImageView) viewLayout.findViewById(R.id.img_conversation_avatar_left);
            fail_send = (ImageView) viewLayout.findViewById(R.id.img_conversation_send_failures);

            //cardView = (CardView) viewLayout.findViewById(R.id.sms_bl_cardviewRow);

            //txtPhone.setRobotoMedium();
            //txtBody.setRobotoLight();
            txtFecha.setRobotoItalic();

        }

    }

    public AdapterInbox(ArrayList<InboxSms> blContacts, int view, Activity activity) {
        this.inboxSms_list = blContacts;
        this.rowView = view;
        this.activity = activity;
    }
    public AdapterInbox(ArrayList<InboxSms> blContacts, int view) {
        this.inboxSms_list = blContacts;
        this.rowView = view;
    }

    @Override
    public AdapterInbox.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowView, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterInbox.ViewHolder holder, int position) {

        final InboxSms sms = inboxSms_list.get(position);

        holder.txtBody.setText(sms.getMsg_body());
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        if(sms.getFecha()!=null) {
            if(date_format.format(new Date()).equals(date_format.format(sms.getFecha())))
                holder.txtFecha.setText(time_format.format(sms.getFecha()));
            else
                holder.txtFecha.setText(date_format.format(sms.getFecha()));
        }

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        String colorKey;

        if(sms.getMsg_type()==Constans.MESSAGE_TYPE_FAILED)
            holder.fail_send.setVisibility(View.VISIBLE);
        else
            holder.fail_send.setVisibility(View.GONE);

        if(sms.getMsg_type()==Constans.MESSAGE_TYPE_FAILED||sms.getMsg_type()==Constans.MESSAGE_TYPE_SENT) {
            colorKey = "Yo";
            int color = generator.getColor(colorKey);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(colorKey, color);
            holder.avatar.setImageDrawable(drawable);
        }else{
            if(sms.getSender_name().length()>1)
                colorKey = sms.getSender_name().substring(0,2);
            else
                colorKey = sms.getSender_name().substring(0,1);
            int color = generator.getColor(colorKey);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(colorKey, color);
            holder.avatar.setImageDrawable(drawable);
        }

    }

    @Override
    public int getItemCount() {
        if(inboxSms_list !=null) {
            return inboxSms_list.size();
        }else {
            return 0;
        }
    }



    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void addItem( InboxSms sms) {
        inboxSms_list.add(0, sms);
    }

    public boolean itemExist( InboxSms sms) {
        boolean exist = false;
        for(InboxSms tmp: inboxSms_list){
            if(tmp.getSender_number()==sms.getSender_number() && tmp.getMsg_type()==sms.getMsg_type() && tmp.getMsg_body() == sms.getMsg_body()) {
                exist = true;
                break;
            }
        }
        return exist;
    }

}
