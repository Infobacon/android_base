package com.usach.tbdgrupo7.iservifast.Controllers;

/**
 * Created by matias on 31-12-15.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Views.FavoritosActivity;
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

public class OfrecerGet2 extends AsyncTask<String, Void, String> {

    private SSLTrust sT;
    private FavoritosActivity favoritosActivity;
    private OfertaGet servicios[];

    public OfrecerGet2(FavoritosActivity favoritosActivity) {
        this.favoritosActivity = favoritosActivity;
        this.sT = new SSLTrust();
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

    @Override
    protected void onPostExecute(String result) {
        if(result!=null) {
            getServiciosOfrecidos(result);
            favoritosActivity.listarServicios(servicios);
        }
        else{
            favoritosActivity.error_internet();
        }
    }

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
                servicio.setUsuario_idUsuario(row.getInt("usuario_idUsuario"));
                servicios[i]=servicio;
            }
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }// getActors(String actors)

}