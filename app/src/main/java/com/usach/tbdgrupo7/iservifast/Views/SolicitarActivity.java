package com.usach.tbdgrupo7.iservifast.Views;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.usach.tbdgrupo7.iservifast.Controllers.SolicitarPost;
import com.usach.tbdgrupo7.iservifast.Model.Categoria;
import com.usach.tbdgrupo7.iservifast.Model.Comunidad;
import com.usach.tbdgrupo7.iservifast.Model.Oferta;
import com.usach.tbdgrupo7.iservifast.Model.Usuario;
import com.usach.tbdgrupo7.iservifast.R;
import com.usach.tbdgrupo7.iservifast.helpers.DocumentHelper;
import com.usach.tbdgrupo7.iservifast.helpers.IntentHelper;
import com.usach.tbdgrupo7.iservifast.imgurmodel.ImageResponse;
import com.usach.tbdgrupo7.iservifast.imgurmodel.Upload;
import com.usach.tbdgrupo7.iservifast.services.UploadService;
import com.usach.tbdgrupo7.iservifast.utilities.JsonHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SolicitarActivity extends AppCompatActivity {

    private Button btn_tomar_foto;
    private Button btn_seleccionar_foto;
    private Button btn_enviar;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri = null;
    private String mPath;
    private Spinner sp;
    private Spinner sp1;
    private ArrayList<String> categoriaItems=new ArrayList<>();
    private ArrayList<String> comunidadItems=new ArrayList<>();
    private BroadcastReceiver br = null;
    private Usuario user;
    private Categoria[] categorias;
    private Comunidad[] comunidades;
    private ProgressDialog progressDialog;

    private String titulo;
    private String descripcion;
    private String precio;
    private int idCat;

    public final static String TAG = OfrecerActivity.class.getSimpleName();

    /*
      These annotations are for ButterKnife by Jake Wharton
      https://github.com/JakeWharton/butterknife
     */
    @Bind(R.id.output_photo)
    ImageView uploadImage;
    @Bind(R.id.input_titulo)
    EditText uploadTitle;
    @Bind(R.id.input_descripcion)
    EditText uploadDesc;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar);

        user = (Usuario) (getIntent().getSerializableExtra("usuario"));
        categorias = (Categoria[]) (getIntent().getSerializableExtra("categorias"));
        comunidades = (Comunidad[]) (getIntent().getSerializableExtra("comunidades"));

        String[] cats = new String[categorias.length];
        String[] coms = new String[comunidades.length];

        progressDialog = new ProgressDialog(SolicitarActivity.this,R.style.AppTheme_Dark_Dialog);


        int i;
        for(i=0;i<cats.length;i++){
            categoriaItems.add(categorias[i].getNombre());
        }


        for(i=0;i<coms.length;i++){
            comunidadItems.add(comunidades[i].getNombre());
        }

        sp=(Spinner)findViewById(R.id.spinner_categorias);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoriaItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp1=(Spinner)findViewById(R.id.spinner_comunidades);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, comunidadItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        btn_tomar_foto = (Button) findViewById(R.id.btn_tomar_foto);
        btn_seleccionar_foto = (Button) findViewById(R.id.btn_seleccionar_foto);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);

        btn_seleccionar_foto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
            }
        });

        btn_tomar_foto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                } else {
                    Toast.makeText(getApplication(), "El celular no soporta cámara.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (chosenFile == null) return;
                createUpload(chosenFile);

                abrirProgressDialog();

                new UploadService(getApplicationContext()).Execute(upload, new UiCallback());

                IntentFilter intentFilter = new IntentFilter("httpPost");

                Spinner spinner_categorias = (Spinner) findViewById(R.id.spinner_categorias);
                String categoria = spinner_categorias.getSelectedItem().toString();

                //Spinner spinner_comunidad = (Spinner)findViewById(R.id.spinner_comunidades);
                //String comunidad = spinner_comunidad.getSelectedItem().toString();

                //int idCom = comunidadItems.indexOf(comunidad);

                titulo = ((EditText) findViewById(R.id.input_titulo)).getText().toString();
                descripcion = ((EditText) findViewById(R.id.input_descripcion)).getText().toString();
                idCat = (categoriaItems.indexOf(categoria));
                precio = ((EditText) findViewById(R.id.input_precio)).getText().toString();

            }
        });
    }

    public void iniciarMain(){
        cerrarProgressDialog();
        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
        intent1.putExtra("usuario",user);
        startActivity(intent1);
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mPath = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
        } else if(type == MEDIA_TYPE_VIDEO) {
            mPath = mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4";
        } else {
            return null;
        }
        mediaFile = new File(mPath);
        return mediaFile;
    }

    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[] { src });
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {

                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.output_photo);
                bitmap = rotateBitmap(mPath,bitmap);
                imageView.setImageBitmap(bitmap);


                String filePath = DocumentHelper.getPath(this, uri);
                //Safety check to prevent null pointer exception
                if (filePath == null || filePath.isEmpty()) return;
                chosenFile = new File(filePath);

                /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 100 && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(mPath, options);
            ImageView imageView = (ImageView) findViewById(R.id.output_photo);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void abrirProgressDialog(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Enviando datos, espere por favor...");
        progressDialog.show();
    }

    public void cerrarProgressDialog(){
        progressDialog.dismiss();
    }


    private Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void error_internet(){
        Toast.makeText(SolicitarActivity.this, getResources().getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.output_photo)
    public void onChooseImage() {
        uploadDesc.clearFocus();
        uploadTitle.clearFocus();
        IntentHelper.chooseFileIntent(this);
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = titulo;
        upload.description = descripcion;
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            //onsuccess image upload

            Oferta a = new Oferta();//duracion y promedio por defecto en constructor
            a.setTitulo(titulo);
            a.setDescripcion(descripcion);
            a.setCategoria_idCategoria(1);
            a.setComunidad_idComunidad(2);
            a.setPrecio(precio);
            a.setUsuario_idUsuario(user.getIdUsuario());
            a.setImagen(imageResponse.data.link);
            JsonHandler jh = new JsonHandler();
            JSONObject jObject = jh.setOferta(a);
            new SolicitarPost(SolicitarActivity.this).execute(getResources().getString(R.string.servidor) + "Solicitud/crear", jObject.toString());
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById(R.id.rootview), "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.transition.slide_left_in, R.transition.slide_right_out);
    }

}