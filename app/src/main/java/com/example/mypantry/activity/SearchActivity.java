package com.example.mypantry.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mypantry.R;
import com.example.mypantry.Utils;
import com.example.mypantry.connection.AuthToken;
import com.example.mypantry.connection.LoadProduct;
import com.example.mypantry.connection.ProductRequest;
import com.example.mypantry.connection.VoteProduct;
import com.example.mypantry.data.ui.fragment.HomeFragment;
import com.example.mypantry.data.ui.login.LoginActivity;
import com.example.mypantry.item.ListItem;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity{

    public static JSONArray listProduct;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    protected TextView barcodeText;
    private String barcodeData;
    private ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode);
        loadingProgressBar = findViewById(R.id.loadingSearch);

        Button btn = findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listProduct = null;
                String barcode = getBarcode();
                if(AuthToken.isNull()){
                    loginIntent();
                }else if(!barcode.isEmpty()){
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    ProductRequest.requestList(barcode);
                    waitListProduct();
                    try {
                        onCreateChooseDialog(v).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}}
        });
    }

    private void initialiseDetectorsAndSources() {
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
                    if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        surfaceView.setVisibility(View.INVISIBLE);
                        ActivityCompat.requestPermissions(SearchActivity.this, new
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
//Dialog -> Scelta----------------------------------------------------------------------------------
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
                            LoadProduct.addProduct(name.getText().toString(), description.getText().toString(), getBarcode());
                            homeIntent();
                        } catch (JSONException e) {e.printStackTrace(); }
                        onStart();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
        return builder.create();
    }

//Dialog -> Nuovo Prodotto -------------------------------------------------------------------------

    public Dialog onCreateChooseDialog(View v) throws JSONException {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleziona un elemento")
                    .setItems(Utils.getCharSequence(listProduct), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                Object obj = listProduct.get(which);
                                String name =  Utils.getData(obj, "name");
                                String description = Utils.getData(obj, "description");
                                String barcode = Utils.getData(obj, "barcode");
                                String id = Utils.getData(obj,"id");
                                VoteProduct.voteProduct(id);
                                HomeFragment.db.save( name,barcode, description,1,id);
                                homeIntent();
                            } catch (JSONException e) {e.printStackTrace(); }
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadingProgressBar.setVisibility(View.INVISIBLE);}
                    }).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onCreateDialog(v).show();

                            loadingProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
            return builder.create();
        }

        private void waitListProduct(){
            while(listProduct==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
        }

        private void loginIntent(){
            Intent intent = new Intent();
            ComponentName component = new ComponentName(getApplicationContext(), LoginActivity.class);
            intent.setComponent(component);
            startActivity(intent);
        }

        private void homeIntent(){
            Intent intent = new Intent();
            ComponentName component =
                    new ComponentName(getApplicationContext(), MainActivity.class);
            intent.setComponent(component);
            startActivity(intent);
        }
}
