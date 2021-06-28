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
import com.example.mypantry.ui.home.HomeFragment;
import com.example.mypantry.ui.login.LoginActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class ActivitySearch extends AppCompatActivity{

    public static JSONArray listProduct;
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
                String barcode = getBarcode();

                if(AuthToken.isNull()){
                    LoginIntent();
                }else if(!barcode.isEmpty()){
                    ProductRequest.requestList(barcode);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    waitListProduct();
                    loadingProgressBar.setVisibility(View.GONE);
                    try {
                        onCreateChooseDialog(v).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}}
        });

        /*Button btnScan = findViewById(R.id.scanButton);
        btnScan.setOnClickListener(v->{
            onCreateDialog(v).show();
        });*/
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
                            LoadProduct.addProduct(name.getText().toString(), description.getText().toString(), getBarcode());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        onStart();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    public Dialog onCreateChooseDialog(View v) throws JSONException {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleziona un elemento")
                    .setItems(Utils.getCharSequence(listProduct), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            try {
                                Object obj = listProduct.get(which);
                                String name =  Utils.getData(obj, "name");
                                String description = Utils.getData(obj, "description");
                                String barcode = Utils.getData(obj, "barcode");

                                Log.e("name", Utils.getData(obj, "name"));
                                Log.e("description", Utils.getData(obj, "description"));
                                Log.e("barcode", Utils.getData(obj, "barcode"));
                                HomeFragment.db.save( Utils.getData(obj, "name"),Utils.getData(obj, "barcode"), Utils.getData(obj, "description"),1,Utils.getData(obj,"productId"));

                               //  db.save("Patate","304323943025","Patate al forno ",1,"2974833240");
                                /*final String[] stars = {"1 Stella", "2 Stelle ","3 Stelle", "4 Stelle","5 Stelle"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setTitle("Valuta "+name )
                                        .setMessage("Valuta il tuo elemento assegnando un punteggio da 1 a 5");

                                builder.show();*/


                                Intent intent = new Intent();
                                ComponentName component =
                                        new ComponentName(getApplicationContext(), MainActivity.class);
                                intent.setComponent(component);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton(R.string.aggiungi, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCreateDialog(v).show();
                }
            });
            return builder.create();
        }
        private void LoginIntent(){
            Intent intent = new Intent();
            ComponentName component = new ComponentName(getApplicationContext(), LoginActivity.class);
            intent.setComponent(component);
            startActivity(intent);
        }
        private void waitListProduct(){
            while(listProduct==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
        }
}
