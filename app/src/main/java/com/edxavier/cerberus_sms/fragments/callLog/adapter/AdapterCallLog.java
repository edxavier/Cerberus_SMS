package com.edxavier.cerberus_sms.fragments.callLog.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.CallLogResume;
import com.edxavier.cerberus_sms.fragments.callLog.contracts.CallLogPresenter;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eder Xavier Rojas on 12/07/2016.
 */
public class AdapterCallLog extends RecyclerView.Adapter<AdapterCallLog.ViewHolder> {
    List<CallLogResume> calls = null;
    boolean usingFlowQuery = true;
    Activity activity;
    private static final int PICK_CONTACT = 1;
    CallLogPresenter presenter;


    public AdapterCallLog(List<CallLogResume> list, Activity activity, CallLogPresenter presenter) {
        this.activity = activity;
        this.calls = list;
        this.presenter = presenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lbl_caller_name)
        TextViewHelper lblCallerName;
        @Bind(R.id.lbl_caller_number)
        TextViewHelper lblCallerNumber;
        @Bind(R.id.lbl_call_datetime)
        TextViewHelper lblCallDatetime;
        @Bind(R.id.lbl_call_areaName)
        TextViewHelper lblCallAreaName;
        @Bind(R.id.lbl_call_operator)
        TextViewHelper lblCallOperator;
        @Bind(R.id.lbl_call_duration)
        TextViewHelper lblCallDuration;
        @Bind(R.id.call_type)
        ImageView callType;
        @Bind(R.id.sms_bl_cardviewRow)
        CardView smsBlCardviewRow;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            lblCallerNumber.setRobotoMedium();
            lblCallDatetime.setRobotoItalic();
            lblCallAreaName.setRobotoItalic();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_call_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CallLogResume call = calls.get(position);
        SimpleDateFormat time_format = new SimpleDateFormat("dd-MMM-yy hh:mm a", Locale.ENGLISH);
        holder.lblCallDatetime.setText(time_format.format(call.getCallDate()));
        holder.lblCallerName.setText(call.getName() + " (" + String.valueOf(call.getNumber_calls()) + ")");
        holder.lblCallerNumber.setText(call.getNumber());
        holder.lblCallDuration.setText(Utils.getDurationString(call.getCallDuration()));
        if (call.getCallDirection() == CallLog.Calls.OUTGOING_TYPE)
            holder.callType.setImageResource(R.drawable.ic_action_communication_call_made);
        else if (call.getCallDirection() == CallLog.Calls.INCOMING_TYPE)
            holder.callType.setImageResource(R.drawable.ic_action_communication_call_received);
        else if (call.getCallDirection() == CallLog.Calls.MISSED_TYPE)
            holder.callType.setImageResource(R.drawable.ic_action_communication_call_missed);
        AreaCode areaCode = Utils.getOperadoraV3(call.getNumber(), holder.itemView.getContext());
        if (areaCode != null) {
            holder.lblCallOperator.setText(areaCode.getArea_operator());
            holder.lblCallAreaName.setText(areaCode.getCountry_name());
            if (areaCode.getArea_operator().equals("Claro"))
                holder.lblCallOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_600));
            else if (areaCode.getArea_operator().equals("Movistar"))
                holder.lblCallOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_600));
            else if (areaCode.getArea_operator().equals("CooTel"))
                holder.lblCallOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_amber_700));

            if (areaCode.getArea_name().length() > 0)
                holder.lblCallAreaName.setText(areaCode.getArea_name() + ", " + areaCode.getCountry_name());
            else if (call.getNumber().replaceAll("\\s+", "").length() >= 8)
                holder.lblCallAreaName.setText(areaCode.getCountry_name());
            else
                holder.lblCallAreaName.setText("");
        } else {
            holder.lblCallOperator.setText("");
            holder.lblCallAreaName.setText("");
        }

        holder.smsBlCardviewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title(call.getName())
                        .typeface("Roboto-Medium.ttf", "Roboto-Regular.ttf")
                        .items(R.array.opciones_contacto)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, final View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        String uri = "tel:" + call.getNumber().replaceAll("\\s+", "");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse(uri));
                                        try {
                                            view.getContext().startActivity(intent);
                                        }catch (Exception ignored){}
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + call.getNumber().replaceAll("\\s+", "")));
                                        intent2.putExtra("sms_body", "");
                                        view.getContext().startActivity(intent2);
                                        break;

                                }
                            }
                        })
                        .show();
            }
        });


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */

    @Override
    public int getItemCount() {
        if (calls != null) {
            return calls.size();
        } else {
            return 0;
        }
    }

    public void removeAll(){
        calls.clear();
        notifyDataSetChanged();
    }
}
