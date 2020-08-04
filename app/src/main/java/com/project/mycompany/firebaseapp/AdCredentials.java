package com.project.mycompany.firebaseapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdCredentials implements Parcelable{

    private String parentID;
    private String downloadAbleLink;
    private String title;
    private String price;
    private String adType;
    private String adDesSpec;
    private String sellerId;
    private String max_offer;
    private String bidder_id;
    private String category;
    private ArrayList<String> imageLinks;

    private String endTime;
    private String endDate;
    private String buyItNow;
    private String isActive;

    Calendar calendarEndDate;


    public AdCredentials() {
//        Log.d("constructor call", "default");
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public AdCredentials(String parentID, String title, String price, String adType, String adDesSpec, String sellerId, String max_offer, String bidder_id, String category, ArrayList<String> imageLinks, String endDate, String buyItNow,String isActive) {
        this.parentID = parentID;
        this.title = title;
        this.price = price;
        this.adType = adType;
        this.adDesSpec = adDesSpec;
        this.sellerId = sellerId;
        this.max_offer = max_offer;
        this.bidder_id = bidder_id;
        this.category = category;
        this.imageLinks = imageLinks;
        this.endDate = endDate;
        this.buyItNow = buyItNow;
        this.isActive=isActive;
    }

    public Calendar getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(Calendar calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBuyItNow() {
        return buyItNow;
    }

    public void setBuyItNow(String buyItNow) {
        this.buyItNow = buyItNow;
    }

    public AdCredentials(ArrayList<String> downloadAbleLink, String parentID, String title, String price, String adType, String adDesSpec, String id, String max_offer, String bidder_id, String category) {
//        Log.d("constructor call", "all attribute constructor");

       this.imageLinks = downloadAbleLink;
       this.parentID=parentID;
        this.title = title;
        this.price = price;
        this.adType = adType;
        this.adDesSpec = adDesSpec;
        this.sellerId = id;
        this.max_offer = max_offer;
        this.bidder_id = bidder_id;
        this.category=category;
    }


    public AdCredentials(String title, String price, String adType, String adDesSpec, String id, String max_offer, String bidder_id,String category) {
//        Log.d("constructor call", "all attribute constructor");

        this.title = title;
        this.price = price;
        this.adType = adType;
        this.adDesSpec = adDesSpec;
        this.sellerId = id;
        this.max_offer = max_offer;
        this.bidder_id = bidder_id;
        this.category=category;
    }


    protected AdCredentials(Parcel in) {
        parentID = in.readString();
        downloadAbleLink = in.readString();
        title = in.readString();
        price = in.readString();
        adType = in.readString();
        adDesSpec = in.readString();
        sellerId = in.readString();
        max_offer = in.readString();
        bidder_id = in.readString();
        category = in.readString();
        imageLinks = in.createStringArrayList();
        endTime = in.readString();
        endDate = in.readString();
        buyItNow = in.readString();
        isActive=in.readString();
    }

    public static final Creator<AdCredentials> CREATOR = new Creator<AdCredentials>() {
        @Override
        public AdCredentials createFromParcel(Parcel in) {
            return new AdCredentials(in);
        }

        @Override
        public AdCredentials[] newArray(int size) {
            return new AdCredentials[size];
        }
    };


    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getDownloadAbleLink() {
        return downloadAbleLink;
    }

    public void setDownloadAbleLink(String downloadAbleLink) {
        this.downloadAbleLink = downloadAbleLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public void setAdDesSpec(String adDesSpec) {
        this.adDesSpec = adDesSpec;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMax_offer(String max_offer) {
        this.max_offer = max_offer;
    }

    public void setBidder_id(String bidder_id) {
        this.bidder_id = bidder_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getAdType() {
        return adType;
    }

    public String getAdDesSpec() {
        return adDesSpec;
    }

    public String getMax_offer() {
        return max_offer;
    }

    public String getBidder_id() {
        return bidder_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentID);
        dest.writeString(downloadAbleLink);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(adType);
        dest.writeString(adDesSpec);
        dest.writeString(sellerId);
        dest.writeString(max_offer);
        dest.writeString(bidder_id);
        dest.writeString(category);
        dest.writeStringList(imageLinks);
        dest.writeString(endTime);
        dest.writeString(endDate);
        dest.writeString(buyItNow);
        dest.writeString(isActive);
    }
}
