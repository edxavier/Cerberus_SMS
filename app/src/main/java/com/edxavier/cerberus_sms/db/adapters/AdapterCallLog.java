package com.edxavier.cerberus_sms.db.adapters;

import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.AreaCode;
import com.edxavier.cerberus_sms.db.models.Call_Log;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AdapterCallLog extends RecyclerView.Adapter<AdapterCallLog.ViewHolder> {

    private ArrayList<Call_Log> call_logs;
    private int rowView;
    private CardView selected;
    private CardView oldselected;
    private int adapterPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextViewHelper txtContactName;
        TextViewHelper txtPhone;
        TextViewHelper txtOperator;
        TextViewHelper txtFecha;
        TextViewHelper txtDuration;
        TextViewHelper txtareaName;
        ImageView icon;
        CardView cardView;

        public ViewHolder(final View viewLayout) {
            super(viewLayout);
            txtContactName = (TextViewHelper) viewLayout.findViewById(R.id.lbl_caller_name);
            txtPhone = (TextViewHelper) viewLayout.findViewById(R.id.lbl_caller_number);
            txtOperator = (TextViewHelper) viewLayout.findViewById(R.id.lbl_call_operator);

            txtFecha  = (TextViewHelper) viewLayout.findViewById(R.id.lbl_call_datetime);
            txtDuration  = (TextViewHelper) viewLayout.findViewById(R.id.lbl_call_duration);
            txtareaName  = (TextViewHelper) viewLayout.findViewById(R.id.lbl_call_areaName);

            cardView = (CardView) viewLayout.findViewById(R.id.sms_bl_cardviewRow);
            icon = (ImageView) viewLayout.findViewById(R.id.call_type);
            txtPhone.setRobotoMedium();
            txtFecha.setRobotoItalic();
            txtareaName.setRobotoItalic();

        }

    }

    public AdapterCallLog(ArrayList<Call_Log> blContacts, int view) {
        this.call_logs = blContacts;
        this.rowView = view;
    }

    @Override
    public AdapterCallLog.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowView, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterCallLog.ViewHolder holder, int position) {

        Call_Log call_log = call_logs.get(position);
        SimpleDateFormat time_format = new SimpleDateFormat("dd-MMM-yy hh:mm a", Locale.ENGLISH);
        holder.txtFecha.setText(time_format.format(call_log.getFecha()));

        AreaCode areaCode = Utils.getOperadoraV3(call_log.getNumber(), holder.itemView.getContext());
        if(areaCode!=null) {
            holder.txtOperator.setText(areaCode.getArea_operator());
            holder.txtareaName.setText(areaCode.getCountry_name());
            if(areaCode.getArea_operator().equals("Claro"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_500));
            else if(areaCode.getArea_operator().equals("Movistar"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_500));

            if(areaCode.getArea_name().length()>0)
                holder.txtareaName.setText(areaCode.getArea_name()+", "+areaCode.getCountry_name());
            else if(call_log.getNumber().replaceAll("\\s+", "").length()>=8)
                holder.txtareaName.setText(areaCode.getCountry_name());
            else
                holder.txtareaName.setText("");
        }else {
            holder.txtOperator.setText("");
            holder.txtareaName.setText("");
        }

        holder.txtContactName.setText(call_log.getName());
        holder.txtPhone.setText(call_log.getNumber());
        holder.txtDuration.setText(Utils.getDurationString(call_log.getDuration()));
        if(call_log.getType()== CallLog.Calls.OUTGOING_TYPE)
            holder.icon.setImageResource(R.drawable.ic_up_right_filled_100);
        else if(call_log.getType()== CallLog.Calls.INCOMING_TYPE)
            holder.icon.setImageResource(R.drawable.ic_down_left_filled_100);
        else if(call_log.getType()== CallLog.Calls.MISSED_TYPE)
            holder.icon.setImageResource(R.drawable.ic_call_missed);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Call_Log call_log = call_logs.get(holder.getAdapterPosition());
                new MaterialDialog.Builder(v.getContext())
                        .title(call_log.getName())
                        .typeface("Roboto-Medium.ttf", "Roboto-Regular.ttf")
                        .items(R.array.opciones_contacto2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        String uri = "tel:" + call_log.getNumber().replaceAll("\\s+", "");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse(uri));
                                        view.getContext().startActivity(intent);
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + call_log.getNumber().replaceAll("\\s+", "")));
                                        intent2.putExtra("sms_body", "");
                                        view.getContext().startActivity(intent2);
                                        break;
                                    case 2:
                                        Sms_Lock lock = new Sms_Lock(call_log.getName(), call_log.getNumber());
                                        if (lock.save() >= 1) {
                                            Toast.makeText(view.getContext(), call_log.getName() + " " + view.getContext().getResources().getString(R.string.add_blacklist), Toast.LENGTH_LONG).show();
                                        }
                                        else
                                            Toast.makeText(  view.getContext(), call_log.getName() + " " +  view.getContext().getResources().getString(R.string.added_blacklist), Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });



    }

    @Override
    public int getItemCount() {
        if(call_logs !=null) {
            return call_logs.size();
        }else {
            return 0;
        }
    }



    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void addCall(Call_Log call_log) {
        call_logs.add(0, call_log);
    }

}
