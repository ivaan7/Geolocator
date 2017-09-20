package com.bosnjakovic.ivan.geolocator.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.bosnjakovic.ivan.geolocator.model.Address;
import com.bosnjakovic.ivan.geolocator.ui.GeolocationActivity;
import com.bosnjakovic.ivan.geolocator.utility.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GeolocationExtractionService extends IntentService {

    public static final String SUCCES = "succes";
    public static final String UNSUCCES ="unsucces" ;

    public static final String LOCATIONS_BROADCAST = "com.bosnjakovic.ivan.geolocator.broadcast.locations";
    public static final String LOCATIONS_EXTRA = "com.bosnjakovic.ivan.geolocator.locations.extra";

    public GeolocationExtractionService() {
        super("GeolocationExtractionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(GeolocationActivity.TV_ARRAY_EXTRA)) {
                ArrayList<Address> adrese = intent.getParcelableArrayListExtra(GeolocationActivity.TV_ARRAY_EXTRA);
                ArrayList<Address> geocodedAdresses = Utilities.getLatLngFromMultipleAddress(getApplicationContext(), adrese);
                writeLocationsToTextView(geocodedAdresses);
            } else if (intent.hasExtra(GeolocationActivity.FILE_ARRAY_EXTRA)) {
                ArrayList<Address> adrese = intent.getParcelableArrayListExtra(GeolocationActivity.FILE_ARRAY_EXTRA);
                ArrayList<Address> geocodedAdresses = Utilities.getLatLngFromMultipleAddress(getApplicationContext(), adrese);
                writeLocationsToFile(geocodedAdresses);
            }
        }
    }

    private void writeLocationsToTextView(ArrayList<Address> geocodedAddresses) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Address address : geocodedAddresses) {
            stringBuilder.append(address);
            stringBuilder.append("\n");
        }
        final String finale = stringBuilder.toString();
        Intent intent = new Intent(LOCATIONS_BROADCAST);
        // add data
        intent.putExtra(LOCATIONS_EXTRA, finale);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void writeLocationsToFile(ArrayList<Address> geocodedAddresses) {

        final String filename = "Converted addresses.txt";
        FileOutputStream outputStream;
        File file = null;
        StringBuilder stringBuilder = new StringBuilder();

        for (Address address : geocodedAddresses) {
            stringBuilder.append(address);
            stringBuilder.append("\n");
        }

        String output = stringBuilder.toString();

        if (isExternalStorageWritable()) {
            file = new File(Environment.getExternalStorageDirectory(), filename);
            try {
                outputStream = new FileOutputStream(file);
                try {
                    outputStream.write(output.getBytes());
                } finally {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(LOCATIONS_BROADCAST);
        String data;
        if (file != null && file.length() > 0) {
            data = file.getAbsolutePath();
            intent.putExtra(SUCCES, data);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            data = "Something went wrong";
            intent.putExtra(UNSUCCES, data);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
