package com.usach.tbdgrupo7.iservifast.Controllers;

/**
 * Created by matias on 31-12-15.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.Views.MainActivity;
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
    private Oferta servicios[];

    public OfrecerGet(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
        getServiciosOfrecidos(result);
        mainActivity.listarServicios(servicios);
    }

    public void getServiciosOfrecidos(String json) {
        try {
            JSONArray ja = new JSONArray(json);;
            servicios = new Oferta[ja.length()];
            String precio;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                Oferta servicio = new Oferta();
                servicio.setCategoria_idCategoria(row.getInt("categoria_idCategoria"));
                servicio.setComunidad_idComunidad(row.getInt("comunidad_idComunidad"));
                servicio.setDescripcion(row.getString("descripcion"));
                servicio.setIdServicio(row.getInt("idServicio"));
                precio = row.getString("precio");
                if(precio.equals("")==true){
                    servicio.setPrecio(-1);
                }
                else{
                    servicio.setPrecio(Integer.parseInt(precio));
                }
                servicio.setTitulo(row.getString("titulo"));
                servicio.setUsuario_idUsuario(row.getInt("usuario_idUsuario"));
                servicios[i]=servicio;
            }
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }// getActors(String actors)

}