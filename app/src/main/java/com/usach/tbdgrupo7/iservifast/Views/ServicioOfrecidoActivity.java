package com.usach.tbdgrupo7.iservifast.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.Gets.FavoritosGet;
import com.usach.tbdgrupo7.iservifast.Controllers.Posts.FavoritoPost;
import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.DescargarImagen;
import com.usach.tbdgrupo7.iservifast.utilities.JsonHandler;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class ServicioOfrecidoActivity extends AppCompatActivity {

    private OfertaGet servicio;
    private Usuario user;
    private TextView titulo;
    private TextView precio;
    private TextView categoria;
    private TextView nombre_vendedor;
    private TextView usuario_vendedor;
    private TextView region_vendedor;
    private TextView descripcion;
    private boolean estado_favorito = false;
    private boolean imagen_establecida = false;
    ImageView img;
    ImageView imagen_servicio;

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
       setContentView(R.layout.activity_servicio_ofrecido);

       img = (ImageView) findViewById(R.id.icono_favorito);
       img.setImageResource(R.drawable.off);

       SystemUtilities su = new SystemUtilities(getApplicationContext());
       if (su.isNetworkAvailable()) {
           new FavoritosGet(this,SERVICIO_OFRECIDO).execute(getResources().getString(R.string.servidor) + "Favoritos");
       }
       else{
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
       }

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_servicio);
       setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);

       //Para fragments
       //((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       //((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

       servicio = (OfertaGet) (getIntent().getSerializableExtra("oferta"));
       user = (Usuario) (getIntent().getSerializableExtra("usuario"));

       imagen_servicio = (ImageView) findViewById(R.id.imagen_servicio);

       if(servicio.getImagen_comprimida()!=null) {
           ImageView previewThumbnail = new ImageView(this);
           byte[] imagen_bytes =  servicio.getImagen_comprimida();
           Bitmap b = BitmapFactory.decodeByteArray(imagen_bytes, 0,imagen_bytes.length);
           previewThumbnail.setImageBitmap(b);
           servicio.setImagen(b);
           imagen_servicio.setImageBitmap(b);
           imagen_establecida = true;
       }
       else{
           imagen_establecida = false;
       }

       if(imagen_establecida==false&&servicio.getUrl().equals("no_image")==false){
           if (su.isNetworkAvailable()) {
               new DescargarImagen(this,SERVICIO_OFRECIDO).execute(servicio.getUrl());
           }
           else{
               Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
           }
       }

       titulo = (TextView) findViewById(R.id.titulo_servicio);
       precio = (TextView) findViewById(R.id.precio_servicio);
       categoria = (TextView) findViewById(R.id.categoria_servicio);
       //nombre_vendedor = (TextView) findViewById(R.id.nombre_vendedor);
       usuario_vendedor = (TextView) findViewById(R.id.usuario_vendedor);
       region_vendedor = (TextView) findViewById(R.id.region_vendedor);
       descripcion = (TextView) findViewById(R.id.descripcion_servicio);

       llenarServicio();

       img.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
               SystemUtilities su = new SystemUtilities(getApplicationContext());
               if (su.isNetworkAvailable()) {
                   favoritoPresionado();
               }
               else{
                   Toast.makeText(ServicioOfrecidoActivity.this, getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void llegoImagen(Bitmap bitmap, String result){
       if(result.equals("OK")){
            imagen_servicio.setImageBitmap(bitmap);
        }
    }

    private void llenarServicio() {
        titulo.setText(servicio.getTitulo());
        precio.setText(transformarPrecio(servicio.getPrecio()));
        categoria.setText(servicio.getCategoria());
        usuario_vendedor.setText(servicio.getUsuario());
        region_vendedor.setText(servicio.getComunidad());
        descripcion.setText(servicio.getDescripcion());
    }

    public String transformarPrecio(String precio) {
        int i;
        int largo = precio.length();
        String aux = "";
        if(largo>3){
            DecimalFormat formatter = new DecimalFormat("#,###,###,###,###");
            precio = formatter.format(Integer.parseInt(precio));
            largo = precio.length();
            for(i=0;i<largo;i++){
                if(precio.charAt(i)==','){
                    aux = aux + '.';
                }
                else{
                    aux = aux + precio.charAt(i);
                }
            }
            return aux;

        }
        else{
            return precio;
        }
    }

    public void error_internet(){
        Toast.makeText(ServicioOfrecidoActivity.this, getResources().getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
    }

    public void favoritoPresionado(){
        if(estado_favorito==true){
            //borrar de favorito
        }
        else{
            Favorito a = new Favorito();
            a.setUsuario_idUsuario(user.getIdUsuario());
            a.setServicio_idServicio(servicio.getIdServicio());

            JsonHandler jh = new JsonHandler();
            JSONObject jObject = jh.setFavorito(a);

            SystemUtilities su = new SystemUtilities(getApplicationContext());
            if (su.isNetworkAvailable()) {
                new FavoritoPost(ServicioOfrecidoActivity.this).execute(getResources().getString(R.string.servidor) + "Favoritos/crear", jObject.toString());
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setearFavorito(Favorito favoritos[]){
        int i;
        int largo = favoritos.length;
        for(i=0;i<largo;i++){
            if(favoritos[i].getServicio_idServicio()==servicio.getIdServicio()){
                if(user.getIdUsuario()==favoritos[i].getUsuario_idUsuario()){
                    estado_favorito = true;
                    img.setImageResource(R.drawable.on);
                    break;
                }
            }
        }
    }

    public void favoritoCambiado(String resultado){
        if(estado_favorito==true){
            estado_favorito=false;
            img.setImageResource(R.drawable.off);
        }

        else{
            estado_favorito=true;
            img.setImageResource(R.drawable.on);
        }
    }

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
    }

}