package com.edxavier.cerberus_sms.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Eder Xavier Rojas on 15/10/2015.
 */
@Table(name = "sms_locks")
public class Sms_Lock extends Model {
    @Column
    private String nombre;
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE, index = true)
    private String numero;
    @Column
    private int conteo = 0;

    public Sms_Lock(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public Sms_Lock() {
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

    public int getConteo() {
        return conteo;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
    }
}
