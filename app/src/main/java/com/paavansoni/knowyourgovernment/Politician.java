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

    public Politician(){
        this.office = "office";
        this.name = "name";
        this.party = "party";
        this.address = null;
        this.number = null;
        this.email = null;
        this.website = null;
        this.facebook = null;
        this.twitter = null;
        this.google = null;
        this.youtube = null;
        this.photo = null;
    }

    public Politician(String office, String name, String party, String address, String number, String email, String website, String facebook, String twitter, String google, String youtube, String photo) {
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getPhoto(){
        return photo;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }
}
