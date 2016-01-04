package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.Login;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final short REQUEST_SIGNUP = 0;
    private String user_text;
    private String pass_text;
    private final short largoMinUsuario = 6;
    private final short largoMaxUsuario = 12;
    private final short largoMinPassword = 6;
    private final short largoMaxPassword = 12;
    private ProgressDialog dialogAutentificando;
    private BroadcastReceiver br = null;

    @InjectView(R.id.input_user)EditText _userText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login)Button _loginButton;
    @InjectView(R.id.link_signup)TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        dialogAutentificando = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrarActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


        // Item Click Listener for the listview
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Getting the Container Layout of the ListView
                LinearLayout linearLayoutParent = (LinearLayout) container;

                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutChild.getChildAt(0);

                Toast.makeText(getBaseContext(), tvCountry.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void login() {

        user_text = _userText.getText().toString();
        pass_text = _passwordText.getText().toString();

        if (validarCampos()==false) {
            camposInvalidos();
            return;
        }

        SystemUtilities su = new SystemUtilities(this.getApplicationContext());
        if (su.isNetworkAvailable()) {
            new Login(this,user_text,pass_text).execute(getResources().getString(R.string.servidor) + "Usuario");
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_LONG);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(false);
        finish();
    }

    public void onLoginSuccess(Usuario user) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("usuario", user.getUsuario());
        /*intent.putExtra("idUsuario", user.getIdUsuario());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("nombre", user.getNombre());
        intent.putExtra("apellido", user.getApellido());
        intent.putExtra("mail", user.getEmail());
        intent.putExtra("region", user.getRegion());
        intent.putExtra("ciudad", user.getCiudad());
        intent.putExtra("comuna", user.getComuna());
        intent.putExtra("direccion", user.getDireccion());*/
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cerrarProgressDialog();
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Usuario y contraseña no coinciden", Toast.LENGTH_LONG).show();
        cerrarProgressDialog();
    }

    private void camposInvalidos(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.campos_invalidos), Toast.LENGTH_LONG);
    }

    public void abrirProgressDialog(){
        _loginButton.setEnabled(false);
        dialogAutentificando.setIndeterminate(true);
        dialogAutentificando.setMessage("Autentificando...");
        dialogAutentificando.show();
    }

    private void cerrarProgressDialog(){
        dialogAutentificando.dismiss();
        _loginButton.setEnabled(true);
    }

    public boolean validarCampos() {
        boolean valid = true;

        if(user_text.isEmpty() || user_text.length() < largoMinUsuario || user_text.length() > largoMaxUsuario ) {
            _userText.setError("Usuario entre "+largoMinUsuario+" y "+largoMaxUsuario + "carácteres alfanuḿericos");
            valid = false;
        } else {
            _userText.setError(null);
        }

        if (pass_text.isEmpty() || pass_text.length() < largoMinPassword || pass_text.length() > largoMaxPassword) {
            _passwordText.setError("Contraseña entre "+largoMinPassword+" y "+largoMaxPassword + " carácteres alfanuméricos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}