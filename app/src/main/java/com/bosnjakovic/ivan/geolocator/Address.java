package com.bosnjakovic.ivan.geolocator;

/**
 * Created by Ivan on 15.9.2017..
 */
public class Address {

    private String storeName;
    private String address;
    private String zipCode;

    public Address(String storeName, String address, String zipCode) {
        this.storeName = storeName;
        this.address = address;
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Poslovnica: " + storeName + " adresa: "+address + "poštanski broj: " + zipCode;
    }
}
