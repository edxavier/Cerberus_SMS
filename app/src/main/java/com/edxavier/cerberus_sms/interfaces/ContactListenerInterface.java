package com.edxavier.cerberus_sms.interfaces;

import com.edxavier.cerberus_sms.db.models.Contactos;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public interface ContactListenerInterface {
    void readContactsFinish(ArrayList<Contactos> contactos);
    void onProgres(Contactos contacto);
}
