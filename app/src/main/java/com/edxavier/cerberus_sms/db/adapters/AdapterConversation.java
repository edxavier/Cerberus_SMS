package com.edxavier.cerberus_sms.db.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.AreaCode;
import com.edxavier.cerberus_sms.db.models.Conversation;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.Constans;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AdapterConversation extends RecyclerView.Adapter<AdapterConversation.ViewHolder> {

    private ArrayList<Conversation> inboxSms_list;
    private int rowView;
    private Activity activity;
    private RelativeLayout selected;
    private RelativeLayout oldselected;
    private int adapterPosition = -1;
    private ConversationClickListener conversationClickListener;

    public interface ConversationClickListener {
        void OnConversationClick(Conversation conversation, int index);
        void OnConversationDelete( int index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewHelper txtContactName;
        TextViewHelper txtPhone;
        TextViewHelper txtOperator;
        TextViewHelper txtFecha;
        TextViewHelper txtTime;
        TextViewHelper txtBody;
        TextViewHelper txtNewMsgs;
        TextViewHelper txtMsgsCount;
        ImageView warning;
        CardView cardView;
        RelativeLayout relativeLayout;
        CardView cardView_newMsg;

        public ViewHolder(final View viewLayout) {
            super(viewLayout);
            txtContactName = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_sender_name);
            txtPhone = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_sender_number);
            txtOperator = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_operator);
            txtBody = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_body);
            txtFecha = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_date);
            txtTime = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_time);
            txtNewMsgs = (TextViewHelper) viewLayout.findViewById(R.id.new_messages);
            txtMsgsCount = (TextViewHelper) viewLayout.findViewById(R.id.lbl_conversation_num_sms);
            warning = (ImageView) viewLayout.findViewById(R.id.img_send_failures);

            cardView = (CardView) viewLayout.findViewById(R.id.sms_bl_cardviewRow);
            cardView_newMsg = (CardView) viewLayout.findViewById(R.id.card_new_messages);
            relativeLayout = (RelativeLayout) viewLayout.findViewById(R.id.relative_inbox);
            //txtPhone.setRobotoMedium();
            //txtBody.setRobotoLight();
            txtTime.setRobotoLight();

        }

    }

    public AdapterConversation(ArrayList<Conversation> blContacts, int view,
                               Activity activity, ConversationClickListener conversationClickListener) {
        this.inboxSms_list = blContacts;
        this.rowView = view;
        this.activity = activity;
        this.conversationClickListener = conversationClickListener;
    }

    @Override
    public AdapterConversation.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowView, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterConversation.ViewHolder holder, int position) {

        final Conversation sms = inboxSms_list.get(position);
        AreaCode areaCode = Utils.getOperadoraV3(sms.getNumber(), holder.itemView.getContext());
        if (areaCode != null) {
            holder.txtOperator.setText(areaCode.getArea_operator());
            if (areaCode.getArea_operator().equals("Claro"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_500));
            else if (areaCode.getArea_operator().equals("Movistar"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_500));
            //else
            //holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_blue_grey_400));
        } else {
            holder.txtOperator.setText("");
        }

        holder.txtContactName.setText(sms.getName());
        holder.txtMsgsCount.setRobotoLight();
        holder.txtMsgsCount.setText("(" + String.valueOf(sms.getMsg_count()) + ")");
        holder.txtPhone.setText(sms.getNumber());
        if (sms.getSnippet().length() > 30) {
            holder.txtBody.setText(sms.getSnippet().substring(0, 30) + "...");
        } else
            holder.txtBody.setText(sms.getSnippet());

        if (sms.getNew_msg_count() > 0) {
            holder.txtContactName.setRobotoMedium();
            holder.txtContactName.setTextColor(holder.itemView.getResources().getColor(R.color.md_teal_700));
            holder.txtPhone.setTextColor(holder.itemView.getResources().getColor(R.color.md_teal_400));
            holder.txtBody.setTextColor(holder.itemView.getResources().getColor(R.color.md_teal_300));
            holder.txtNewMsgs.setText(String.valueOf(sms.getNew_msg_count()));
            holder.cardView_newMsg.setVisibility(View.VISIBLE);
        } else {
            holder.txtContactName.setRobotoRegular();
            holder.txtContactName.setTextColor(holder.itemView.getResources().getColor(R.color.md_black_1000_75));
            holder.txtPhone.setTextColor(holder.itemView.getResources().getColor(R.color.md_black_1000_50));
            holder.txtBody.setTextColor(holder.itemView.getResources().getColor(R.color.md_black_1000_50));
            holder.cardView_newMsg.setVisibility(View.GONE);
            holder.txtNewMsgs.setText(String.valueOf(sms.getNew_msg_count()));
        }

        if (sms.getSend_failure() == Constans.MESSAGE_TYPE_FAILED) {
            holder.warning.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_error_64));
        } else {
            holder.warning.setImageDrawable(null);
        }

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        if (sms.getDate() != null) {
            holder.txtFecha.setText(date_format.format(sms.getDate()));
            holder.txtTime.setText(time_format.format(sms.getDate()));
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the selected positions to the SparseBooleanArray
                Conversation sms = inboxSms_list.get(holder.getAdapterPosition());
                conversationClickListener.OnConversationClick(sms, holder.getAdapterPosition());
            }
        });

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Conversation sms = inboxSms_list.get(holder.getAdapterPosition());
                new MaterialDialog.Builder(v.getContext())
                        .title(sms.getName())
                        .typeface("Roboto-Medium.ttf", "Roboto-Regular.ttf")
                        .items(R.array.opciones_contacto)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, final View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        String uri = "tel:" + sms.getNumber().replaceAll("\\s+", "");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse(uri));
                                        try {
                                            view.getContext().startActivity(intent);
                                        }catch (Exception ignored){}
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + sms.getNumber().replaceAll("\\s+", "")));
                                        intent2.putExtra("sms_body", "");
                                        view.getContext().startActivity(intent2);
                                        break;
                                    case 2:
                                        Sms_Lock lock = new Sms_Lock(sms.getName(), sms.getNumber());
                                        if (lock.save() >= 1) {
                                            Toast.makeText(view.getContext(), sms.getName() + " " + view.getContext().getResources().getString(R.string.add_blacklist), Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(view.getContext(), sms.getName() + " " + view.getContext().getResources().getString(R.string.added_blacklist), Toast.LENGTH_LONG).show();
                                        break;
                                    case 3:
                                        MaterialDialog dlg = new MaterialDialog.Builder(view.getContext())
                                                .title(sms.getName())
                                                .content(view.getContext().getResources().getString(R.string.elimianr_msg))
                                                .positiveText(view.getContext().getResources().getString(R.string.eliminar))
                                                .negativeText(view.getContext().getResources().getString(R.string.cancelar))
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                                        int res = view.getContext().getContentResolver()
                                                                .delete(Uri.parse("content://sms/"),"address='"+sms.getNumber().replaceAll("\\s+", "")+"'", null);
                                                        if(res>0) {
                                                            inboxSms_list.remove(holder.getAdapterPosition());
                                                            conversationClickListener.OnConversationDelete(holder.getAdapterPosition());
                                                        }else
                                                            Toast.makeText(view.getContext(),view.getContext().getResources().getString(R.string.eliminar_msg2),Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .show();
                                }
                            }
                        })
                        .show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        if(inboxSms_list !=null) {
            return inboxSms_list.size();
        }else {
            return 0;
        }
    }

    public void addConversation(Conversation conversation) {
       inboxSms_list.add(0, conversation);
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void updateAdapterConversation(int index, Conversation conversation) {
        inboxSms_list.set(index, conversation);
    }



}
