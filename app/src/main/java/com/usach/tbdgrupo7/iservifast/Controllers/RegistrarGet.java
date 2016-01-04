package com.usach.tbdgrupo7.iservifast.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.Views.RegistrarActivity;
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
 * @author: Grupo 7
 */
public class RegistrarGet extends AsyncTask<String, Void, String> {

    private SSLTrust sT;
    private Context context;
    private RegistrarActivity registrarActivity;
    private String usuarioIngresado;
    private String mailIngresado;
    private String[] usuarios;
    private String[] mails;
    boolean usuarioDisponible = false;
    boolean mailDisponible = false;
    private Usuario user;

    public RegistrarGet(RegistrarActivity registrarActivity, Usuario user) {
        this.registrarActivity = registrarActivity;
        this.sT = new SSLTrust();
        this.user = user;
        this.usuarioIngresado = user.getUsuario();
        this.mailIngresado = user.getEmail();
    }

    @Override
    protected void onPreExecute(){
        registrarActivity.abrirProgressDialog();
    }

    @Override
    protected String doInBackground(String...url) {
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
            getUsuariosMails(result, usuarioIngresado, mailIngresado);
            boolean usuarioDisponible = usuarioDisponible(usuarios,usuarioIngresado);
            boolean mailDisponible = mailDisponible(mails,mailIngresado);

            if(usuarioDisponible  == false) {
                registrarActivity.cerrarProgressDialog();
                registrarActivity.usuarioEnUso();
            }

            if(mailDisponible == false) {
                registrarActivity.cerrarProgressDialog();
                registrarActivity.mailEnUso();
            }

            if(usuarioDisponible == true && mailDisponible == true){
                registrarActivity.enviarJsonYLoguear(user);
            }
            else{
                registrarActivity.cerrarProgressDialog();
                registrarActivity.error();
            }
        }
        else{
            registrarActivity.cerrarProgressDialog();
            registrarActivity.error();
        }
    }

    public void getUsuariosMails(String json,String usuario,String mail) {
        try {
            JSONArray ja = new JSONArray(json);
            usuarios = new String[ja.length()];
            mails = new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                usuarios[i]=row.getString("usuario");
                mails[i]=row.getString("mail");
            }
            setUsuarioDisponible(usuarioDisponible(usuarios, usuario));
            setMailDisponible(mailDisponible(mails,mail));
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
    }// getActors(String actors)

    private boolean usuarioDisponible(String[] usuarios, String usuario){
        int i;
        for(i=0;i<usuarios.length;i++){
            if(usuarios[i].equals(usuario)){
                return false;//False: usuario en uso, no dispnible
            }
        }
        return true;//True: usuario disponible
    }

    private boolean mailDisponible(String[] mails, String mail){
        int i;
        for(i=0;i<mails.length;i++){
            if(mails[i].equals(mail)){
                return false;//False: mail en uso, no dispnible
            }
        }
        return true;//True: mail disponible
    }

    public boolean isUsuarioDisponible() {
        return usuarioDisponible;
    }

    public void setUsuarioDisponible(boolean usuarioDisponible) {
        this.usuarioDisponible = usuarioDisponible;
    }

    public String[] getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String[] usuarios) {
        this.usuarios = usuarios;
    }

    public String[] getMails() {
        return mails;
    }

    public void setMails(String[] mails) {
        this.mails = mails;
    }

    public boolean isMailDisponible() {
        return mailDisponible;
    }

    public void setMailDisponible(boolean mailDisponible) {
        this.mailDisponible = mailDisponible;
    }
}