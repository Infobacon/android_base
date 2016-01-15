package com.usach.tbdgrupo7.iservifast.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.usach.tbdgrupo7.iservifast.Model.OfertaGet;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;

public class ServicioSolicitadoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_servicio_solicitado);

        img = (ImageView) findViewById(R.id.icono_favorito);
        img.setImageResource(R.drawable.off);

        //new DescargarImagen(img).execute("https://estudiarfisica.files.wordpress.com/2008/11/leon.jpg");

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
    }

    private void llenarServicio() {
        titulo.setText(servicio.getTitulo());
        precio.setText(servicio.getPrecio());
        categoria.setText(servicio.getCategoria());
        usuario_vendedor.setText(servicio.getUsuario());
        region_vendedor.setText(servicio.getComunidad());
        descripcion.setText(servicio.getDescripcion());
    }

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
    }

}
