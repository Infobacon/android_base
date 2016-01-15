package com.usach.tbdgrupo7.iservifast.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Comunidad;
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

/**
 * Created by matias on 11-01-16.
 */
public class ComunidadesGet extends AsyncTask<String, Void, String>{

    private MainActivity mainActivity;
    private SSLTrust sT;
    private Comunidad comunidades[];


    public ComunidadesGet(MainActivity mainActivity){
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
    protected void onPostExecute(String result){
        if(result!=null) {
            getServiciosOfrecidos(result);
            mainActivity.getComunidades(comunidades);
        }
        else{
            mainActivity.error_internet();
        }
    }

    public void getServiciosOfrecidos(String json) {
        try {
            JSONArray ja = new JSONArray(json);;
            comunidades = new Comunidad[ja.length()];
            String precio;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                Comunidad com = new Comunidad();
                com.setIdComunidad(row.getInt("idComunidad"));
                com.setNombre(row.getString("nombre"));
                com.setComuna(row.getString("comuna"));
                com.setCiudad(row.getString("ciudad"));
                comunidades[i]=com;
            }
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }
}
