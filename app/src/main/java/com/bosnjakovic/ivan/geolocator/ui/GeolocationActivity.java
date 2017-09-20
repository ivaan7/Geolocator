package com.bosnjakovic.ivan.geolocator.ui;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bosnjakovic.ivan.geolocator.service.GeolocationExtractionService;
import com.bosnjakovic.ivan.geolocator.model.Address;
import com.bosnjakovic.ivan.geolocator.R;
import com.bosnjakovic.ivan.geolocator.utility.Utilities;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment;

import static android.R.color.background_light;

public class GeolocationActivity extends AppCompatActivity {

    private ScrollView svText;
    public static final String TV_ARRAY_EXTRA = "com.bosnjakovic.ivan.geolocator.tv.array.extra";
    public static final String FILE_ARRAY_EXTRA = "com.bosnjakovic.ivan.geolocator.file.array.extra";
    private EditText mEtSingleAddressEntry;
    private Button mBSingleAddress;
    private Button mBMultipleAddress;
    private Button mBSaveToFile;
    private TextView mTvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        initWidgets();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(GeolocationExtractionService.LOCATIONS_BROADCAST));
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void initWidgets() {

        mEtSingleAddressEntry = (EditText) findViewById(R.id.etLocationEntry);
        mBSingleAddress = (Button) findViewById(R.id.bSingleLocation);
        mBMultipleAddress = (Button) findViewById(R.id.bLocationFromCsv);
        mBSaveToFile = (Button) findViewById(R.id.bWriteToFile);
        mTvResults = (TextView) findViewById(R.id.tvResult);
        svText = (ScrollView) findViewById(R.id.svText);

    }

    private void setupListeners() {

        mBSingleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = mEtSingleAddressEntry.getText().toString();
                String[] address = location.split(",");

                String locationName = address[0];
                String locationAddress = address[1];
                String locationZipCode = address[2];

                LatLng addressLatLng = Utilities.getLatLngFromSingleAddress(GeolocationActivity.this,
                        locationAddress, locationZipCode);
                double addressLat = addressLatLng.latitude;
                double addressLng = addressLatLng.longitude;

                mTvResults.setText("Location name: " + locationName + "\n"
                        + "Location address: " + locationAddress + "\n"
                        + "Zip code: " + locationZipCode + "\n"
                        + "Latitude: " + addressLat + "\n"
                        + "Longitude: " + addressLng
                );
            }
        });

        mBMultipleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogConfig dialogConfig = setupDialogConfig();

                new FilePickerDialogFragment.Builder()
                        .configs(dialogConfig)
                        .onFilesSelected(new FilePickerDialogFragment.OnFilesSelectedListener() {
                            @Override
                            public void onFileSelected(List<File> list) {
                                for (File file : list) {
                                    InputStreamReader streamReader = null;
                                    try {
                                        streamReader = new InputStreamReader(new FileInputStream(file));
                                        BufferedReader reader = new BufferedReader(streamReader);
                                        String line = "";
                                        ArrayList<Address> adrese = new ArrayList<Address>();
                                        while ((line = reader.readLine()) != null) {

                                            String[] address = line.split(",");
                                            String storeName = address[0];
                                            String storeAddress = address[1];
                                            String zip = address[2];
                                            adrese.add(new Address(storeName, storeAddress, zip));
                                        }
                                        reader.close();
                                        Intent intent = new Intent(GeolocationActivity.this, GeolocationExtractionService.class);
                                        intent.putParcelableArrayListExtra(TV_ARRAY_EXTRA, adrese);
                                        startService(intent);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .build()
                        .show(getSupportFragmentManager(), null);
            }
        });
        mBSaveToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogConfig dialogConfig = setupDialogConfig();
                new FilePickerDialogFragment.Builder()
                        .configs(dialogConfig)
                        .onFilesSelected(new FilePickerDialogFragment.OnFilesSelectedListener() {
                            @Override
                            public void onFileSelected(List<File> list) {
                                for (File file : list) {
                                    InputStreamReader streamReader = null;
                                    try {
                                        streamReader = new InputStreamReader(new FileInputStream(file));
                                        BufferedReader reader = new BufferedReader(streamReader);
                                        String line = "";
                                        ArrayList<Address> adrese = new ArrayList<Address>();
                                        while ((line = reader.readLine()) != null) {

                                            String[] address = line.split(",");
                                            String storeName = address[0];
                                            String storeAddress = address[1];
                                            String zip = address[2];
                                            adrese.add(new Address(storeName, storeAddress, zip));
                                        }
                                        reader.close();
                                        Intent intent = new Intent(GeolocationActivity.this, GeolocationExtractionService.class);
                                        intent.putParcelableArrayListExtra(FILE_ARRAY_EXTRA, adrese);
                                        startService(intent);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .build()
                        .show(getSupportFragmentManager(), null);
            }
        });
        mTvResults.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mTvResults.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                String text = mTvResults.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(GeolocationActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @NonNull
    private DialogConfig setupDialogConfig() {
        return new DialogConfig.Builder()
                .enableMultipleSelect(true) // default is false
                .enableFolderSelect(true) // default is false
                .initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()) // default is sdcard
                .supportFiles(new SupportFile(".csv", 0), new SupportFile(".txt", 0), new SupportFile(".doc", 0),
                        new SupportFile(".docx", 0)) // default is showing all file types.
                .build();
    }

    // handler for received Intents for the event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            if (intent.hasExtra(GeolocationExtractionService.LOCATIONS_EXTRA)) {
                String message = intent.getStringExtra(GeolocationExtractionService.LOCATIONS_EXTRA);
                mTvResults.setText(message);
            } else if (intent.hasExtra(GeolocationExtractionService.SUCCES)) {
                String path = intent.getStringExtra(GeolocationExtractionService.SUCCES);
                Toast.makeText(GeolocationActivity.this, "Success!" + "\n +Location of convrted file is: " + path, Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(GeolocationExtractionService.UNSUCCES)) {
                String message = intent.getStringExtra(GeolocationExtractionService.UNSUCCES);
                Toast.makeText(GeolocationActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
