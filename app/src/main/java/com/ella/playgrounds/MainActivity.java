package com.ella.playgrounds;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

//interface CallBack_UploadParks {
//    void UploadParks(List<Park> parksList);
//}



public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private MapFragment fragment_map;
    private ParksData parksData;
    private User currentUser;
    private DatabaseReference myRef;
    private double lat;
    private double log;



    int LOCATION_REQUEST_CODE = 1001;

    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLoginActivity();
        } else {
            currentUser = new User();
            addAndUpdateUser();
            initMap();
            database = FirebaseDatabase.getInstance();
//            initMap();


        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            readParksAndShowOnMap();
        } else {
            askLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        parksData.setCallBack_UploadParks(callBack_uploadParks);
    }

    private void initMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fragment_map = new MapFragment();
        fragment_map.setCallBack_location(callBack_location);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment_map)
                .commit();
        readParksAndShowOnMap();
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    private void updateUserDatabase() {
        myRef = database.getReference("Users").child(currentUser.getUid());
        myRef.setValue(currentUser);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "onSuccess: " + location.toString());
                    Log.d(TAG, "onSuccess: " + location.getLatitude());
                    Log.d(TAG, "onSuccess: " + location.getLongitude());
                    // TODO: 19/01/2022 put location in user object
//                    if (currentUser.getUid() != null ) {
                        lat=location.getLatitude();
                        log=location.getLongitude();
//                        updateUserDatabase();
//                    }
//                    if(callBack_location != null){
//                        callBack_location.updateLocation(lat,log);
//                    }
//                    fragment_map.showMarker(location.getLatitude(), location.getLongitude());
                } else {
                    Log.d(TAG, "onFailure: Location was null..");
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());

            }
        });

    }
    //get current location callback
    private CallBack_Location callBack_location = new CallBack_Location() {
        @Override
        public void updateLocation() {
            // check if currentUser is not null and the distance is more than 20 meters from the previous location to update lan ,lng
            if (currentUser.getUid() != null ) {
                currentUser.setLastLat(lat);
                currentUser.setLastLng(log);
                updateUserDatabase();
            }
            fragment_map.showMarker(lat, log);
        }
    };

    private void addAndUpdateUser() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    addNewUser();
                    return;
                }
                currentUser = dataSnapshot.getValue(User.class);
                baseUser = currentUser;
                if (callBack_location != null) {
                    callBack_location.updateLocation();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void addNewUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        User user = new User()
                .setUid(firebaseAuth.getUid())
                .setStatus("offline");
        baseUser = user;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.child(user.getUid()).setValue(user);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit:
                Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show();
                openEditProfileActivity();
                return true;
            case R.id.action_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    startLoginActivity();
                                else
                                    Log.d(TAG, "onComplete: ", task.getException());
                            }
                        });
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void openEditProfileActivity() {
        Intent myIntent = new Intent(this, EditProfileActivity.class);
        startActivity(myIntent);
    }

    // upload parks to map callback
    private CallBack_UploadParks callBack_uploadParks = new CallBack_UploadParks() {
        @Override
        public void UploadParks(List<Park> parksList) {
            for (Park park : parksList) {
                fragment_map.addParkMarkers(park.getLat(), park.getLng(), park.getPid());

            }
        }
    };

    private void readParksAndShowOnMap() {
        parksData = new ParksData(this);
        parksData.getParks();
        parksData.setCallBack_UploadParks(callBack_uploadParks);
    }

}