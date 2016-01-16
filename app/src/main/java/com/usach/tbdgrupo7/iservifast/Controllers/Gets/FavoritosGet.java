package com.usach.tbdgrupo7.iservifast.Controllers.Gets;

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Views.FavoritosActivity;
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
public class FavoritosGet extends AsyncTask<String, Void, String>{

    private FavoritosActivity favoritosActivity;
    private ServicioOfrecidoActivity servicioOfrecidoActivity;
    private SSLTrust sT;
    private Favorito favoritos[];
    private int flag;
    private static final int MAIN_ACTIVITY = 1;
    private static final int SERVICIOS_SOLICITADOS = 2;
    private static final int MIS_SERVICIOS_OFRECIDOS = 3;
    private static final int MIS_SERVICIOS_SOLICITADOS = 4;
    private static final int FAVORITOS = 5;
    private static final short SERVICIO_OFRECIDO = 6;
    private static final short SERVICIO_SOLICITADO = 7;

    public FavoritosGet(FavoritosActivity favoritosActivity, int flag){
        this.favoritosActivity = favoritosActivity;
        this.sT = new SSLTrust();
        this.flag = flag;
    }

    public FavoritosGet(ServicioOfrecidoActivity servicioOfrecidoActivity, int flag){
        this.servicioOfrecidoActivity = servicioOfrecidoActivity;
        this.sT = new SSLTrust();
        this.flag = flag;
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
    }

    public void getServicios(String json) {
        try {
            JSONArray ja = new JSONArray(json);;
            favoritos = new Favorito[ja.length()];
            String precio;
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

    @Override
    protected void onPostExecute(String result) {
        if(flag==FAVORITOS){
            if(result!=null) {
                getServicios(result);
                favoritosActivity.getServicios(favoritos);
            }
            else{
                favoritosActivity.error_internet();
            }
        }
        else if(flag==SERVICIO_OFRECIDO){
            if(result!=null) {
                getServicios(result);
                servicioOfrecidoActivity.setearFavorito(favoritos);
            }
            else{
                servicioOfrecidoActivity.error_internet();
            }
        }
    }
}
