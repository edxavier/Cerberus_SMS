package com.edxavier.cerberus_sms.db.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 13/11/2015.
 */
public class ContactAdapter extends ArrayAdapter<PersonalContact> {
    Context context;
    int resource, textViewResourceId;
    List<PersonalContact> items, tempItems, suggestions;
    TextViewHelper cNombre;
    TextViewHelper cNumero;
    TextViewHelper cOperador;

    public ContactAdapter(Context context, int resource, int textViewResourceId, List<PersonalContact> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<PersonalContact>(items); // this makes the difference.
        suggestions = new ArrayList<PersonalContact>();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_contacts2, parent, false);
        }
        PersonalContact people = items.get(position);
        if (people != null) {
            cNombre = (TextViewHelper) view.findViewById(R.id.lbl_contact_name_auto_complete);
            cNumero = (TextViewHelper) view.findViewById(R.id.lbl_contact_number_auto_complete);
            cOperador = (TextViewHelper) view.findViewById(R.id.lbl_contact_operator_auto_complete);
            cNumero.setText(people.getContact_phone_number());
            AreaCode areaCode = Utils.getOperadoraV3(people.getContact_phone_number(), context);
            if(areaCode!=null) {
                cOperador.setText(areaCode.getArea_operator());
                if (areaCode.getArea_operator().equals("Claro"))
                    cOperador.setTextColor(context.getResources().getColor(R.color.md_red_600));
                else if (areaCode.getArea_operator().equals("Movistar"))
                    cOperador.setTextColor(context.getResources().getColor(R.color.md_green_600));
                else if (areaCode.getArea_operator().equals("CooTel"))
                    cOperador.setTextColor(context.getResources().getColor(R.color.md_amber_700));
            }

            if (cNombre != null)
                cNombre.setText(people.getContact_name());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((PersonalContact) resultValue).getContact_phone_number();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (PersonalContact people : tempItems) {
                    if (people.getContact_name().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            people.getContact_phone_number().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<PersonalContact> filterList = (ArrayList<PersonalContact>) results.values;
            if (results.count > 0) {
                clear();
                for (PersonalContact people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

}

