package com.usach.tbdgrupo7.iservifast.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.utilities.SSLTrust;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by matias on 28-12-15.
 */
public class OfrecerPost extends AsyncTask<String, Void, String>{

    private SSLTrust sT;
    private Context context;

    public OfrecerPost(Context context) {
        this.context = context;
        this.sT = new SSLTrust();
    }// HttpPost(Context context)

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
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setFixedLengthStreamingMode(params[1].getBytes().length);
            connection.connect();

            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
            os.write(params[1].getBytes());
            os.flush();
            /*OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(params[1]);
            out.close();*/
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
        Intent intent = new Intent("httpPost").putExtra("post", result);
        context.sendBroadcast(intent);
    }// onPostExecute(String result)

    public JSONObject setActor(Oferta oferta) {
        // build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("usuario_idUsuario", oferta.getUsuario_idUsuario());
            jsonObject.accumulate("titulo", oferta.getTitulo());
            jsonObject.accumulate("descripcion", oferta.getDescripcion());
            jsonObject.accumulate("categoria_idCategoria", oferta.getCategoria_idCategoria());
            jsonObject.accumulate("comunidad_idComunidad", oferta.getComunidad_idComunidad());
            jsonObject.accumulate("duracion", oferta.getDuracion());
            jsonObject.accumulate("precio", oferta.getPrecio());
            jsonObject.accumulate("promedio", oferta.getPromedio());
            jsonObject.accumulate("usuario_idUsuario", oferta.getUsuario_idUsuario());
            return jsonObject;

        }catch(JSONException je){
            Log.e("ERROR",this.getClass().toString()+ " - "+ je.getMessage());
        }
        return null;
    }


}// HttpPost