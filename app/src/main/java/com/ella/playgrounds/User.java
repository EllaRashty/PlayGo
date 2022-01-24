package com.ella.playgrounds;

import java.io.Serializable;

public class User implements Serializable {

    private  String uid ;
    private  String adultName = "";
    private  String familyName = "";
    private  String childName = "";
    private  String phoneNumber = "";
    private  String status = "";
    private  String favoritePark = "";
    private double lastLng;
//    private double lastLng=0.0;
//    private double lastLat=0.0;
    private double lastLat;
    private String adultTitle = "";
    private String adultGender = "";
    private String childGender = "";
    private String childAge;
    private String about="";
    private String imageUrl;



    public User() {
        lastLng=0.0;
        lastLat=0.0;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getFavoritePark() {
        return favoritePark;
    }

    public User setFavoritePark(String favoritePark) {
        this.favoritePark = favoritePark;
        return this;
    }

    public String getChildName() {
        return childName;
    }

    public User setChildName(String childName) {
        this.childName = childName;
        return this;
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

    public User setLastLng(double lastLng) {
        this.lastLng = lastLng;
        return this;
    }

    public double getLastLat() {
        return lastLat;
    }

    public User setLastLat(double lastLat) {
        this.lastLat = lastLat;
        return this;
    }

    public String getAdultTitle() {
        return adultTitle;
    }

    public User setAdultTitle(String adultTitle) {
        this.adultTitle = adultTitle;
        return this;
    }

    public String getAdultGender() {
        return adultGender;
    }

    public User setAdultGender(String adultGender) {
        this.adultGender = adultGender;
        return this;
    }

    public String getChildAge() {
        return childAge;
    }

    public User setChildAge(String childAge) {
        this.childAge = childAge;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public User setAbout(String about) {
        this.about = about;
        return this;
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

    public User setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getChildGender() {
        return childGender;
    }

    public User setChildGender(String childGender) {
        this.childGender = childGender;
        return this;
    }
}
