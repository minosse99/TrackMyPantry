package com.example.mypantry.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypantry.R;
import com.example.mypantry.connection.AuthToken;
import com.example.mypantry.connection.ProductRequest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;

public class ActivitySearch extends AppCompatActivity{

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    protected TextView barcodeText;
    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode);
        //initialiseDetectorsAndSources();
        final ProgressBar loadingProgressBar = findViewById(R.id.loadingSearch);


        Button btn = findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String barcode = getBarcode();
                ProductRequest req = new ProductRequest();
                String a = String.valueOf(req.execute(barcode));
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        Button btnScan = findViewById(R.id.scanButton);
        btnScan.setOnClickListener(v->{
/*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("add a new product in a list")
                    .setTitle("Adding");
            Dialog dialog = new Dialog(this);

            builder.show();*/
            onCreateDialog(v).show();
        });
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ActivitySearch.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        surfaceView.setVisibility(View.INVISIBLE);
                        ActivityCompat.requestPermissions(ActivitySearch.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });
    }

    public String getBarcode(){
        EditText text = (EditText) findViewById(R.id.barcode);
        return text.getText().toString();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

        public Dialog onCreateDialog(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater

            final View customLayout = getLayoutInflater().inflate(R.layout.add_product_dialog, null);
            builder.setView(customLayout);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setIcon(R.drawable.trackmypantry)
                    // Add action buttons
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            EditText name = customLayout.findViewById(R.id.nameProduct);
                            EditText description = customLayout.findViewById(R.id.descriptionProduct);


                            Log.e("name", name.getText().toString());
                            try {
                                ProductRequest.addProduct(name.getText().toString(), description.getText().toString(), getBarcode());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Editable names = name.getEditableText();
                            //                  Log.e("name", name.getText().toString());
                            //EditText description = (EditText) findViewById(R.id.descriptionProduct);
                            //String descr = description.getText().toString();
                            //Log.e("description",describeProduct.getText().toString());
                            //MainActivity.saveElement("1234567",name.getText().toString(),description.getText().toString());
                            onStart();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
}