package com.edxavier.cerberus_sms.db.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.AreaCode;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 13/11/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contactos> {
    Context context;
    int resource, textViewResourceId;
    List<Contactos> items, tempItems, suggestions;
    TextViewHelper cNombre;
    TextViewHelper cNumero;
    TextViewHelper cOperador;

    public ContactAdapter(Context context, int resource, int textViewResourceId, List<Contactos> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Contactos>(items); // this makes the difference.
        suggestions = new ArrayList<Contactos>();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_contacts2, parent, false);
        }
        Contactos people = items.get(position);
        if (people != null) {
            cNombre = (TextViewHelper) view.findViewById(R.id.lbl_sms_bl_contact_name);
            cNumero = (TextViewHelper) view.findViewById(R.id.lbl_sms_bl_contact_number);
            cOperador = (TextViewHelper) view.findViewById(R.id.lbl_sms_bl_contact_operator);
            cNumero.setText(people.getNumero());
            AreaCode areaCode = Utils.getOperadoraV2(people.getNumero());
            if(areaCode!=null) {
                cOperador.setText(areaCode.getArea_operator());
                if (areaCode.getArea_operator().equals("Claro"))
                    cOperador.setTextColor(context.getResources().getColor(R.color.md_red_500));
                else if (areaCode.getArea_operator().equals("Movistar"))
                    cOperador.setTextColor(context.getResources().getColor(R.color.md_green_500));
            }

            if (cNombre != null)
                cNombre.setText(people.getNombre());
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
            String str = ((Contactos) resultValue).getNumero();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Contactos people : tempItems) {
                    if (people.getNombre().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            people.getNumero().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<Contactos> filterList = (ArrayList<Contactos>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Contactos people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
