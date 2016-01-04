package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.OfrecerGet;
import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CustomListAdapter adapter;
    ListView list;
    private ProgressDialog progressDialog;
    private String[] titulos;
    private String[] descripciones;
    private Button btn_ofrecer;
    private Button btn_solicitar;
    private Oferta[] servicios;

    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War"
    };

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
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);

        new OfrecerGet(this).execute(getResources().getString(R.string.servidor) + "Oferta");

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

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(MainActivity.this,ServicioOfrecidoActivity.class);
                myIntent.putExtra("oferta",servicios[position]);
                startActivity(myIntent);

            }
        });

        btn_ofrecer = (Button) findViewById(R.id.btn_ofrecer);
        btn_ofrecer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SystemUtilities su = new SystemUtilities(getApplicationContext());
                if (su.isNetworkAvailable()) {
                    //IntentFilter intentFilter = new IntentFilter("httpData");
                    //new Ofrecer(getApplicationContext()).execute(getResources().getString(R.string.servidor)+"Categoria");;
                    Intent myIntent = new Intent(MainActivity.this,OfrecerActivity.class);
                    //myIntent.putExtra("id_usuario",id_usuario);
                    startActivity(myIntent);

                }
            }
        });

        btn_solicitar = (Button) findViewById(R.id.btn_solicitar);
        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SystemUtilities su = new SystemUtilities(getApplicationContext());
                if (su.isNetworkAvailable()) {
                    //IntentFilter intentFilter = new IntentFilter("httpData");
                    //new Ofrecer(getApplicationContext()).execute(getResources().getString(R.string.servidor)+"Categoria");;
                    Intent myIntent = new Intent(MainActivity.this,SolicitarActivity.class);
                    //myIntent.putExtra("id_usuario",id_usuario);
                    startActivity(myIntent);

                }
            }
        });
    }

    public void listarServicios(Oferta[] servicios){
        this.servicios = servicios;
        titulos = crearArrayTitulo(servicios);
        descripciones = crearArrayDescripcion(servicios);
        adapter = new CustomListAdapter(this, titulos, descripciones, imgid);
        list.setAdapter(adapter);
    }

    private String[] crearArrayTitulo(Oferta[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getTitulo();
        }
        return aux;
    }

    private String[] crearArrayDescripcion(Oferta[] servicios){
        int i;
        int largo = servicios.length;
        String[] aux = new String[largo];
        for(i=0;i<largo;i++){
            aux[i] = servicios[i].getDescripcion();
        }
        return aux;
    }

    public void abrirProgressDialog(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();
    }

    public void cerrarProgressDialog(){
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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


        if (id == R.id.servicios_ofrecidos) {
            Toast.makeText(getApplicationContext(),"Servicios ofrecidos mensaje",Toast.LENGTH_LONG).show();
        } else if (id == R.id.servicios_solicitados) {

            /*
            setContentView(R.layout.fragment_solicitar);
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_solicitar, new SolicitarFragment());
            transaction.commit();
*/
            Intent intent = new Intent(getApplicationContext(), OfrecerActivity.class);
            startActivityForResult(intent, 0);


            Toast.makeText(getApplicationContext(),"mensaje",Toast.LENGTH_LONG).show();
/*
            f1 = new SolicitarFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ofrecer_servicio, f1);
            ft.replace(R.id.ofrecer_servicio,)
            ft.commit();
*/
        } else if (id == R.id.ofrecer_servicio) {

            Intent intent = new Intent(getApplicationContext(), OfrecerActivity.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.solicitar_servicio){

            /*
            Fragment fragment = new SolicitarFragment();
            String tag = fragment.toString();

            FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
            fragmentManager.beginTransaction()
                    .hide(getFragmentManager().findFragmentById(R.id.fragment_main))
                    .add(fragment,fragment.toString())
                    .addToBackStack(tag)
                    .commit();
            */

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}