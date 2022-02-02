package com.ella.playgrounds;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParksData {
    private List<Park> parksList = new ArrayList<>();
    private Context context;
    private CallBack_UploadParks callBack_uploadParks;

    public void setCallBack_UploadParks(CallBack_UploadParks callBack_uploadParks) {
        this.callBack_uploadParks = callBack_uploadParks;
    }


    public ParksData() {

        // init the all the parks
//        createParks();
    }

    public ParksData(Context context) {
        this.context = context;

        // init the all the parks
//        createParks();
    }

    public List<Park> getParksList() {
        return parksList;
    }

    public ParksData setParksList(List<Park> parksList) {
        this.parksList = parksList;
        return this;
    }

    public void getParks() {
        DatabaseReference mDatabas = FirebaseDatabase.getInstance().getReference().child("Parks");
        mDatabas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot key : snapshot.getChildren()) {
                    Park park = key.getValue(Park.class);
                    parksList.add(park);
                }
                addParksToMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addParksToMap() {
        callBack_uploadParks.UploadParks(parksList);
    }


    private void createParks() {
        DatabaseReference mDatabas = FirebaseDatabase.getInstance().getReference().child("Parks");
        Park park1 = new Park().setAddress(context.getString(R.string.park_1_address)).setLat(32.119612).setLng(34.842249).setPid("park_1")
                .setWater("yes").setShade("no").setLights("yes").setBenches("3")
                .setName(context.getString(R.string.park_1_name));

        Park park2 = new Park().setAddress(context.getString(R.string.park_2_address)).setLat(32.114151).setLng(34.817486).setPid("park_2")
                .setWater("yes").setShade("yes").setLights("yes").setBenches("1")
                .setName(context.getString(R.string.park_2_name)).setParkImage1("https://www.themoviedb.org/t/p/w600_and_h900_bestv2/1MJNcPZy46hIy2CmSqOeru0yr5C.jpg");



        mDatabas.child(park1.getPid()).setValue(park1);
        mDatabas.child(park2.getPid()).setValue(park2);

    }


}
