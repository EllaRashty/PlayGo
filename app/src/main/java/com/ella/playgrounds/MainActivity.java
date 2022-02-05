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

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private MapFragment fragment_map;
    private ParksData parksData;
    private User currentUser;
    private DatabaseReference myRef;
    private double lat;
    private double lng;

    private TextView map_LBL_park_name;
    private TextView popup_LBL_online;
    private TextView popup_LBL_rating;
    private TextView popup_LBL_rate_users;
    private TextView popup_LBL_distance;
    private MaterialButton popup_BTN_goto;
    private Park currentPark;
    private Park park;
    private Distance distance;

    private FloatingActionButton fab;
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
            // TODO: 03/02/2022 del new user 
            currentUser = new User();
            addAndUpdateUser();
            initMap();
            findView();
            database = FirebaseDatabase.getInstance();


        }

    }

    private void findView() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                updateLocationUser();
                fragment_map.zoomOnMarker(fragment_map.marker);
            }
        });
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
        distance = new Distance();
        fragment_map = new MapFragment();
        fragment_map.setCallBack_location(callBack_location);
        fragment_map.setCallBack_showPopUp(callBack_showPopUp);

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

    private void updateLocationUser() {
        currentUser.setLastLat(lat);
        currentUser.setLastLng(lng);
        updateUserDatabase();
        fragment_map.showMarker(lat, lng);
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
                    lat = location.getLatitude();
                    lng = location.getLongitude();
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
            if (currentUser.getUid() != null && distance.checkIfDistanceChanged(lat, lng)) {
                updateLocationUser();
            }
//            fragment_map.showMarker(lat, lng);
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

    //count online users in current park and update popup view
    private void countOnlineUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfOnlineUsers = 0;
                for (DataSnapshot key : dataSnapshot.getChildren()) {
                    try {

                        User user = key.getValue(User.class);
                        if (currentPark.getUsersUidList().contains(user.getUid()) && user.getStatus().equals("online")) {
                            numOfOnlineUsers++;
                        }
                    } catch (Exception ex) {
                    }
                }
                popup_LBL_online.setText("" + numOfOnlineUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    //create and show pop up
    // the popup shows marker details on map
    public void showPopUpWindowOnMap(String pid) {
        PopUPWindow mPopupWindow = new PopUPWindow(this);
        updatePopUpView(pid, mPopupWindow.PopUpWindowOnMap());
    }

    private void updatePopUpView(String pid, View popUpView) {
        map_LBL_park_name = popUpView.findViewById(R.id.map_LBL_park_name);
        popup_LBL_online = popUpView.findViewById(R.id.popup_LBL_online);
        popup_LBL_rating = popUpView.findViewById(R.id.popup_LBL_rating);
        popup_LBL_rate_users = popUpView.findViewById(R.id.popup_LBL_rate_users);
        popup_LBL_distance = popUpView.findViewById(R.id.popup_LBL_distance);
        popup_BTN_goto = popUpView.findViewById(R.id.popup_BTN_goto);


        //look for current park
        //init view items
        for (Park park : parksData.getParksList()) {
            if (park.getPid() == pid) {
                currentPark = park;
                setDistanceFromCurrLocation();
                updateParkByPid(currentPark.getPid(), false);
                map_LBL_park_name.setText(currentPark.getName());
                countOnlineUsers();
            }
        }


        //go to button on the popup
        popup_BTN_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get park by pid and open park activity
                updateParkByPid(currentPark.getPid(), true);
            }
        });


    }

    private void setDistanceFromCurrLocation() {
        if (currentPark.getLat() != 0.0 && currentPark.getLng() != 0.0) {
            int dist = distance.calcDistance(currentPark.getLat(), currentPark.getLng());
            popup_LBL_distance.setText("" + dist + "m");
        }
    }


    private void updateParkByPid(String pid, boolean openParkActivity) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parks");

        myRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                park = dataSnapshot.getValue(Park.class);

                if (openParkActivity) {
                    currentPark = park;
                    openParkActivity();
                } else {
                    getRatingByPid(park.getPid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void getRatingByPid(String pid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Rating");

        myRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    popup_LBL_rate_users.setText("" + rating.getTotNumOfRates());
                    popup_LBL_rating.setText("" + rating.getTotRating());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private void openParkActivity() {
        Intent myIntent = new Intent(this, ParkActivity.class);
        myIntent.putExtra("PARK", currentPark);
        startActivity(myIntent);
    }

    private void addNewUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        User user = new User()
                .setUid(firebaseAuth.getUid())
                .setAdultName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
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
            case R.id.my_profile:
                Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                myIntent.putExtra("USER_PROFILE", currentUser);
                startActivity(myIntent);
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

    private CallBack_showPopUp callBack_showPopUp = new CallBack_showPopUp() {
        @Override
        public void PopUpWindowOnMap(String pid) {
            showPopUpWindowOnMap(pid);
        }
    };

    private void readParksAndShowOnMap() {
        parksData = new ParksData(this);
        parksData.getParks();
        parksData.setCallBack_UploadParks(callBack_uploadParks);
    }

}