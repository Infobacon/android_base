package com.usach.tbdgrupo7.iservifast.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Views.MainActivity;
import com.usach.tbdgrupo7.iservifast.Views.MisServiciosOfrecidosActivity;
import com.usach.tbdgrupo7.iservifast.Views.ServiciosSolicitadosActivity;

import java.io.InputStream;

/**
 * Created by matias on 13-01-16.
 */
public class DescargarImagen4 extends AsyncTask<String, Void, Bitmap> {

    private int position;
    private MisServiciosOfrecidosActivity misServiciosOfrecidosActivity;

    public DescargarImagen4(MisServiciosOfrecidosActivity misServiciosOfrecidosActivity, int position) {
        this.misServiciosOfrecidosActivity=misServiciosOfrecidosActivity;
        this.position=position;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(int position, Bitmap result) {
        if (result != null) {
            misServiciosOfrecidosActivity.llegoImagen(position,result);
        }
    }
}