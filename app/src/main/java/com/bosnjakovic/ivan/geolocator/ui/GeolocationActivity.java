package com.bosnjakovic.ivan.geolocator.ui;

import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bosnjakovic.ivan.geolocator.R;
import com.bosnjakovic.ivan.geolocator.utility.Utilities;
import com.google.android.gms.maps.model.LatLng;

import java.util.Scanner;

public class GeolocationActivity extends AppCompatActivity {

    private EditText mEtSingleAddressEntry;
    private Button mBSingleAddress;
    private Button mBMultipleAddress;
    private TextView mTvResults;

    private Geocoder mGeocoder;

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
                scanner.useDelimiter(",") ;
                String locationName = scanner.next();
                String locationAdress = scanner.next();
                String locationZipCode = scanner.next();

              /*  Toast.makeText(GeolocationActivity.this, "Location name: " + locationName +
                        "\nLocation address: " + locationAdress +
                        "\nLocation zip code: " + locationZipCode
                        , Toast.LENGTH_SHORT).show();*/

                scanner.close();

                LatLng addressLatLng = Utilities.getLatLngFromSingleAddress(GeolocationActivity.this,locationName,locationAdress,locationZipCode);
                double adressLat = addressLatLng.latitude;
                double addressLng = addressLatLng.longitude;

                Toast.makeText(GeolocationActivity.this, "Latitude: " +adressLat + "\nlongitude: " + addressLng, Toast.LENGTH_SHORT).show();
            }
        });

        mBMultipleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
