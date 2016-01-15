package com.usach.tbdgrupo7.iservifast.Controllers.Favoritos;

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Views.ServicioOfrecidoActivity;
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
public class FavoritoGetInicio extends AsyncTask<String, Void, String>{

    private ServicioOfrecidoActivity servicioOfrecidoActivity;
    private SSLTrust sT;
    private Favorito favoritos[];


    public FavoritoGetInicio(ServicioOfrecidoActivity servicioOfrecidoActivity){
        this.servicioOfrecidoActivity = servicioOfrecidoActivity;
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
            servicioOfrecidoActivity.setearFavorito(favoritos);
        }
        else{
            servicioOfrecidoActivity.error_internet();
        }
    }

    public void getServiciosOfrecidos(String json) {
        try {
            JSONArray ja = new JSONArray(json);;
            favoritos = new Favorito[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                Favorito fav = new Favorito();
                fav.setIdFavorito(row.getInt("idFavorito"));
                fav.setServicio_idServicio(row.getInt("servicio_idServicio"));
                fav.setUsuario_idUsuario(row.getInt("usuario_idUsuario"));
                favoritos[i]=fav;
            }
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }
}
