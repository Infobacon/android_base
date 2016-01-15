package com.usach.tbdgrupo7.iservifast.Controllers.Favoritos;

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Views.ServicioOfrecidoActivity;
import com.usach.tbdgrupo7.iservifast.utilities.SSLTrust;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FavoritoPost extends AsyncTask<String, Void, String> {

    private SSLTrust sT;
    private ServicioOfrecidoActivity servicioOfrecidoActivity;

    public FavoritoPost(ServicioOfrecidoActivity servicioOfrecidoActivity) {
        this.sT = new SSLTrust();
        this.servicioOfrecidoActivity = servicioOfrecidoActivity;
    }

    /***
     * Envia consulta POST.
     * @param params [0] URL, [1] parametros de envio (formato JSON)
     */
    public String sendData(String... params){
        try {

            URL url = new URL(params[0]);
            sT.trustEveryone(); //necesario para conexión ssl
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setFixedLengthStreamingMode(params[1].getBytes().length);
            connection.connect();

            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
            os.write(params[1].getBytes());
            os.flush();
            return "OK";
        }catch(Exception e){
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return "ERROR";
    }

    @Override
    protected String doInBackground(String... data) {
        return sendData(data);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("OK")) {
            servicioOfrecidoActivity.favoritoCambiado(result);
        }
        else{
            servicioOfrecidoActivity.error_internet();
        }
    }


}