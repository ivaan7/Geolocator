package com.bosnjakovic.ivan.geolocator.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ivan on 13.9.2017..
 */
public class Utilities {

    private static Geocoder mGeocoder;

    public static LatLng getLatLngFromSingleAddress(Context context, String locationAddress, String zipCode) {

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

    public static ArrayList<com.bosnjakovic.ivan.geolocator.model.Address> getLatLngFromMultipleAddress(Context context, ArrayList<com.bosnjakovic.ivan.geolocator.model.Address> addresses) {

        mGeocoder = new Geocoder(context, Locale.getDefault());
        ArrayList<com.bosnjakovic.ivan.geolocator.model.Address> geocodedAddresses = new ArrayList<>();
        for (int i = 0; i<addresses.size();i++) {
            try {
                List<Address> adresses = mGeocoder.getFromLocationName(addresses.get(i).getAddress() + " "
                        + addresses.get(i).getZipCode(), 1);
                if (addresses.size() > 0) {
                    double latitude = adresses.get(0).getLatitude();
                    double longitude = adresses.get(0).getLongitude();
                    geocodedAddresses.add(
                            new com.bosnjakovic.ivan.geolocator.model.Address(addresses.get(i).getStoreName(),
                                    addresses.get(i).getAddress(),addresses.get(i).getZipCode(),latitude,longitude));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return geocodedAddresses;
    }
}