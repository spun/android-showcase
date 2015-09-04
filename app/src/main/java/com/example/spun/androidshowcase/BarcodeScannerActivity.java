package com.example.spun.androidshowcase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarcodeScannerActivity extends AppCompatActivity {

    static final String CAMERA_PHOTO = "cameraPhoto";
    static final String SCAN_RESULT = "scanResult";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    TextView mBarcodeResultTextView;
    Bitmap mPhoto;
    String mScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        mImageView = (ImageView) findViewById(R.id.barcode_picture_imageview);
        mBarcodeResultTextView = (TextView) findViewById(R.id.barcode_result_textview);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restaura una imagen escaneada anteriormente
        mPhoto = savedInstanceState.getParcelable(CAMERA_PHOTO);
        mImageView.setImageBitmap(mPhoto);
        // Restaura el resultado de una imagen escaneada anteriormente
        mScanResult = savedInstanceState.getString(SCAN_RESULT);
        mBarcodeResultTextView.setText(mScanResult);
    }

    public void launchCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mPhoto = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(mPhoto);
            scanPhoto(mPhoto);
        }
    }

    private void scanPhoto(Bitmap photo) {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        if (!barcodeDetector.isOperational()) {
            mBarcodeResultTextView.setText("Barcode Scanner no operational");
        } else {
            Frame frame = new Frame.Builder().setBitmap(photo).build();
            SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

            mScanResult = String.valueOf(barcodes.size()) + " barcodes";
            for (int i = 0; i < barcodes.size(); i++) {
                Barcode thisCode = barcodes.valueAt(i);
                mScanResult = mScanResult + "\n\n" + thisCode.rawValue;
            }
            mBarcodeResultTextView.setText(mScanResult);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Guarda la imagen y el resultado de un escaneo
        outState.putParcelable(CAMERA_PHOTO, mPhoto);
        outState.putString(SCAN_RESULT, mScanResult);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_barcode_scanner, menu);
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
