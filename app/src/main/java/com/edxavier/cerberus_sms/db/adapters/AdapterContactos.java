package com.edxavier.cerberus_sms.db.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.AreaCode;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.db.models.Sms_Lock;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolder> {

    private ArrayList<Contactos> contactos_list;
    private int rowView;
    private CardView selected;
    private CardView oldselected;
    private int adapterPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewHelper txtContactName;
        TextViewHelper txtPhone;
        TextViewHelper txtOperator;
        TextViewHelper txtCountry;
        ImageView avatar;
        CardView cardView;

        public ViewHolder(final View viewLayout) {
            super(viewLayout);
            txtContactName = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_bl_contact_name);
            txtPhone = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_bl_contact_number);
            txtOperator = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_bl_contact_operator);
            txtCountry = (TextViewHelper) viewLayout.findViewById(R.id.lbl_sms_bl_contact_country);
            cardView = (CardView) viewLayout.findViewById(R.id.sms_bl_cardviewRow);
            avatar = (ImageView) viewLayout.findViewById(R.id.contact_avatar);
            txtContactName.setRobotoMedium();
            txtCountry.setRobotoItalic();

        }


    }

    public AdapterContactos(ArrayList<Contactos> blContacts, int view) {
        this.contactos_list = blContacts;
        this.rowView = view;
    }

    @Override
    public AdapterContactos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowView, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterContactos.ViewHolder holder, int position) {

        Contactos contacto = contactos_list.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        String colorKey;
        if (contacto.getNombre().length() > 1)
            colorKey = contacto.getNombre().substring(0, 2);
        else
            colorKey = contacto.getNombre().substring(0, 1);

        int color = generator.getColor(colorKey);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(colorKey, color);
        holder.avatar.setImageDrawable(drawable);

        AreaCode areaCode = Utils.getOperadoraV3(contacto.getNumero(), holder.itemView.getContext());
        if (areaCode != null) {
            holder.txtOperator.setText(areaCode.getArea_operator());
            if (areaCode.getArea_operator().equals("Claro"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_500));
            else if (areaCode.getArea_operator().equals("Movistar"))
                holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_500));
            //else
            //  holder.txtOperator.setTextColor(holder.itemView.getResources().getColor(R.color.md_blue_grey_400));

            if (areaCode.getArea_name().length() > 0)
                holder.txtCountry.setText(areaCode.getArea_name() + ", " + areaCode.getCountry_name());
            else if (contacto.getNumero().replaceAll("\\s+", "").length() >= 8)
                holder.txtCountry.setText(areaCode.getCountry_name());
            else
                holder.txtCountry.setText("");
        } else {
            holder.txtCountry.setText("");
            holder.txtOperator.setText("");
        }


        holder.txtContactName.setText(contacto.getNombre());
        holder.txtPhone.setText(contacto.getNumero());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Contactos contacto = contactos_list.get(holder.getAdapterPosition());
                new MaterialDialog.Builder(v.getContext())
                        .title(contacto.getNombre())
                        .typeface("Roboto-Medium.ttf", "Roboto-Regular.ttf")
                        .items(R.array.opciones_contacto2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, final View view, int which, CharSequence text) {

                                switch (which) {
                                    case 0:
                                        String uri = "tel:" + contacto.getNumero().replaceAll("\\s+", "");
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse(uri));
                                        view.getContext().startActivity(intent);
                                        break;
                                    case 1:
                                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contacto.getNumero().replaceAll("\\s+", "")));
                                        intent2.putExtra("sms_body", "");
                                        view.getContext().startActivity(intent2);
                                        break;
                                    case 2:
                                        Sms_Lock lock = new Sms_Lock(contacto.getNombre(), contacto.getNumero());
                                        if (lock.save() >= 1) {
                                            Toast.makeText(  view.getContext(), contacto.getNombre() + " " +  view.getContext().getResources().getString(R.string.add_blacklist), Toast.LENGTH_LONG).show();
                                        }
                                        else
                                            Toast.makeText(  view.getContext(), contacto.getNombre() + " " +  view.getContext().getResources().getString(R.string.added_blacklist), Toast.LENGTH_LONG).show();
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
        if(contactos_list !=null) {
            return contactos_list.size();
        }else {
            return 0;
        }
    }



    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void addContacto(Contactos contacto2) {
        contactos_list.add(0,contacto2);
    }

}
