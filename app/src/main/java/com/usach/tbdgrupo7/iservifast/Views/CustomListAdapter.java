package com.usach.tbdgrupo7.iservifast.Views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.usach.tbdgrupo7.iservifast.R;

/**
 * Created by matias on 31-12-15.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titulos;
    private final String[] descripciones;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] titulos, String[] descripciones , Integer[] imgid) {
        super(context, R.layout.mylist, titulos);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.titulos=titulos;
        this.descripciones=descripciones;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(titulos[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText(descripciones[position]);
        return rowView;
    };
}