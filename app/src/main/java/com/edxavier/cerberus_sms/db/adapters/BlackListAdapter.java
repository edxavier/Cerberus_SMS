package com.edxavier.cerberus_sms.db.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.activeandroid.query.Delete;
//import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class BlackListAdapter  extends RecyclerView.Adapter<BlackListAdapter.ViewHolder> {

    private ArrayList<Sms_Lock> sms_lockArrayList;
    private int rowView;
    private CardView selected;
    private CardView oldselected;
    private int adapterPosition = -1;
    private AdapterInterfaceListener adapListener;

    public interface AdapterInterfaceListener {
        void OnAdapterItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextViewHelper txtContactName;
        TextViewHelper txtPhone;
        TextViewHelper txtOperator;
        TextViewHelper txtNumBlockSms;
        CardView cardView;

        public ViewHolder(final View viewLayout) {
            super(viewLayout);
            txtContactName = (TextViewHelper) viewLayout.findViewById(R.id.lbl_contact_name);
            txtPhone = (TextViewHelper) viewLayout.findViewById(R.id.lbl_contact_number);
            txtOperator = (TextViewHelper) viewLayout.findViewById(R.id.lbl_contact_operator);
            txtNumBlockSms = (TextViewHelper) viewLayout.findViewById(R.id.lbl_num_locked);
            cardView = (CardView) viewLayout.findViewById(R.id.sms_bl_cardviewRow);
            txtContactName.setRobotoMedium();

        }

    }

    public BlackListAdapter(ArrayList<Sms_Lock> blContacts, int view, AdapterInterfaceListener adapListener) {
        this.sms_lockArrayList = blContacts;
        this.rowView = view;
        this.adapListener = adapListener;
    }

    @Override
    public BlackListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowView, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BlackListAdapter.ViewHolder holder, int position) {

        Sms_Lock contacto = sms_lockArrayList.get(position);
        AreaCode areaCode = Utils.getOperadoraV2(contacto.getNumero());
        if(areaCode!=null) {
            holder.txtOperator.setText(areaCode.getArea_operator());
            if(areaCode.getArea_operator().equals("Claro"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_500));
            else if(areaCode.getArea_operator().equals("Movistar"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_500));
  //          else
//                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_blue_grey_400));
        }else {
            holder.txtOperator.setText("");
        }

        holder.txtNumBlockSms.setText(String.valueOf(contacto.getConteo()));
        holder.txtContactName.setText(contacto.getNombre());
        holder.txtPhone.setText(contacto.getNumero());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPosition = holder.getAdapterPosition();
                Sms_Lock contactoLocked = sms_lockArrayList.get(adapterPosition);
               /* new MaterialDialog.Builder(v.getContext())
                        .title(contactoLocked.getNombre())
                        .typeface("Roboto-Medium.ttf","Roboto-Regular.ttf")
                        .items(R.array.opciones_blocksms)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        adapListener.OnAdapterItemClick(adapterPosition);
                                        break;
                                }
                            }
                        })
                        .show();*/
            }
        });



    }

    @Override
    public int getItemCount() {
        if(sms_lockArrayList !=null) {
            return sms_lockArrayList.size();
        }else {
            return 0;
        }
    }

    public void addItem(Sms_Lock entrada){
        sms_lockArrayList.add(0, entrada);
        notifyItemInserted(0);
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public String removeItem(int position){
        Sms_Lock it = sms_lockArrayList.get(position);
        sms_lockArrayList.remove(position);
        it.delete();
        this.notifyItemRemoved(position);
        adapterPosition = -1;
        it.delete();
        return it.getNombre();
    }
    public Sms_Lock getItem(int position){
        return sms_lockArrayList.get(position);
    }
}
