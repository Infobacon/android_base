package com.usach.tbdgrupo7.iservifast.Model;

import java.io.Serializable;

/**
 * Created by matias on 11-01-16.
 */
public class Favorito implements Serializable{

    private int idFavorito;
    private int servicio_idServicio;
    private int usuario_idUsuario;

    public Favorito(){

    }

    public int getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }

    public int getServicio_idServicio() {
        return servicio_idServicio;
    }

    public void setServicio_idServicio(int servicio_idServicio) {
        this.servicio_idServicio = servicio_idServicio;
    }

    public int getUsuario_idUsuario() {
        return usuario_idUsuario;
    }

    public void setUsuario_idUsuario(int usuario_idUsuario) {
        this.usuario_idUsuario = usuario_idUsuario;
    }
}
