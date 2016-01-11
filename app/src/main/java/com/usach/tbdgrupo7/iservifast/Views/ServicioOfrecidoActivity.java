package com.usach.tbdgrupo7.iservifast.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.R;

public class ServicioOfrecidoActivity extends AppCompatActivity {

    private Oferta servicio;
    private TextView titulo;
    private TextView precio;
    private TextView categoria;
    private TextView nombre_vendedor;
    private TextView usuario_vendedor;
    private TextView region_vendedor;
    private TextView descripcion;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_servicio_ofrecido);

       servicio = (Oferta) (getIntent().getSerializableExtra("oferta"));

       titulo = (TextView) findViewById(R.id.titulo_servicio);
       precio = (TextView) findViewById(R.id.precio_servicio);
       categoria = (TextView) findViewById(R.id.categoria_servicio);
       //nombre_vendedor = (TextView) findViewById(R.id.nombre_vendedor);
       usuario_vendedor = (TextView) findViewById(R.id.usuario_vendedor);
       region_vendedor = (TextView) findViewById(R.id.region_vendedor);
       descripcion = (TextView) findViewById(R.id.descripcion_servicio);

       titulo.setText(servicio.getTitulo());
       precio.setText(Integer.toString(servicio.getPrecio()));
       categoria.setText(servicio.getCategoria());
       usuario_vendedor.setText(servicio.getUsuario());
       region_vendedor.setText(servicio.getRegion());
       descripcion.setText(servicio.getDescripcion());
       //llenarServicio();
    }

    private void llenarServicio(){
        titulo.setText(servicio.getTitulo());
        precio.setText(servicio.getPrecio());
        categoria.setText(servicio.getCategoria_idCategoria());
        nombre_vendedor.setText(servicio.getUsuario_idUsuario());
        region_vendedor.setText(servicio.getComunidad_idComunidad());
        descripcion.setText(servicio.getDescripcion());
    }
}
