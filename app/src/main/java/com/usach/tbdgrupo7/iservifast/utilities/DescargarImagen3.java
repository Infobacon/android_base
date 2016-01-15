package com.usach.tbdgrupo7.iservifast.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.usach.tbdgrupo7.iservifast.Views.FavoritosActivity;

import java.io.InputStream;

/**
 * Created by matias on 13-01-16.
 */
public class DescargarImagen3 extends AsyncTask<String, Void, Bitmap> {

    private int position;
    private FavoritosActivity favoritosActivity;

    public DescargarImagen3(FavoritosActivity favoritosActivity, int position) {
        this.favoritosActivity=favoritosActivity;
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
            favoritosActivity.llegoImagen(position,result);
        }
    }
}