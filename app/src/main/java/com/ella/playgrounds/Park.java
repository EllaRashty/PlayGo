package com.ella.playgrounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Park implements Serializable {

    private final String ONLINE = "online";
    private final String OFFLINE = "offline";

    private double lng = 0.0;
    private double lat = 0.0;
    private String pid = "";
    private String name = "";
    private String address = "";
    private float rating;
    private List<String> usersUidList;
    private String parkImage1; // link
    private String parkImage2; // link
    private String parkImage3; // link
    private String parkImage4; // link

    private String water = "";
    private String shade = "";
    private String lights = "";
    private String benches = "";


    public Park() {
        usersUidList = new ArrayList<>();
    }


    public double getLng() {
        return lng;
    }

    public Park setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Park setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public Park setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Park setAddress(String address) {
        this.address = address;
        return this;
    }

    public void addUserToPark(String uid) {
        if (!usersUidList.contains(uid)) {
            usersUidList.add(uid);
        }

    }

    public void removeUserFromPark(String uid) {
        usersUidList.remove(uid);
    }

    public List<String> getUsersUidList() {
        return usersUidList;
    }

    public Park setUsersUidList(List<String> usersUidList) {
        this.usersUidList = usersUidList;
        return this;
    }

    public String getParkImage1() {
        return parkImage1;
    }

    public Park setParkImage1(String parkImage1) {
        this.parkImage1 = parkImage1;
        return this;
    }

    public String getParkImage2() {
        return parkImage2;
    }

    public Park setParkImage2(String parkImage2) {
        this.parkImage2 = parkImage2;
        return this;
    }

    public String getParkImage3() {
        return parkImage3;
    }

    public Park setParkImage3(String parkImage3) {
        this.parkImage3 = parkImage3;
        return this;
    }

    public String getParkImage4() {
        return parkImage4;
    }

    public Park setParkImage4(String parkImage4) {
        this.parkImage4 = parkImage4;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Park setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public String getWater() {
        return water;
    }

    public Park setWater(String water) {
        this.water = water;
        return this;
    }

    public String getShade() {
        return shade;
    }

    public Park setShade(String shade) {
        this.shade = shade;
        return this;
    }

    public String getLights() {
        return lights;
    }

    public Park setLights(String lights) {
        this.lights = lights;
        return this;
    }

    public String getBenches() {
        return benches;
    }

    public Park setBenches(String benches) {
        this.benches = benches;
        return this;
    }

    public String getName() {
        return name;
    }

    public Park setName(String name) {
        this.name = name;
        return this;
    }
}
