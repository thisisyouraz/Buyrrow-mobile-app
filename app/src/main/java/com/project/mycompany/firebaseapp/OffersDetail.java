package com.project.mycompany.firebaseapp;

public class OffersDetail {

    double amount;
    String contact;

    public OffersDetail() {
    }

    public OffersDetail(double amount) {

        this.amount = amount;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
