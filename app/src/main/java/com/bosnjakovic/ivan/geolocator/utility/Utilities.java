package com.bosnjakovic.ivan.geolocator.utility;

import android.content.ContentValues;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ivan on 13.9.2017..
 */
public class Utilities {

    private static Geocoder mGeocoder;

    public static LatLng getLatLngFromSingleAddress(Context context, String locationName, String locationAddress, String zipCode){

        mGeocoder = new Geocoder(context, Locale.getDefault());
        LatLng latLng = null;
        try {
            List<Address> adresses = mGeocoder.getFromLocationName(locationAddress + " " + zipCode, 1);
            if (adresses.size() > 0) {
                double latitude = adresses.get(0).getLatitude();
                double longitude = adresses.get(0).getLongitude();
                latLng = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
