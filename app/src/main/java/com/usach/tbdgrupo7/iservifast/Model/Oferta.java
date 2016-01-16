package com.usach.tbdgrupo7.iservifast.Model;

import java.io.Serializable;

/**
 * Created by matias on 28-12-15.
 */
public class Oferta implements Serializable{

    private String titulo;
    private String descripcion;
    private String fecha;
    private String precio;
    private String imagen;
    private String url;
    private int idServicio;
    private int categoria_idCategoria;
    private int comunidad_idComunidad;
    private int duracion;
    private int promedio;
    private int usuario_idUsuario;

    public Oferta(){
        this.duracion = 10;
        this.promedio = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCategoria_idCategoria() {
        return categoria_idCategoria;
    }

    public void setCategoria_idCategoria(int categoria_idCategoria) {
        this.categoria_idCategoria = categoria_idCategoria;
    }

    public int getComunidad_idComunidad() {
        return comunidad_idComunidad;
    }

    public void setComunidad_idComunidad(int comunidad_idComunidad) {
        this.comunidad_idComunidad = comunidad_idComunidad;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getPromedio() {
        return promedio;
    }

    public void setPromedio(int promedio) {
        this.promedio = promedio;
    }

    public int getUsuario_idUsuario() {
        return usuario_idUsuario;
    }

    public void setUsuario_idUsuario(int usuario_idUsuario) {
        this.usuario_idUsuario = usuario_idUsuario;
    }
}
