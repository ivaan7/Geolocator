package com.bosnjakovic.ivan.geolocator.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.bosnjakovic.ivan.geolocator.model.Address;
import com.bosnjakovic.ivan.geolocator.ui.GeolocationActivity;
import com.bosnjakovic.ivan.geolocator.utility.Utilities;

import java.util.ArrayList;

public class GeolocationExtractionService extends IntentService {

    public static final String LOCATIONS_BROADCAST = "com.bosnjakovic.ivan.geolocator.broadcast.locations";
    public static final String LOCATIONS_EXTRA = "com.bosnjakovic.ivan.geolocator.locations.extra";

    public GeolocationExtractionService() {
        super("GeolocationExtractionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(GeolocationActivity.ADDRESS_ARRAY_EXTRA)) {
                ArrayList<Address> adrese = intent.getParcelableArrayListExtra(GeolocationActivity.ADDRESS_ARRAY_EXTRA);
                ArrayList<Address> geocodedAdresses = Utilities.getLatLngFromMultipleAddress(getApplicationContext(), adrese);
                writeLocations(geocodedAdresses);
            }
        }
    }

    private void writeLocations(ArrayList<Address> geocodedAddresses) {
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

}
