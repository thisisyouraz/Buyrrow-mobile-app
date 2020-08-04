package com.project.mycompany.firebaseapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {

    private String title;
    Uri []uri;
    String adType;
    String specs;
    String price;
    String category;

    public Credentials(String title, Uri[] uri, String adType, String specs, String price, String category) {
        this.title = title;
        this.uri = uri;
        this.adType = adType;
        this.specs = specs;
        this.price = price;
        this.category=category;
    }

    protected Credentials(Parcel in) {
        title = in.readString();
        uri = in.createTypedArray(Uri.CREATOR);
        adType = in.readString();
        specs = in.readString();
        price = in.readString();
        category = in.readString();
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri[] getUri() {
        return uri;
    }

    public void setUri(Uri[] uri) {
        this.uri = uri;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedArray(uri, flags);
        dest.writeString(adType);
        dest.writeString(specs);
        dest.writeString(price);
        dest.writeString(category);
    }

}
