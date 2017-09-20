package com.bosnjakovic.ivan.geolocator.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivan on 15.9.2017..
 */

public class Address implements Parcelable {

    private String storeName;
    private String address;
    private String zipCode;
    private double latitude;
    private double longitude;

    public Address(String storeName, String address, String zipCode) {
        this.storeName = storeName;
        this.address = address;
        this.zipCode = zipCode;
    }

    public Address(String storeName, String address, String zipCode, double latitude, double longitude) {
        this.storeName = storeName;
        this.address = address;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Address(Parcel in) {
        storeName = in.readString();
        address = in.readString();
        zipCode = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public String toString() {
        return "Location name: " + storeName + "\n"
                + "Location address: " + address + "\n"
                + "Zip code: " + zipCode + "\n"
                + "Latitude: " + latitude + "\n"
                + "Longitude: " + longitude;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(storeName);
        parcel.writeString(address);
        parcel.writeString(zipCode);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
