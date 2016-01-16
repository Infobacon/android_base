package com.usach.tbdgrupo7.iservifast.Controllers.Gets;

/**
 * Created by matias on 31-12-15.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Views.FavoritosActivity;
import com.usach.tbdgrupo7.iservifast.Views.MainActivity;
import com.usach.tbdgrupo7.iservifast.Views.MisServiciosOfrecidosActivity;
import com.usach.tbdgrupo7.iservifast.Views.MisServiciosSolicitados;
import com.usach.tbdgrupo7.iservifast.utilities.SSLTrust;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class OfrecerGet extends AsyncTask<String, Void, String> {

    private SSLTrust sT;
    private MainActivity mainActivity;
    private FavoritosActivity favoritosActivity;
    private MisServiciosOfrecidosActivity misServiciosOfrecidosActivity;
    private MisServiciosSolicitados misServiciosSolicitados;
    private OfertaGet servicios[];
    private int flag;
    private static final int MAIN_ACTIVITY = 1;
    private static final int SERVICIOS_SOLICITADOS = 2;
    private static final int MIS_SERVICIOS_OFRECIDOS = 3;
    private static final int MIS_SERVICIOS_SOLICITADOS = 4;
    private static final int FAVORITOS = 5;
    private static final short SERVICIO_OFRECIDO = 6;
    private static final short SERVICIO_SOLICITADO = 7;

    public OfrecerGet(MainActivity mainActivity, int flag) {
        this.mainActivity = mainActivity;
        this.sT = new SSLTrust();
        this.flag = flag;
    }

    public OfrecerGet(FavoritosActivity favoritosActivity, int flag) {
        this.favoritosActivity = favoritosActivity;
        this.sT = new SSLTrust();
        this.flag = flag;
    }

    public OfrecerGet(MisServiciosOfrecidosActivity misServiciosOfrecidosActivity, int flag) {
        this.misServiciosOfrecidosActivity = misServiciosOfrecidosActivity;
        this.sT = new SSLTrust();
        this.flag = flag;
    }

    public OfrecerGet(MisServiciosSolicitados misServiciosSolicitados, int flag) {
        this.misServiciosSolicitados = misServiciosSolicitados;
        this.sT = new SSLTrust();
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... url) {
        try {
            URL u = new URL(url[0]);
            sT.trustEveryone(); //necesario para conexión ssl
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (MalformedURLException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (ProtocolException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (IOException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }// doInBackground(String... urls)

    public void getServiciosOfrecidos(String json) {
        try {
            JSONArray ja = new JSONArray(json);;
            servicios = new OfertaGet[ja.length()];
            String precio;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                OfertaGet servicio = new OfertaGet();
                servicio.setCategoria_idCategoria(row.getInt("categoria_idCategoria"));
                servicio.setCategoria(row.getString("catnombre"));
                servicio.setComunidad(row.getString("comnombre"));
                servicio.setDescripcion(row.getString("descripcion"));
                servicio.setIdServicio(row.getInt("idServicio"));
                servicio.setRegion(row.getString("region"));
                servicio.setUsuario(row.getString("unick"));
                servicio.setPrecio(row.getString("precio"));
                servicio.setTitulo(row.getString("titulo"));
                servicio.setUrl(row.getString("imagen"));
                servicio.setUsuario_idUsuario(row.getInt("usuario_idUsuario"));
                servicios[i]=servicio;
            }
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(flag==MAIN_ACTIVITY){
            if (result != null) {
                getServiciosOfrecidos(result);
                mainActivity.listarServicios(servicios);
            }
            else{
                mainActivity.error_internet();
            }
        }
        else if(flag==FAVORITOS){
            if (result != null) {
                getServiciosOfrecidos(result);
                favoritosActivity.listarServicios(servicios);
            }
            else{
                favoritosActivity.error_internet();
            }
        }
        else if(flag==MIS_SERVICIOS_OFRECIDOS){
            if (result != null) {
                getServiciosOfrecidos(result);
                misServiciosOfrecidosActivity.listarServicios(servicios);
            }
            else{
                misServiciosOfrecidosActivity.error_internet();
            }
        }
        else if(flag==MIS_SERVICIOS_SOLICITADOS){
            if (result != null) {
                getServiciosOfrecidos(result);
                misServiciosSolicitados.listarServicios(servicios);
            }
            else{
                misServiciosSolicitados.error_internet();
            }
        }
    }
}