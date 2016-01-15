package com.usach.tbdgrupo7.iservifast.Model;

import java.io.Serializable;

/**
 * Created by matias on 11-01-16.
 */
public class Categoria implements Serializable{

    private String nombre;
    private int idCategoria;
    private String categorias;

    public Categoria(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
}
