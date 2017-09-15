package com.bosnjakovic.ivan.geolocator.ui;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bosnjakovic.ivan.geolocator.Address;
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
import java.util.Scanner;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment;

public class GeolocationActivity extends AppCompatActivity {

    private EditText mEtSingleAddressEntry;
    private Button mBSingleAddress;
    private Button mBMultipleAddress;
    private TextView mTvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        initWidgets();
        setupListeners();
    }

    private void initWidgets() {

        mEtSingleAddressEntry = (EditText) findViewById(R.id.etLocationEntry);
        mBSingleAddress = (Button) findViewById(R.id.bSingleLocation);
        mBMultipleAddress = (Button) findViewById(R.id.bLocationFromCsv);
        mTvResults = (TextView) findViewById(R.id.tvResult);

    }

    private void setupListeners() {

        mBSingleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = mEtSingleAddressEntry.getText().toString();

                Scanner scanner = new Scanner(location);
                scanner.useDelimiter(",");
                String locationName = scanner.next();
                String locationAdress = scanner.next();
                String locationZipCode = scanner.next();

                scanner.close();

                LatLng addressLatLng = Utilities.getLatLngFromSingleAddress(GeolocationActivity.this, locationName, locationAdress, locationZipCode);
                double adressLat = addressLatLng.latitude;
                double addressLng = addressLatLng.longitude;

                Toast.makeText(GeolocationActivity.this, "Latitude: " + adressLat + "\nlongitude: " + addressLng, Toast.LENGTH_SHORT).show();
            }
        });

        mBMultipleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogConfig dialogConfig = new DialogConfig.Builder()
                        .enableMultipleSelect(true) // default is false
                        .enableFolderSelect(true) // default is false
                        .initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()) // default is sdcard
                        //  .supportFiles(new SupportFile(".3gpp", R.drawable.btn_select), new SupportFile(".mp3", 0), new SupportFile(".pdf", 0)) // default is showing all file types.
                        .build();

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
                                        List<Address> adrese = new ArrayList<Address>();
                                        while ((line = reader.readLine()) != null) {

                                            String[] address = line.split(",");
                                            String storeName = address[0];
                                            String storeAddress = address[1];
                                            String zip = address[2];
                                            adrese.add(new Address(storeName,storeAddress,zip));
                                        }

                                        mTvResults.setText(adrese.get(0).toString());
                                        reader.close();
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

    }
}
