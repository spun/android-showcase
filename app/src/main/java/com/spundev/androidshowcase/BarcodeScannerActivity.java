package com.spundev.androidshowcase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScannerActivity extends AppCompatActivity {

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private TextView mBarcodeInfo;
    private static final int REQUEST_CAMERA = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the Camera permission is already avaible.
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Camera permission is already avaible, show the camera preview.
                showCameraPreview();
            } else {
                // Camera permission has not been granted.

                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Camera permission is needed to show the camera preview", Toast.LENGTH_LONG).show();
                }

                // Request Camera permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
        } else {
            showCameraPreview();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // Received permission result for camera permission.

            // Check if the only required permission has been granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                showCameraPreview();
            } else {
                // Camera permission was denied, so we cannot use this feature
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    void showCameraPreview() {
        mCameraView = (SurfaceView) findViewById(R.id.camera_view);
        mBarcodeInfo = (TextView) findViewById(R.id.code_info);

        mBarcodeDetector = new BarcodeDetector.Builder(this).build();

        mCameraSource = new CameraSource
                .Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(480, 640)
                .build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCameraSource.start(mCameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    mBarcodeInfo.post(new Runnable() {
                        public void run() {
                            mBarcodeInfo.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
