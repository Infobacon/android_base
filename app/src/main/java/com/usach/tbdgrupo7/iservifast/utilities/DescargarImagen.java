package com.usach.tbdgrupo7.iservifast.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Views.FavoritosActivity;
import com.usach.tbdgrupo7.iservifast.Views.MainActivity;
import com.usach.tbdgrupo7.iservifast.Views.MisServiciosOfrecidosActivity;
import com.usach.tbdgrupo7.iservifast.Views.MisServiciosSolicitados;
import com.usach.tbdgrupo7.iservifast.Views.ServicioOfrecidoActivity;
import com.usach.tbdgrupo7.iservifast.Views.ServiciosSolicitadosActivity;

import java.io.InputStream;

/**
 * Created by matias on 13-01-16.
 */
public class DescargarImagen extends AsyncTask<String, Void, String> {

    private int position;
    private MainActivity mainActivity;
    private ServiciosSolicitadosActivity serviciosSolicitadosActivity;
    private MisServiciosOfrecidosActivity misServiciosOfrecidosActivity;
    private MisServiciosSolicitados misServiciosSolicitados;
    private FavoritosActivity favoritosActivity;
    private ServicioOfrecidoActivity servicioOfrecidoActivity;
    private String global;
    private Bitmap bitmap;
    private int flag;
    private static final int MAIN_ACTIVITY = 1;
    private static final int SERVICIOS_SOLICITADOS = 2;
    private static final int MIS_SERVICIOS_OFRECIDOS = 3;
    private static final int MIS_SERVICIOS_SOLICITADOS = 4;
    private static final int FAVORITOS = 5;
    private static final int SERVICIO_OFRECIDO = 6;
    private static final int SERVICIO_SOLICITADO = 7;

    public DescargarImagen(MainActivity mainActivity, int position, short flag) {
        this.mainActivity=mainActivity;
        this.position=position;
        this.flag = flag;
    }

    public DescargarImagen(ServiciosSolicitadosActivity serviciosSolicitadosActivity, int position, int flag) {
        this.serviciosSolicitadosActivity=serviciosSolicitadosActivity;
        this.position=position;
        this.flag = flag;
    }

    public DescargarImagen(MisServiciosOfrecidosActivity misServiciosOfrecidosActivity, int position, int flag) {
        this.misServiciosOfrecidosActivity=misServiciosOfrecidosActivity;
        this.position=position;
        this.flag = flag;
    }

    public DescargarImagen(MisServiciosSolicitados misServiciosSolicitados, int position, int flag) {
        this.misServiciosSolicitados=misServiciosSolicitados;
        this.position=position;
        this.flag = flag;
    }

    public DescargarImagen(FavoritosActivity favoritosActivity, int position, int flag) {
        this.favoritosActivity=favoritosActivity;
        this.position=position;
        this.flag = flag;
    }

    public DescargarImagen(ServicioOfrecidoActivity servicioOfrecidoActivity, int flag) {
        this.servicioOfrecidoActivity=servicioOfrecidoActivity;
        this.flag = flag;
    }

    protected String doInBackground(String... urls) {
        global = urls[0];
        String urldisplay = urls[0];
        bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        if(bitmap!=null){
            return "OK";
        }
        else{
            return "NO";
        }
    }

    protected void onPostExecute(String result) {
        if(flag==MAIN_ACTIVITY){
            if (result.equals("OK")) {
                mainActivity.llegoImagen(position,bitmap, result);
            }
            else{
                mainActivity.llegoImagen(position,bitmap, result);
            }
        }
        else if(flag==SERVICIOS_SOLICITADOS) {
            if (result.equals("OK")) {
                serviciosSolicitadosActivity.llegoImagen(position,bitmap, result);
            }
            else{
                serviciosSolicitadosActivity.llegoImagen(position,bitmap, result);
            }
        }
        else if(flag==MIS_SERVICIOS_OFRECIDOS) {
            if (result.equals("OK")) {
                misServiciosOfrecidosActivity.llegoImagen(position,bitmap, result);
            }
            else{
                misServiciosOfrecidosActivity.llegoImagen(position,bitmap, result);
            }
        }
        else if(flag==MIS_SERVICIOS_SOLICITADOS){
            if (result.equals("OK")) {
                misServiciosSolicitados.llegoImagen(position,bitmap, result);
            }
            else{
                misServiciosSolicitados.llegoImagen(position,bitmap, result);
            }
        }
        else if(flag==FAVORITOS){
            if (result.equals("OK")) {
                favoritosActivity.llegoImagen(position,bitmap, result);
            }
            else{
                favoritosActivity.llegoImagen(position,bitmap, result);
            }
        }
        else if(flag==SERVICIO_OFRECIDO){
            if (result.equals("OK")) {
                servicioOfrecidoActivity.llegoImagen(bitmap, result);
            }
            else{
                servicioOfrecidoActivity.llegoImagen(bitmap, result);
            }
        }
    }
}