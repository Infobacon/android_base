package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.usach.tbdgrupo7.iservifast.Controllers.FavoritosGet;
import com.usach.tbdgrupo7.iservifast.Controllers.OfrecerGet2;
import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;

public class FavoritosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CustomListAdapter adapter;
    private ListView list;
    private ProgressDialog progressDialog;
    private String[] titulos;
    private String[] descripciones;
    private Oferta[] servicios;
    private Usuario user;


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
        setContentView(R.layout.activity_favoritos);

        user = (Usuario) (getIntent().getSerializableExtra("usuario"));

        System.out.println(user.getIdUsuario());
        progressDialog = new ProgressDialog(FavoritosActivity.this,R.style.AppTheme_Dark_Dialog);
        new FavoritosGet(this).execute(getResources().getString(R.string.servidor) + "Favoritos/users/" + user.getIdUsuario());

        list=(ListView)findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(FavoritosActivity.this, ServicioOfrecidoActivity.class);
                myIntent.putExtra("oferta", servicios[position]);
                startActivity(myIntent);

            }
        });


    }

    public void getServicios(Favorito favoritos[]){
        new OfrecerGet2(this).execute(getResources().getString(R.string.servidor) + "Oferta");
    }

    public void listarServicios(Oferta[] servicios) {
        int i;
        int j = 0;
        int idUsuario = user.getIdUsuario();
        for (i = 0; i < servicios.length; i++) {
            if (servicios[i].getUsuario_idUsuario() == idUsuario) {
                servicios[j] = servicios[i];
                j++;
            }
        }
        if (j != 0) {
            Oferta[] servs = new Oferta[j];
            for(i=0;i<j;i++){
                servs[i] = servicios[i];
            }
            this.servicios = servs;
            titulos = crearArrayTitulo(servs);
            descripciones = crearArrayDescripcion(servs);
            adapter = new CustomListAdapter(this, titulos, descripciones, imgid);
            list.setAdapter(adapter);
        }
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }
}
