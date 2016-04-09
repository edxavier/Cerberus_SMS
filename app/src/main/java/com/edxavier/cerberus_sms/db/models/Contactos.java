package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by Eder Xavier Rojas on 12/10/2015.
 */
public class Contactos extends Model{
    @Column
    private String nombre;
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE, index = true)
    private String numero;

    public Contactos() {
    }

    public Contactos(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
