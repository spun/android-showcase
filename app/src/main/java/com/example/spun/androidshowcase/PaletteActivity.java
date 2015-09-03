package com.example.spun.androidshowcase;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;

public class PaletteActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_OPEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
    }


    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Solo el sistema recibe ACTION_OPEN_DOCUMENT, no necesita comprobación
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {

            Uri fullPhotoUri = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.palette_imageView);

            try {
                // Generamos un Bitmap de la imagen escalado a la vista
                Bitmap scaledBitmap = getScaledBitmapFromURI(fullPhotoUri, imageView);
                imageView.setImageBitmap(scaledBitmap);

                runPalette(scaledBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Escala un Bitmap para que se ajuste al tamaño de la vista proporcionada
    private Bitmap getScaledBitmapFromURI(Uri fullPhotoUri, ImageView imageView) throws FileNotFoundException {
        // Recoge las dimensiones de la vista
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Recoge las dimensiones del bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(fullPhotoUri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determina el factor de escalado de la imagen
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decodifica la imagen a un Bitmap con el tamaño adecuado para la vista
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(getContentResolver().openInputStream(fullPhotoUri), null, bmOptions);
    }

    // Recoge los colores destacados de un Bitmap y colorea con ellos algunos elementos de la interfaz
    private void runPalette(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (darkVibrant != null && vibrant != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setStatusBarColor(darkVibrant.getRgb());
                    }
                    setActionBarBackgroundColor(vibrant.getRgb());
                }

                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                if (lightVibrant != null) {
                    setBackgroundColor(lightVibrant.getRgb());
                }
            }
        });
    }

    // Cambia el color de la barra de estado
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int rgb) {
        getWindow().setStatusBarColor(rgb);
    }

    // Cambia el color de fondo de la ActioBar
    private void setActionBarBackgroundColor(int rgb) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(rgb));
        }
    }

    // Cambia el color del LinearLayout que contiene la imagen
    private void setBackgroundColor(int rgb) {
        LinearLayout screen = (LinearLayout) findViewById(R.id.palette_linearLayout);
        screen.setBackgroundColor(rgb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_palette, menu);
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


}
