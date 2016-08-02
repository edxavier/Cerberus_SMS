package com.edxavier.cerberus_sms.helpers;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.AreaCode_Table;
import com.edxavier.cerberus_sms.db.entities.PersonalContact;
import com.edxavier.cerberus_sms.db.entities.PersonalContact_Table;
import com.edxavier.cerberus_sms.db.models.Call_Log;
import com.edxavier.cerberus_sms.db.models.Contactos;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 13/10/2015.
 */
public class Utils {


    public static AreaCode getOperadoraV3(String numero, Context cntx){
        String codigoPais="";
        String codigoArea="";
        if (numero != null) {
            numero = numero.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll("\\s+", "");
            if (numero.startsWith("+1")) {
                codigoPais = "+1";
                if (numero.length() >= 5)
                    codigoArea = numero.substring(2, 5);
            }
            else if (numero.startsWith("+7")) {
                codigoPais = "+7";
            }
            else if (numero.startsWith("+50") && numero.length() >= 4) {
                codigoPais = numero.substring(0, 4);
                if(numero.length() >= 7 && numero.startsWith("+505"))
                    codigoArea = numero.substring(4, 7);
            }else if (numero.length() >= 3  && numero.startsWith("+3") || numero.startsWith("+4") || numero.startsWith("+8")) {
                try{
                    codigoPais = numero.substring(0, 3);
                }catch (Exception ignored){}
            }else if (numero.length() >= 3  && numero.startsWith("+5")) {
                if(!numero.startsWith("+50"))
                    codigoPais = numero.substring(0, 3);
            }else if ( numero.length() >= 4 && numero.length()<=8) {
                TelephonyManager tm = (TelephonyManager) cntx.getSystemService(Context.TELEPHONY_SERVICE);
                String countryCodeValue = tm.getSimCountryIso();
                Log.d("EDER_CV",countryCodeValue);
                if (countryCodeValue.equals("ni")) {
                    codigoPais = "+505";
                    codigoArea = numero.substring(0, 3);
                }else if(countryCodeValue.equals("us")){
                    codigoPais = "+1";
                    codigoArea = "";
                }
            }

        }
        int num = 0;

        try {
            AreaCode areaCode;
            if(codigoArea.length()>0) {
                areaCode = SQLite.select().from(AreaCode.class).where(AreaCode_Table.country_code.eq(codigoPais))
                        .and(AreaCode_Table.area_code.eq(codigoArea)).querySingle();
            }else{
                areaCode = SQLite.select().from(AreaCode.class).where(AreaCode_Table.country_code.eq(codigoPais))
                        .querySingle();
                areaCode.setArea_name("");
            }
            return areaCode;

        } catch (Exception e) {
            return null;
        }

    }

    public static AreaCode getOperadoraV2(String numero){
        String codigoPais="";
        String codigoArea="";
        if (numero != null) {
            numero = numero.replaceAll("-", "").replaceAll("\\s+", "");
            if (numero.length() == 12 && numero.startsWith("+1")) {
                codigoPais = "+1";
                codigoArea = numero.substring(2, 5);
            }else if (numero.length() == 12 && !numero.startsWith("+505")) {
                codigoPais = numero.substring(0, 4);
                codigoArea = "";
            }else if (numero.length() == 12 && numero.startsWith("+505")) {
                codigoPais = numero.substring(0, 4);
                codigoArea = numero.substring(4, 7);
            }else if (numero.length() == 8 && !numero.startsWith("+505")) {
                codigoPais = "+505";
                codigoArea = numero.substring(0, 3);
            }else if(numero.length()>=4 && numero.length()<=7) {
                codigoPais = "+505";
                codigoArea = numero.substring(0, 3);
            }

        }
        int num = 0;

        try {
            AreaCode areaCode;
            if(codigoArea.length()>0) {
                //areaCode = new Select().from(AreaCode.class).where("country_code = ? ", codigoPais)
                  //      .and("area_code = ?", codigoArea).executeSingle();
            }else{
                //areaCode = new Select().from(AreaCode.class).where("country_code = ? ", codigoPais)
                  //      .executeSingle();
            }
            return null;//areaCode;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EDER_ERROR", e.getMessage());
            return null;
        }

            /*int operador = 0;
            if ((num >= 570 && num <= 589) || num >= 820 && num <= 823) {
                operador_str = "Claro";
            } else if ((num >= 830 && num <= 831) || num == 833 || num == 835 || num == 836 || (num >= 840 && num <= 844) || num == 849) {
                operador_str = "Claro";
            } else if ((num >= 850 && num <= 854) || (num >= 860 && num <= 866) || num == 869) {
                operador_str = "Claro";
            } else if ((num >= 870 && num <= 874) || (num >= 882 && num <= 885) || (num >= 890 && num <= 894)) {
                operador_str = "Claro";

            } else if (num >= 750 && num <= 755 || num >= 760 && num <= 789 || num >= 810 && num <= 819 || num >= 824 && num <= 829) {
                operador_str = "Movistar";
            } else if (num == 832 || num >= 837 && num <= 839 || num >= 845 && num <= 848 || num >= 855 && num <= 859) {
                operador_str = "Movistar";
            } else if ((num >= 867 && num <= 868) || num >= 875 && num <= 881 || num >= 886 && num <= 889) {
                operador_str = "Movistar";
            } else if ((num >= 895 && num <= 899)) {
                operador_str = "Movistar";
            }
            */
    }

    public static String getPais(String numero){
        if(numero!=null) {
            numero = numero.replace('-', ' ').replace("+","").replaceAll("\\s+", "");
            if(numero.length()==11)
                numero = numero.substring(0,3);
        }
        int num=0;
        try{
            num = Integer.parseInt(numero);
        }catch (Exception e){
            e.printStackTrace();
            //Log.d("EDER_EXEPTION_UTILS", e.getMessage());
        }
        String pais="";
        if(num == Constans.NICARAGUA)
            pais = "Nicaragua";
        else if(num == Constans.COSTARICA)
            pais = "Costa Rica";
        return pais;
    }

    public static String formatPhoneNumber(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll("\\s+","");
        if(phoneNumber.length()==12)
            phoneNumber = String.format("%s %s %s", phoneNumber.substring(0, 4), phoneNumber.substring(4, 8),
                    phoneNumber.substring(8, 12));
        else if(phoneNumber.length()==8)
            phoneNumber = String.format("%s %s", phoneNumber.substring(0, 4), phoneNumber.substring(4, 8));
        return phoneNumber;
    }

    public static void loadContacts(Context ctx){
        try {
            ContentResolver cr = ctx.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            while (cursor.moveToNext()){
                String haveNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                if(Integer.parseInt(haveNumber)>0){
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumber = formatPhoneNumber(phoneNumber);
                    (new Contactos(name, phoneNumber)).save();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void getCallLogs(Context ctx){
        try{
            ContentResolver cr = ctx.getContentResolver();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 300";
            Uri callUri = Uri.parse("content://call_log/calls");
            Cursor cur = cr.query(callUri, null, null, null, strOrder);
            // loop through cursor
            while (cur.moveToNext()) {

                String callNumber = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                long callDate = cur.getLong(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DATE));
                Date fecha = new Date(callDate);
                int callType = cur.getInt(cur
                        .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                int duration = cur.getInt(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DURATION));


                callNumber = formatPhoneNumber(callNumber);
                String originalNum = callNumber;
                Contactos c = new Select().from(Contactos.class).where("numero = ? ", callNumber).executeSingle();
                String name="";
                Call_Log call_log = new Call_Log(name, callNumber, callType, fecha, duration);

                if(c!=null) {
                    name = c.getNombre();
                    call_log.setName(name);
                }else {
                    if (callNumber.length() == 14) {
                        callNumber = callNumber.substring(5, 14);
                    }else if (callNumber.length() == 9 && !callNumber.startsWith("+")) {
                        if(Utils.isInteger(callNumber)) {
                            callNumber = "+505 " + callNumber;
                        }
                    }
                    c = new Select().from(Contactos.class).where("numero = ? ", callNumber).executeSingle();
                    if (c != null) {
                        call_log.setName(c.getNombre());
                    } else {
                        call_log.setName(originalNum);
                    }
                }
                call_log.save();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    public static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public static boolean isInteger(String str) {
        int num;
        try{
            num = Integer.parseInt(str.substring(0,3));
            return true;
        }catch (Exception e){
            return false;
        }
        //return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
    }


    public static String getContactName(String number){
        String nombre="";
        String originalNumber = number;
        PersonalContact contact = SQLite.select().from(PersonalContact.class)
                .where(PersonalContact_Table.contact_phone_number.eq(number))
                .querySingle();
        if(contact!=null) {
            nombre = contact.getContact_name();
        }else {
            if (number.length() == 14)
                number = number.substring(5, 14);
            else if (number.length() == 9 && !number.startsWith("+")) {
                if(Utils.isInteger(number))
                    number = "+505 " + number;
            }
            contact = SQLite.select().from(PersonalContact.class)
                    .where(PersonalContact_Table.contact_phone_number.eq(number))
                    .querySingle();
            if (contact != null) {
                nombre = contact.getContact_name();
            } else
               nombre = originalNumber;
        }
        return nombre;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static TextDrawable getAvatar(String contact_name){
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        if (contact_name.length() > 1)
            contact_name = contact_name.substring(0, 2);
        else
            contact_name = contact_name.substring(0, 1);

        int color = generator.getColor(contact_name);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(contact_name, color);

        return drawable;
    }

}

