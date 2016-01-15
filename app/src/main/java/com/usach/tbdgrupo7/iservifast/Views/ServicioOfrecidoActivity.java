package com.usach.tbdgrupo7.iservifast.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.Favoritos.FavoritoGetInicio;
import com.usach.tbdgrupo7.iservifast.Controllers.Favoritos.FavoritoPost;
import com.usach.tbdgrupo7.iservifast.Model.Favorito;
import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.utilities.JsonHandler;
import com.usach.tbdgrupo7.iservifast.utilities.SystemUtilities;

import org.json.JSONObject;

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
    ImageView img;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_servicio_ofrecido);

       img = (ImageView) findViewById(R.id.icono_favorito);
       img.setImageResource(R.drawable.off);

       //new DescargarImagen(img).execute("https://estudiarfisica.files.wordpress.com/2008/11/leon.jpg");
       new FavoritoGetInicio(this).execute(getResources().getString(R.string.servidor)+"Favoritos");

       servicio = (OfertaGet) (getIntent().getSerializableExtra("oferta"));
       user = (Usuario) (getIntent().getSerializableExtra("usuario"));

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

    private void llenarServicio() {
        titulo.setText(servicio.getTitulo());
        precio.setText(servicio.getPrecio());
        categoria.setText(servicio.getCategoria());
        usuario_vendedor.setText(servicio.getUsuario());
        region_vendedor.setText(servicio.getComunidad());
        descripcion.setText(servicio.getDescripcion());
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
            System.out.println(jObject.toString());
            new FavoritoPost(ServicioOfrecidoActivity.this).execute(getResources().getString(R.string.servidor) + "Favoritos/crear", jObject.toString());
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
}
