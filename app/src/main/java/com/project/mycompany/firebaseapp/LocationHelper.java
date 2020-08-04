package com.project.mycompany.firebaseapp;


import android.os.Parcel;
import android.os.Parcelable;

class LocationHelper implements Parcelable {

    private String adId;
    private double Longitude;
    private double Latitude;

    public LocationHelper() {

    }

    public LocationHelper( double longitude, double latitude) {
        Longitude = longitude;
        Latitude = latitude;
    }

    protected LocationHelper(Parcel in) {
        adId = in.readString();
        Longitude = in.readDouble();
        Latitude = in.readDouble();
    }

    public static final Creator<LocationHelper> CREATOR = new Creator<LocationHelper>() {
        @Override
        public LocationHelper createFromParcel(Parcel in) {
            return new LocationHelper(in);
        }

        @Override
        public LocationHelper[] newArray(int size) {
            return new LocationHelper[size];
        }
    };

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adId);
        dest.writeDouble(Longitude);
        dest.writeDouble(Latitude);
    }
}
