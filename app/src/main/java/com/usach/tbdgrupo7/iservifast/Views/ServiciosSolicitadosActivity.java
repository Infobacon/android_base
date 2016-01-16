package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.Gets.SolicitarGet;
import com.usach.tbdgrupo7.iservifast.Model.Categoria;
import com.usach.tbdgrupo7.iservifast.Model.Comunidad;
import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.DescargarImagen;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

import java.io.ByteArrayOutputStream;

public class ServiciosSolicitadosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CustomListAdapter adapter;
    private ListView list;
    private String[] titulos;
    private String[] descripciones;
    private OfertaGet[] servicios;
    private Usuario user;
    private Categoria categorias[];
    private Comunidad comunidades[];
    private Favorito favoritos[];
    private int gets;
    private Bitmap imagenes[];
    private ProgressDialog progressDialogDescargando;
    private Bitmap imagen_blanco;

    private static final short MAIN_ACTIVITY = 1;
    private static final short SERVICIOS_SOLICITADOS = 2;
    private static final short MIS_SERVICIOS_OFRECIDOS = 3;
    private static final short MIS_SERVICIOS_SOLICITADOS = 4;
    private static final short FAVORITOS = 5;
    private static final short SERVICIO_OFRECIDO = 6;
    private static final short SERVICIO_SOLICITADO = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_solicitados);
        progressDialogDescargando = new ProgressDialog(ServiciosSolicitadosActivity.this,R.style.AppTheme_Dark_Dialog);
        abrirProgressDialogDescargando();

        gets = 0;
        SystemUtilities su = new SystemUtilities(this);
        if (su.isNetworkAvailable()) {
            new SolicitarGet(this,SERVICIOS_SOLICITADOS).execute(getResources().getString(R.string.servidor) + "Solicitud");
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
        }

        user = (Usuario) (getIntent().getSerializableExtra("usuario"));
        comunidades = (Comunidad[]) (getIntent().getSerializableExtra("comunidades"));
        categorias = (Categoria[]) (getIntent().getSerializableExtra("categorias"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list=(ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imagenes[position] != imagen_blanco) {
                    Bitmap b = imagenes[position];
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    servicios[position].setImagen_comprimida(bs.toByteArray());
                    servicios[position].setImagen(null);
                } else {
                    servicios[position].setImagen_comprimida(null);
                }
                Intent i = new Intent(getApplicationContext(), ServicioOfrecidoActivity.class);
                i.putExtra("oferta", servicios[position]);
                i.putExtra("usuario", user);
                startActivity(i);
                overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
            }
        });

        setNombreEmail();

    }

    private void setNombreEmail(){
        TextView tv_nombre = (TextView) findViewById(R.id.nav_nombre_usuario);
        tv_nombre.setText(user.getNombre() + " " + user.getApellido());
        TextView tv_email = (TextView) findViewById(R.id.nav_email_usuario);
        tv_email.setText(user.getEmail());
    }

    public void llegoImagen(int position,Bitmap bitmap, String result){
        if(result.equals("OK")){
            imagenes[position] = bitmap;
            servicios[position].setImagen(bitmap);
            adapter.notifyDataSetChanged();
        }
    }

    public void listarServicios(OfertaGet[] serviciosOfrecidos){
        this.servicios = serviciosOfrecidos;
        titulos = crearArrayTitulo(serviciosOfrecidos);
        descripciones = crearArrayDescripcion(serviciosOfrecidos);
        int i;
        imagen_blanco = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
        imagenes = new Bitmap[serviciosOfrecidos.length];
        for(i=0;i<serviciosOfrecidos.length;i++){
            imagenes[i]=imagen_blanco;
            if(serviciosOfrecidos[i].getUrl().equals("no_image")==false){
                SystemUtilities su = new SystemUtilities(this);
                if (su.isNetworkAvailable()) {
                    new DescargarImagen(this,i,SERVICIOS_SOLICITADOS).execute(serviciosOfrecidos[i].getUrl());
                }
                else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
        adapter = new CustomListAdapter(this, titulos, descripciones, imagenes);
        list.setAdapter(adapter);
        cerrarProgressDialogDescargando();
    }

    public void cerrarProgressDialogDescargando(){
        progressDialogDescargando.dismiss();
    }

    public void error_internet(){
        Toast.makeText(this, getResources().getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
        cerrarProgressDialogDescargando();
    }

    private void cerrarDialogo(){
        gets++;
        if(gets==3){
            cerrarProgressDialogDescargando();
        }
    }

    private String[] crearArrayTitulo(OfertaGet[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getTitulo();
        }
        return aux;
    }

    private String[] crearArrayDescripcion(OfertaGet[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getDescripcion();
        }
        return aux;
    }

    public void getFavoritos(Favorito favoritos[]){
        if(favoritos!=null) {
            this.favoritos = favoritos;
        }
    }

    public void getCategorias(Categoria cats[]){
        this.categorias = new Categoria[cats.length];
        this.categorias = cats;
    }

    public void getComunidades(Comunidad coms[]){
        comunidades = new Comunidad[coms.length];
        comunidades = coms;
    }

    public void abrirProgressDialogDescargando(){
        progressDialogDescargando.setIndeterminate(true);
        progressDialogDescargando.setMessage("Descargando datos, espere por favor...");
        progressDialogDescargando.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SystemUtilities su = new SystemUtilities(getApplicationContext());
        if (su.isNetworkAvailable()) {
            if(categorias!=null&&comunidades!=null){
                if (id == R.id.servicios_ofrecidos) {
                    Intent intent = new Intent(ServiciosSolicitadosActivity.this, MainActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.servicios_solicitados) {
                    Intent intent = new Intent(ServiciosSolicitadosActivity.this, ServiciosSolicitadosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.ofrecer_servicio){
                    Intent intent = new Intent(ServiciosSolicitadosActivity.this,OfrecerActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario",user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.solicitar_servicio) {
                    Intent intent = new Intent(ServiciosSolicitadosActivity.this,SolicitarActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario",user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_ofrecidos){
                    Intent intent = new Intent(getApplicationContext(), MisServiciosOfrecidosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_solicitados){
                    Intent intent = new Intent(getApplicationContext(), MisServiciosSolicitados.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.mis_servicios_favoritos_ofrecidos){
                    Intent intent = new Intent(getApplicationContext(), FavoritosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in,R.transition.slide_left_out);
                }
            }
            else{
                Toast.makeText(ServiciosSolicitadosActivity.this, "COMUNIDAD O CAT NULL", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(ServiciosSolicitadosActivity.this, "No se puede realizar la actividad solicitada, compruebe su conexión a internet.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
            overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
        }
    }

}