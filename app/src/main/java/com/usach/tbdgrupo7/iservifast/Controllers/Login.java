package com.usach.tbdgrupo7.iservifast.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.Views.LoginActivity;
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

public class Login extends AsyncTask<String, Void, String> {

    private SSLTrust sT;
    private Context context;
    private String input_usuario;
    private String input_password;
    private Usuario user;
    private boolean resultadoValidaciones;
    private LoginActivity loginActivity;

    public Login(LoginActivity loginActivity, String usuario, String password) {
        this.context = context;
        this.input_usuario = usuario;
        this.input_password = password;
        this.sT = new SSLTrust();
        this.loginActivity = loginActivity;
    }

    @Override
    protected void onPreExecute(){
        loginActivity.abrirProgressDialog();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            sT.trustEveryone(); //necesario para conexión ssl
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        try {
            resultadoValidaciones= validarCredenciales(result, input_usuario, input_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(resultadoValidaciones==true){
            loginActivity.onLoginSuccess(user);
        }
        else{//no hay coincidencias para el usuario y contraseña ingresados
            loginActivity.onLoginFailed();
        }
    }

    public boolean validarCredenciales(String json, String usuario, String password) throws JSONException {
        JSONArray ja = new JSONArray(json);
        int i;
        String usuario_aux,password_aux;
        for (i = 0; i < ja.length(); i++) {
            JSONObject row = ja.getJSONObject(i);
            usuario_aux = row.getString("usuario");
            password_aux = row.getString("password");
            if(matchUsuarioPassword(usuario,usuario_aux,password,password_aux)==true){
                user = new Usuario();
                user.setIdUsuario(row.getInt("idUsuario"));
                user.setUsuario(usuario);
                user.setNombre(row.getString("nombre"));
                user.setApellido(row.getString("apellido"));
                user.setPassword(password);
                user.setEmail(row.getString("mail"));
                user.setRegion(row.getString("region"));
                user.setCiudad(row.getString("ciudad"));
                user.setComuna(row.getString("comuna"));
                user.setDireccion(row.getString("direccion"));
                return true;
            }
        }
        return false;
    }

    public boolean matchUsuarioPassword(String usuario1, String usuario2,String password1, String password2){
        if(usuario1.equals(usuario2)&&password1.equals(password2)) {
            return true;
        }
        return false;
    }
}