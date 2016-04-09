package com.edxavier.cerberus_sms.helpers;

import android.content.Context;

import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.models.AreaCode;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 07/11/2015.
 */
public class AreaCodeHelper {
    public static void initAreaCodes(Context cntx){


        ArrayList<AreaCode> areaCodes = new ArrayList<AreaCode>();

        //codigoPais, CodigoArea, Pais, Area, Operador
        areaCodes.add(new AreaCode("+1","209",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","213",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","310",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","323",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","408",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));

        areaCodes.add(new AreaCode("+1","415",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","510",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","530",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","559",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","562",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));

        areaCodes.add(new AreaCode("+1","619",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","626",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","650",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","661",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","707",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));

        areaCodes.add(new AreaCode("+1","714",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","760",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","805",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","818",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","831",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));

        areaCodes.add(new AreaCode("+1","858",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","909",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","916",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","925",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));
        areaCodes.add(new AreaCode("+1","949",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.ca),""));


        areaCodes.add(new AreaCode("+1","305",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","321",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","352",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","386",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","407",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","561",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","727",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","754",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));

        areaCodes.add(new AreaCode("+1","772",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","786",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","813",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","850",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","863",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","904",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","941",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));
        areaCodes.add(new AreaCode("+1","954",cntx.getResources().getString(R.string.usa),cntx.getResources().getString(R.string.fl),""));


        areaCodes.add(new AreaCode("+501","","Belice","",""));
        areaCodes.add(new AreaCode("+502","","Guatemala","",""));
        areaCodes.add(new AreaCode("+503","","El Salvador","",""));
        areaCodes.add(new AreaCode("+504","","Honduras","",""));
        areaCodes.add(new AreaCode("+506","","Costa Rica","",""));
        areaCodes.add(new AreaCode("+507","","Panama","",""));
        areaCodes.add(new AreaCode("+509","","Haití","",""));

        areaCodes.add(new AreaCode("+51",""," Perú","",""));
        areaCodes.add(new AreaCode("+52","","Mexico","",""));
        areaCodes.add(new AreaCode("+53","","Cuba","",""));
        areaCodes.add(new AreaCode("+54",""," Argentina","",""));
        areaCodes.add(new AreaCode("+55","","Brasil","",""));
        areaCodes.add(new AreaCode("+56","","Chile","",""));
        areaCodes.add(new AreaCode("+57","","Colombia","",""));
        areaCodes.add(new AreaCode("+58","","Venezuela","",""));


        areaCodes.add(new AreaCode("+32","","Belgica","",""));
        areaCodes.add(new AreaCode("+33","","Francia","",""));
        areaCodes.add(new AreaCode("+34","","España","",""));
        areaCodes.add(new AreaCode("+39","","Italia","",""));

        areaCodes.add(new AreaCode("+40","","Rumania","",""));
        areaCodes.add(new AreaCode("+41","","Suiza","",""));
        areaCodes.add(new AreaCode("+43","","Austria","",""));
        areaCodes.add(new AreaCode("+44","","Inglaterra","",""));
        areaCodes.add(new AreaCode("+45","","Dinamarca","",""));
        areaCodes.add(new AreaCode("+49","","Alemania","",""));

        areaCodes.add(new AreaCode("+7","","Rusia","",""));
        areaCodes.add(new AreaCode("+80","","Japón","",""));
        areaCodes.add(new AreaCode("+81","","Corea del Sur","",""));
        areaCodes.add(new AreaCode("+86","","China","",""));




        //FIJOS
        areaCodes.add(new AreaCode("+505","222","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","223","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","224","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","225","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","226","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","227","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","228","Nicaragua","Managua",""));
        areaCodes.add(new AreaCode("+505","229","Nicaragua","Managua",""));

        areaCodes.add(new AreaCode("+505","231","Nicaragua","Leon",""));
        areaCodes.add(new AreaCode("+505","234","Nicaragua","Chinandega",""));
        areaCodes.add(new AreaCode("+505","235","Nicaragua","Chinandega",""));
        areaCodes.add(new AreaCode("+505","251","Nicaragua","Chontales",""));
        areaCodes.add(new AreaCode("+505","252","Nicaragua","Masaya",""));
        areaCodes.add(new AreaCode("+505","253","Nicaragua","Carazo",""));
        areaCodes.add(new AreaCode("+505","254","Nicaragua","Boaco",""));
        areaCodes.add(new AreaCode("+505","255","Nicaragua","Granada",""));
        areaCodes.add(new AreaCode("+505","256","Nicaragua","Rivas",""));
        areaCodes.add(new AreaCode("+505","257","Nicaragua","RAAS",""));
        areaCodes.add(new AreaCode("+505","258","Nicaragua","Rio San Juan",""));
        areaCodes.add(new AreaCode("+505","271","Nicaragua","Esteli",""));
        areaCodes.add(new AreaCode("+505","272","Nicaragua","Madriz",""));
        areaCodes.add(new AreaCode("+505","273","Nicaragua","Nueva Segovia",""));
        areaCodes.add(new AreaCode("+505","277","Nicaragua","Matagalpa",""));
        areaCodes.add(new AreaCode("+505","278","Nicaragua","Jinotega",""));
        areaCodes.add(new AreaCode("+505","279","Nicaragua","RAAN",""));

        //MOVIL CLARO
        areaCodes.add(new AreaCode("+505","550","Nicaragua","","Claro"));

        for(int i = 570;i<=589;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));

        areaCodes.add(new AreaCode("+505","820","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","821","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","822","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","823","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","830","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","831","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","833","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","835","Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","836","Nicaragua","","Claro"));

        for(int i = 840 ;i<=844;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","849","Nicaragua","","Claro"));

        for(int i = 850 ;i<=854;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));
        for(int i = 860 ;i<=866;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));
        areaCodes.add(new AreaCode("+505","869","Nicaragua","","Claro"));

        for(int i = 870 ;i<=874;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));
        for(int i = 882 ;i<=885;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));
        for(int i = 890 ;i<=894;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Claro"));

        //MOVISTAR
        for(int i = 750 ;i<=755;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));

        for(int i = 760 ;i<=789;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));

        for(int i = 810 ;i<=819;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));

        for(int i = 824 ;i<=829;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));
        areaCodes.add(new AreaCode("+505","832", "Nicaragua", "", "Movistar"));
        areaCodes.add(new AreaCode("+505","837", "Nicaragua", "", "Movistar"));
        areaCodes.add(new AreaCode("+505","838", "Nicaragua", "", "Movistar"));
        areaCodes.add(new AreaCode("+505","839", "Nicaragua", "", "Movistar"));
        for(int i = 845 ;i<=848;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));

        for(int i = 855 ;i<=859;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));

        areaCodes.add(new AreaCode("+505","867", "Nicaragua", "", "Movistar"));
        areaCodes.add(new AreaCode("+505","868", "Nicaragua", "", "Movistar"));
        for(int i = 875 ;i<=881;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));
        for(int i = 886 ;i<=889;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));
        for(int i = 895 ;i<=899;i++)
            areaCodes.add(new AreaCode("+505",String.valueOf(i),"Nicaragua","","Movistar"));


        for (AreaCode ac:areaCodes) {
            ac.save();
        }
    }
}

