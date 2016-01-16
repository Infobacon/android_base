package com.usach.tbdgrupo7.iservifast.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by matias on 12-01-16.
 */
public class OfertaGet implements Serializable{

    private String titulo;
    private String nombre;
    private String descripcion;
    private String usuario;
    private String region;
    private String categoria;
    private String comunidad;
    private String precio;
    private Bitmap imagen;
    private String url;
    private byte[] imagen_comprimida;
    private int idServicio;
    private int categoria_idCategoria;
    private int comunidad_idComunidad;
    private int duracion;
    private int promedio;
    private int usuario_idUsuario;

    public OfertaGet(){
        this.duracion = 10;
        this.promedio = 0;
    }

    public byte[] getImagen_comprimida() {
        return imagen_comprimida;
    }

    public void setImagen_comprimida(byte[] imagen_comprimida) {
        this.imagen_comprimida = imagen_comprimida;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getComunidad() {
        return comunidad;
    }

    public void setComunidad(String comunidad) {
        this.comunidad = comunidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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