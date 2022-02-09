package com.ella.playgrounds;

import java.io.Serializable;

public class User implements Serializable {

    private  String uid ;
    private  String adultName = "";
    private  String familyName = "";
    private String adultTitle = "";
    private String imageUrl;
    private  String childName = "";
    private String adultGender = "";
    private String childGender = "";
    private String childAge;
    private String about="";
    private  String status = "";
    private  String registerPark = "";
    private double lastLng=0.0;
    private double lastLat=0.0;

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getAdultName() {
        return adultName;
    }

    public User setAdultName(String adultName) {
        this.adultName = adultName;
        return this;
    }

    public String getRegisterPark() {
        return registerPark;
    }

    public void setRegisterPark(String favoritePark) {
        this.registerPark = favoritePark;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getStatus() {
        return status;
    }

    public User setStatus(String status) {
        this.status = status;
        return this;
    }

    public double getLastLng() {
        return lastLng;
    }

    public void setLastLng(double lastLng) {
        this.lastLng = lastLng;
    }

    public double getLastLat() {
        return lastLat;
    }

    public void setLastLat(double lastLat) {
        this.lastLat = lastLat;
    }

    public String getAdultTitle() {
        return adultTitle;
    }

    public void setAdultTitle(String adultTitle) {
        this.adultTitle = adultTitle;
    }

    public String getAdultGender() {
        return adultGender;
    }

    public void setAdultGender(String adultGender) {
        this.adultGender = adultGender;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }
}
