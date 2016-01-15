package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.RegistrarGet;
import com.usach.tbdgrupo7.iservifast.Controllers.RegistrarPost;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.JsonHandler;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistrarActivity extends AppCompatActivity {
    private static final String TAG = "RegistrarActivity";

    @Bind(R.id.input_usuario)EditText _usuarioText;
    @Bind(R.id.input_nombre)EditText _nombreText;
    @Bind(R.id.input_apellido)EditText _apellidoText;
    @Bind(R.id.input_password)EditText _passwordText;
    @Bind(R.id.input_password2)TextView _password2Text;
    @Bind(R.id.input_email)TextView _mailText;
    @Bind(R.id.input_region)TextView _regionText;
    @Bind(R.id.input_ciudad)TextView _ciudadText;
    @Bind(R.id.input_comuna)TextView _comunaText;
    @Bind(R.id.btn_signup)Button _signupButton;
    @Bind(R.id.input_direccion)TextView _direccionText;
    @Bind(R.id.link_login)TextView _loginLink;

    private String usuario;
    private String nombre;
    private String apellido;
    private String password;
    private String password2;
    private String mail;
    private String region;
    private String ciudad;
    private String comuna;
    private String direccion;
    private final short REQUEST_LOGIN = 1;
    boolean usuarioDisponible;
    boolean emailDisponible;
    private ProgressDialog progressDialog;
    private final short largoMinNombre = 2;
    private final short largoMaxNombre = 30;
    private final short largoMinApellido = 2;
    private final short largoMaxApellido = 30;
    private final short largoMinUsuario = 6;
    private final short largoMaxUsuario = 12;
    private final short largoMinPassword = 6;
    private final short largoMaxPassword = 12;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(RegistrarActivity.this,R.style.AppTheme_Dark_Dialog);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                finish();
            }
        });

    }

    public void signup() throws InterruptedException, ExecutionException, TimeoutException {

        usuario = _usuarioText.getText().toString();
        nombre = _nombreText.getText().toString();
        apellido = _apellidoText.getText().toString();
        password = _passwordText.getText().toString();
        password2 = _password2Text.getText().toString();
        mail = _mailText.getText().toString();
        region = _regionText.getText().toString();
        ciudad = _ciudadText.getText().toString();
        comuna = _comunaText.getText().toString();
        direccion = _direccionText.getText().toString();

        if (validarCampos()==false) {
            camposInvalidos();
            return;
        }

        SystemUtilities su = new SystemUtilities(this.getApplicationContext());

        if (su.isNetworkAvailable()) {
            IntentFilter intentFilter = new IntentFilter("httpPost");

            Usuario us = new Usuario();
            us.setUsuario(usuario);
            us.setNombre(nombre);
            us.setApellido(apellido);
            us.setPassword(password);
            //us.setPassword2(password2);
            us.setEmail(mail);
            us.setRegion(region);
            us.setCiudad(ciudad);
            us.setComuna(comuna);
            us.setDireccion(direccion);

            JsonHandler jh = new JsonHandler();
            JSONObject jObject = jh.setUsuario(us);

            new RegistrarGet(this,us).execute(getResources().getString(R.string.servidor) + "Usuario");
        }else{
            Toast.makeText(this, getResources().getString(R.string.error_internet), Toast.LENGTH_LONG).show();
        }

    }

    public void error(){
        Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG);
    }

    public void usuarioEnUso(){
        onSignupFailed();
        _usuarioText.setError("El usuario no se encuentra disponible.");
    }

    public void mailEnUso(){
        onSignupFailed();
        _mailText.setError("El email no se encuentra disponible.");
    }

    public void loguear(Usuario usuario){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Usuario", usuario);//getIntent().getSerializableExtra("MyClass"); para obtener objeto
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cerrarProgressDialog();
        startActivity(intent);
        //finish();
    }

    public void enviarJsonYLoguear(Usuario usuario){
        JsonHandler jh = new JsonHandler();
        JSONObject json = jh.setUsuario(usuario);
        new RegistrarPost(this, usuario).execute(getResources().getString(R.string.servidor) + "Usuario/crear", json.toString());
    }

    public void abrirProgressDialog(){
        _signupButton.setEnabled(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();
    }

    public void cerrarProgressDialog(){
        progressDialog.dismiss();
        _signupButton.setEnabled(true);
    }

    private void camposInvalidos(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.campos_invalidos), Toast.LENGTH_LONG);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Fallo al crear la cuenta", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validarCampos(){
        boolean valid = true;

        _usuarioText.setError(null);
        _passwordText.setError(null);
        _mailText.setError(null);
        _nombreText.setError(null);
        _apellidoText.setError(null);
        _direccionText.setError(null);

        if(usuario.isEmpty() || usuario.length() < largoMinUsuario || usuario.length() > largoMaxUsuario ) {
            _usuarioText.setError("Usuario entre " + largoMinUsuario + " y " + largoMaxUsuario + " carácteres alfanuḿericos");
            valid = false;
        } else {
            _usuarioText.setError(null);
        }

        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            _mailText.setError("Ingresar un email válido");
            valid = false;
        } else {
            _mailText.setError(null);
        }

        if (password.isEmpty() || password.length() < largoMinPassword || password.length() > largoMaxPassword) {
            _passwordText.setError("Contraseña entre " + largoMinPassword + " y " + largoMaxPassword + " carácteres alfanuméricos");
            valid = false;
        }
        else{
            if(!password.equals(password2)){
                _passwordText.setError("Las contraseñas no coinciden");
                valid = false;
            }
            else{
                _passwordText.setError(null);
                _password2Text.setError(null);
            }
        }

        if (nombre.isEmpty() || nombre.length() < largoMinNombre || nombre.length() > largoMaxNombre) {
            _nombreText.setError("Nombre entre " + largoMinNombre + " y " + largoMaxNombre + " carácteres.");
            valid = false;
        }
        else{
            _nombreText.setError(null);
        }

        if (apellido.isEmpty() || apellido.length() < largoMinApellido || apellido.length() > largoMaxApellido) {
            _apellidoText.setError("Apellido entre " + largoMinApellido + " y " + largoMaxApellido + " carácteres.");
            valid = false;
        }
        else{
            _apellidoText.setError(null);
        }

        if(direccion.isEmpty()){
            _direccionText.setError("Ingrese una dirección válida.");
            valid = false;
        }
        else{
            _direccionText.setError(null);
        }

        return valid;
    }

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
    }

}
