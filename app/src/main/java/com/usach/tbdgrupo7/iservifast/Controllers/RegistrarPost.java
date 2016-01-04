package com.usach.tbdgrupo7.iservifast.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.Views.RegistrarActivity;
import com.usach.tbdgrupo7.iservifast.utilities.SSLTrust;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by matias on 29-12-15.
 */
public class RegistrarPost extends AsyncTask<String, Void, String>{

    private SSLTrust sT;
    private RegistrarActivity registrarActivity;
    private Usuario usuario;

    public RegistrarPost(RegistrarActivity registrarActivity, Usuario usuario){
        this.registrarActivity = registrarActivity;
        this.usuario = usuario;
        this.sT = new SSLTrust();
    }

    @Override
    protected String doInBackground(String...parametros) {
        try {
            String json = parametros[1];
            URL u = new URL(parametros[0]);
            sT.trustEveryone(); //necesario para conexión ssl
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setFixedLengthStreamingMode(parametros[1].getBytes().length);
            connection.connect();
            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
            os.write(parametros[1].getBytes());
            os.flush();
            return "OK";
        } catch (Exception e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return "ERROR";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("OK")) {
            registrarActivity.loguear(usuario);
        } else {
            registrarActivity.error();
        }
    }
}