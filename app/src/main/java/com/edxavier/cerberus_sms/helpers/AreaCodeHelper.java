package com.edxavier.cerberus_sms.helpers;

import android.content.Context;

import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.OperatorDB;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 07/11/2015.
 */
public class AreaCodeHelper {
    public static void initAreaCodes(Context cntx){


        ArrayList<AreaCode> areaCodes = new ArrayList<AreaCode>();
        //codigoPais, CodigoArea, Pais, Area, Operador
        areaCodes.add(new AreaCode("+1","209",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","213",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","310",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","323",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","408",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));

        areaCodes.add(new AreaCode("+1","415",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","510",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","530",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","559",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","562",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));

        areaCodes.add(new AreaCode("+1","619",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","626",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","650",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","661",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","707",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));

        areaCodes.add(new AreaCode("+1","714",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","760",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","805",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","818",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","831",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));

        areaCodes.add(new AreaCode("+1","858",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","909",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","916",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","925",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","949",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),"", R.drawable.usa));


        areaCodes.add(new AreaCode("+1","305",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","321",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","352",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","386",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","407",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","561",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","727",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","754",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));

        areaCodes.add(new AreaCode("+1","772",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","786",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","813",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","850",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","863",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","904",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","941",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));
        areaCodes.add(new AreaCode("+1","954",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),"", R.drawable.usa));


        areaCodes.add(new AreaCode("+501","","Belice","","", R.drawable.belize));
        areaCodes.add(new AreaCode("+502","","Guatemala","","", R.drawable.guatemala));
        areaCodes.add(new AreaCode("+503","","El Salvador","","", R.drawable.el_salvador));
        areaCodes.add(new AreaCode("+504","","Honduras","","", R.drawable.honduras));
        areaCodes.add(new AreaCode("+506","","Costa Rica","","", R.drawable.costa_rica));
        areaCodes.add(new AreaCode("+507","","Panama","","", R.drawable.panama));
        areaCodes.add(new AreaCode("+509","","Haití","","", R.drawable.haiti));

        areaCodes.add(new AreaCode("+51",""," Perú","","", R.drawable.peru));
        areaCodes.add(new AreaCode("+52","","Mexico","","", R.drawable.mexico));
        areaCodes.add(new AreaCode("+53","","Cuba","","", R.drawable.cuba));
        areaCodes.add(new AreaCode("+54",""," Argentina","","", R.drawable.argentina));
        areaCodes.add(new AreaCode("+55","","Brasil","","", R.drawable.brazil));
        areaCodes.add(new AreaCode("+56","","Chile","","", R.drawable.chile));
        areaCodes.add(new AreaCode("+57","","Colombia","","", R.drawable.colombia));
        areaCodes.add(new AreaCode("+58","","Venezuela","","", R.drawable.venezuela));


        areaCodes.add(new AreaCode("+32","","Belgica","","", R.drawable.belgium));
        areaCodes.add(new AreaCode("+33","","Francia","","", R.drawable.france));
        areaCodes.add(new AreaCode("+34","","España","","", R.drawable.spain));
        areaCodes.add(new AreaCode("+39","","Italia","","", R.drawable.italy));

        areaCodes.add(new AreaCode("+40","","Rumania","","", R.drawable.romania));
        areaCodes.add(new AreaCode("+41","","Suiza","","", R.drawable.switzerland));
        areaCodes.add(new AreaCode("+43","","Austria","","", R.drawable.austria));
        areaCodes.add(new AreaCode("+44","","Inglaterra","","", R.drawable.england));
        areaCodes.add(new AreaCode("+45","","Dinamarca","","", R.drawable.denmark));
        areaCodes.add(new AreaCode("+49","","Alemania","","", R.drawable.germany));

        areaCodes.add(new AreaCode("+7","","Rusia","","", R.drawable.russia));
        areaCodes.add(new AreaCode("+80","","Japón","","", R.drawable.japan));
        areaCodes.add(new AreaCode("+81","","Corea del Sur","","", R.drawable.south_krea));
        areaCodes.add(new AreaCode("+86","","China","","", R.drawable.china));




        //FIJOS
        areaCodes.add(new AreaCode("+505","222","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","223","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","224","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","225","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","226","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","227","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","228","Nicaragua","Managua","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","229","Nicaragua","Managua","", R.drawable.nicaragua));

        areaCodes.add(new AreaCode("+505","231","Nicaragua","Leon","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","234","Nicaragua","Chinandega","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","235","Nicaragua","Chinandega","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","251","Nicaragua","Chontales","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","252","Nicaragua","Masaya","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","253","Nicaragua","Carazo","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","254","Nicaragua","Boaco","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","255","Nicaragua","Granada","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","256","Nicaragua","Rivas","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","257","Nicaragua","RAAS","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","258","Nicaragua","Rio San Juan","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","271","Nicaragua","Esteli","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","272","Nicaragua","Madriz","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","273","Nicaragua","Nueva Segovia","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","277","Nicaragua","Matagalpa","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","278","Nicaragua","Jinotega","", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","279","Nicaragua","RAAN","", R.drawable.nicaragua));

        //MOVIL CLARO
        areaCodes.add(new AreaCode("+505","550","Nicaragua","","Claro", R.drawable.nicaragua));

        for(int i = 570;i<=589;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));

        areaCodes.add(new AreaCode("+505","820","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","821","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","822","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","823","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","830","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","831","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","833","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","835","Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","836","Nicaragua","","Claro", R.drawable.nicaragua));

        for(int i = 840 ;i<=844;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","849","Nicaragua","","Claro", R.drawable.nicaragua));

        for(int i = 850 ;i<=854;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));
        for(int i = 860 ;i<=866;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","869","Nicaragua","","Claro", R.drawable.nicaragua));

        for(int i = 870 ;i<=874;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));
        for(int i = 882 ;i<=885;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));
        for(int i = 890 ;i<=894;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro", R.drawable.nicaragua));

        //MOVISTAR
        for(int i = 750 ;i<=755;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        for(int i = 760 ;i<=789;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        for(int i = 810 ;i<=819;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        for(int i = 824 ;i<=829;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","832", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","837", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","838", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","839", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        for(int i = 845 ;i<=848;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        for(int i = 855 ;i<=859;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        areaCodes.add(new AreaCode("+505","867", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","868", "Nicaragua", "", "Movistar", R.drawable.nicaragua));
        for(int i = 875 ;i<=881;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));
        for(int i = 886 ;i<=889;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));
        for(int i = 895 ;i<=899;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar", R.drawable.nicaragua));

        areaCodes.add(new AreaCode("+505","620", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","630", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","633", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","635", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","640", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","644", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","645", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","650", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","655", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","677", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","681", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        for(int i = 681 ;i<=690;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","695", "Nicaragua", "", "CooTel", R.drawable.nicaragua));
        areaCodes.add(new AreaCode("+505","699", "Nicaragua", "", "CooTel", R.drawable.nicaragua));




        ProcessModelTransaction<AreaCode> processModelTransaction =
                new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<AreaCode>() {
                    @Override
                    public void processModel(AreaCode model) {
                        // call some operation on model here
                        model.save();
                    }
                }).processListener(new ProcessModelTransaction.OnModelProcessListener<AreaCode>() {
                    @Override
                    public void onModelProcessed(long current, long total, AreaCode modifiedModel) {
                    }
                }).addAll(areaCodes).build();

        Transaction transaction = FlowManager.getDatabase(OperatorDB.class).beginTransactionAsync(processModelTransaction).build();
        transaction.execute();


        //for (AreaCode ac:areaCodes) {
           // ac.save();
        //}
    }
}

