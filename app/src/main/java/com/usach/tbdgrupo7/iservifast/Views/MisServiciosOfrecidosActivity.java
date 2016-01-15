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
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.MisServiciosOfrecidosGet;
import com.usach.tbdgrupo7.iservifast.Model.Categoria;
import com.usach.tbdgrupo7.iservifast.Model.Comunidad;
import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.DescargarImagen4;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

public class MisServiciosOfrecidosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CustomListAdapter adapter;
    private ListView list;
    private ProgressDialog progressDialog;
    private String[] titulos;
    private String[] descripciones;
    private OfertaGet[] servicios;
    private Usuario user;
    private Categoria categorias[];
    private Comunidad comunidades[];
    private Bitmap imagenes[];

    Integer[] imgid={
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
            R.drawable.bmw_logo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_servicios_ofrecidos_1);

        user = (Usuario) (getIntent().getSerializableExtra("usuario"));
        comunidades = (Comunidad[]) (getIntent().getSerializableExtra("comunidades"));
        categorias = (Categoria[]) (getIntent().getSerializableExtra("categorias"));

        progressDialog = new ProgressDialog(MisServiciosOfrecidosActivity.this,R.style.AppTheme_Dark_Dialog);

        new MisServiciosOfrecidosGet(this).execute(getResources().getString(R.string.servidor) + "Oferta/users/"+user.getIdUsuario());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        list=(ListView)findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MisServiciosOfrecidosActivity.this, ServicioOfrecidoActivity.class);
                myIntent.putExtra("oferta", servicios[position]);
                startActivity(myIntent);
            }
        });
    }

    public void listarServicios(OfertaGet[] servicios) {
        this.servicios = servicios;
        titulos = crearArrayTitulo(servicios);
        descripciones = crearArrayDescripcion(servicios);
        int i,largo;
        largo=servicios.length;
        Bitmap imagen_blanco = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
        imagenes = new Bitmap[largo];
        for(i=0;i<largo;i++){
            imagenes[i]=imagen_blanco;
            new DescargarImagen4(this,i);
        }
        adapter = new CustomListAdapter(this, titulos, descripciones, imagenes);
        list.setAdapter(adapter);
    }

    public void llegoImagen(int position,Bitmap bitmap){
        imagenes[position] = bitmap;
        adapter.notifyDataSetChanged();
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
                    Intent intent = new Intent(MisServiciosOfrecidosActivity.this, MainActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.servicios_solicitados) {
                    Intent intent = new Intent(MisServiciosOfrecidosActivity.this, ServiciosSolicitadosActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario", user);
                    startActivity(intent);
                } else if (id == R.id.ofrecer_servicio){
                    Intent intent = new Intent(MisServiciosOfrecidosActivity.this,OfrecerActivity.class);
                    intent.putExtra("categorias",categorias);
                    intent.putExtra("comunidades",comunidades);
                    intent.putExtra("usuario",user);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_right_in, R.transition.slide_left_out);
                } else if (id == R.id.solicitar_servicio) {
                    Intent intent = new Intent(MisServiciosOfrecidosActivity.this,SolicitarActivity.class);
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
                Toast.makeText(MisServiciosOfrecidosActivity.this, "COMUNIDAD O CAT NULL", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(MisServiciosOfrecidosActivity.this, "No se puede realizar la actividad solicitada, compruebe su conexión a internet.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void error_internet(){
        Toast.makeText(MisServiciosOfrecidosActivity.this, getResources().getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
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
