package com.paavansoni.knowyourgovernment;

import java.io.Serializable;

public class Politician implements Serializable {
    private String office;//basic info
    private String name;
    private String party;

    private String address;//contact info
    private String number;
    private String email;
    private String website;

    private String facebook;//social info
    private String twitter;
    private String google;
    private String youtube;

    private String photo;

 /*   public Politician(){
        this.office = "office";
        this.name = "name";
        this.party = "party";
        this.address = "missing";
        this.number = "missing";
        this.email = "missing";
        this.website = "missing";
        this.facebook = "missing";
        this.twitter = "missing";
        this.google = "missing";
        this.youtube = "missing";
        this.photo = "missing";
    }*/

    Politician(String office, String name, String party, String address, String number, String email, String website, String facebook, String twitter, String google, String youtube, String photo) {
        this.office = office;
        this.name = name;
        this.party = party;
        this.address = address;
        this.number = number;
        this.email = email;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.google = google;
        this.youtube = youtube;
        this.photo = photo;
    }

    String getOffice() {
        return office;
    }

    public String getName() {
        return name;
    }

   /* public void setName(String name) {
        this.name = name;
    }*/

    String getParty() {
        return party;
    }

    String getAddress() {
        return address;
    }

    String getNumber() {
        return number;
    }

    String getEmail() {
        return email;
    }

    String getWebsite() {
        return website;
    }

    String getFacebook() {
        return facebook;
    }

    String getTwitter() {
        return twitter;
    }

    String getGoogle() {
        return google;
    }

    String getYoutube() {
        return youtube;
    }

    String getPhoto(){
        return photo;
    }
}
