package com.usach.tbdgrupo7.iservifast.Model;

import java.io.Serializable;

/**
 * Created by matias on 11-01-16.
 */
public class Comunidad implements Serializable{

    private String nombre;
    private int idComunidad;
    private String comuna;
    private String ciudad;


    public Comunidad(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdComunidad() {
        return idComunidad;
    }

    public void setIdComunidad(int idComunidad) {
        this.idComunidad = idComunidad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
