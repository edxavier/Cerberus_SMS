package com.edxavier.cerberus_sms.fragments.messages.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.MessagesResume;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolder> {

    private List<MessagesResume> messages = null;

    public AdapterMessages(List<MessagesResume> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @Bind(R.id.sender_name)
        TextViewHelper senderName;
        @Bind(R.id.conversation_sms_count)
        TextViewHelper conversationSmsCount;
        @Bind(R.id.new_messages)
        TextViewHelper newMessages;
        @Bind(R.id.card_new_messages)
        CardView cardNewMessages;
        @Bind(R.id.lbl_sms_sender_number)
        TextViewHelper lblSmsSenderNumber;
        @Bind(R.id.msg_body)
        TextViewHelper msgBody;
        @Bind(R.id.lbl_contact_country)
        TextViewHelper lblContactCountry;
        @Bind(R.id.lbl_sms_date)
        TextViewHelper lblSmsDate;
        @Bind(R.id.lbl_sms_time)
        TextViewHelper lblSmsTime;
        @Bind(R.id.lbl_sms_operator)
        TextViewHelper lblSmsOperator;
        @Bind(R.id.img_send_failures)
        ImageView imgSendFailures;
        @Bind(R.id.sms_bl_cardviewRow)
        CardView smsBlCardviewRow;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MessagesResume resume = messages.get(position);
        holder.senderName.setText(resume.getSender_name());
        holder.conversationSmsCount.setText("("+String.valueOf(resume.getMessages_count())+")");
        holder.lblSmsSenderNumber.setText(resume.getSender_number());
        holder.msgBody.setText(resume.getMsg_body());
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        if (resume.getMsg_date() != null) {
            holder.lblSmsDate.setText(date_format.format(resume.getMsg_date()));
            holder.lblSmsTime.setText(time_format.format(resume.getMsg_date()));
        }
        AreaCode areaCode = Utils.getOperadoraV3(resume.getSender_number(), holder.itemView.getContext());
        if (areaCode != null) {
            holder.lblSmsOperator.setText(areaCode.getArea_operator());
            if (areaCode.getArea_operator().equals("Claro"))
                holder.lblSmsOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_600));
            else if (areaCode.getArea_operator().equals("Movistar"))
                holder.lblSmsOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_600));
            else if (areaCode.getArea_operator().equals("CooTel"))
                holder.lblSmsOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_amber_600));
            //else
            //holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_blue_grey_400));
        } else {
            holder.lblSmsOperator.setText("");
        }

        holder.smsBlCardviewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title(resume.getSender_name())
                        .typeface("Roboto-Medium.ttf", "Roboto-Regular.ttf")
                        .items(R.array.opciones_contacto)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, final View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        String uri = "tel:" + resume.getSender_number().replaceAll("\\s+", "");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse(uri));
                                        try {
                                            view.getContext().startActivity(intent);
                                        }catch (Exception ignored){}
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + resume.getSender_number().replaceAll("\\s+", "")));
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

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    public void removeAll(){
        messages.clear();
        notifyDataSetChanged();
    }

}
