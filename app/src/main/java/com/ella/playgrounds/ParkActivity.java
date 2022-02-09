package com.ella.playgrounds;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ParkActivity extends BaseActivity {
    private Park park;
    private boolean parkStatus;
    private ArrayList<User> users;
    private UserAdapter userAdapter;
    private DatabaseReference myRef;
    private Rating rating;
    private RatingBar rate_RB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        fileText();
        operations();
    }

    private void fileText() {
        TextView park_LBL_status_address = findViewById(R.id.park_LBL_status_address);
        TextView park_LBL_status_shade = findViewById(R.id.park_LBL_status_shade);
        TextView park_LBL_status_lights = findViewById(R.id.park_LBL_status_lights);
        TextView park_LBL_status_benches = findViewById(R.id.park_LBL_status_benches);
        TextView park_LBL_status_water = findViewById(R.id.park_LBL_status_water);
        TextView park_LBL_name = findViewById(R.id.park_LBL_name);

        park_LBL_name.setText(park.getName());
        park_LBL_status_address.setText(park.getAddress());
        park_LBL_status_water.setText(park.getWater());
        park_LBL_status_shade.setText(park.getShade());
        park_LBL_status_lights.setText(park.getLights());
        park_LBL_status_benches.setText(park.getBenches());
    }

    private void operations() {
        ImageButton park_BTN_navigation = findViewById(R.id.park_BTN_navigation);
        park_BTN_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMap();
            }
        });

        //open chat
        ImageButton park_IBTN_chat = findViewById(R.id.chat_BTM);
        park_IBTN_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity();
            }
        });

        ImageButton back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MaterialButton park_MBTN_rate = findViewById(R.id.park_MBTN_rate);
        park_MBTN_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rating.checkIfUserExist(baseUser.getUid())) {
                    getUserRating();
                } else {
                    Toast.makeText(ParkActivity.this, "You have already rated the park !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // register/unregister to park
        ImageButton register = findViewById(R.id.register_BTN);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToPark(register);
            }
        });

        getCurrentUserFromDatabase(register);
    }

    private void init() {
        park = (Park) getIntent().getSerializableExtra("PARK");
        users = new ArrayList<>();
        rating = new Rating();
        rate_RB = findViewById(R.id.rate_RB);
        displayImages();
        setAdapter();
    }

    private void displayImages() {
        ImageView park_IMG1, park_IMG2, park_IMG3, park_IMG4, main_IMG_background;
        park_IMG1 = findViewById(R.id.img_00);
        uploadImage(park.getParkImage1(), park_IMG1);

        park_IMG2 = findViewById(R.id.img_01);
        uploadImage(park.getParkImage2(), park_IMG2);

        park_IMG3 = findViewById(R.id.img_10);
        uploadImage(park.getParkImage3(), park_IMG3);

        park_IMG4 = findViewById(R.id.img_11);
        uploadImage(park.getParkImage4(), park_IMG4);

        main_IMG_background = findViewById(R.id.chat_background);
        uploadImage("https://indiagardening.com/wp-content/uploads/2019/12/1Grass.jpg", main_IMG_background);
    }

    private void uploadImage(String link, ImageView view) {
        Glide
                .with(this)
                .load(link)
                .into(view);
    }

    private void setAdapter() {
        RecyclerView list_RV_users = findViewById(R.id.list_RV_users);
        list_RV_users.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, users);
        list_RV_users.setAdapter(userAdapter);
        userAdapter.setClickListener(new UserAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openProfileActivity(users.get(position));
            }
        });
    }

    private void getCurrentUserFromDatabase(ImageButton register) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        database.getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                baseUser = snapshot.getValue(User.class);
                checkIfParkIsRegister(register);
                getUsersInParkList(database);
                getRatesFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getUserRating() {
        float rate = rate_RB.getRating();
        rating.setRating(rate);
        rating.calcRating();
        park.setRating(rating.getTotRating());
        rating.addUserToRatingList(baseUser.getUid());
        rate_RB.setRating(rating.getTotRating());
        rate_RB.setIsIndicator(true);
        Toast.makeText(ParkActivity.this, "Thank`s for rating !", Toast.LENGTH_SHORT).show();
        updateRateDatabase();
        updateParkDatabase();
    }

    private void getRatesFromDatabase() {
        database.getReference("Rating").child(park.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    updateRateDatabase();
                } else {
                    rating = snapshot.getValue(Rating.class);
                    checkRate();
                    rate_RB.setRating((float) rating.getTotRating());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkRate() {
        String uid = baseUser.getUid();
        if (rating.checkIfUserExist(uid))
            rate_RB.setIsIndicator(true);
    }

    private void updateRateDatabase() {
        myRef = database.getReference("Rating").child(park.getPid());
        myRef.setValue(rating);
    }

    private void getUsersInParkList(FirebaseDatabase database) {
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot key : snapshot.getChildren()) {
                    try {
                        User user = key.getValue(User.class);
                        Distance distance_temp = new Distance();
                        assert user != null;
                        initDistance(distance_temp, user);
                        //if current park is user`s park - add to users list
                        if (user.getRegisterPark().equals(park.getPid()) && checkIfUserInPark(user) == -1)
                            updateUsersList(user);
                        //if the user unregister from the park
                        else if (!user.getRegisterPark().equals(park.getPid()) && checkIfUserInPark(user) != -1)
                            removeUserFromList(user);
                        //check if user is not at the park
                        if (checkIfUserInPark(user) != -1 && !check_distance(distance_temp) && user.getStatus().equals("online")) {
                            setNewStatus(getString(R.string.offline), user);
                            updateUsersStatusList(user);
                        } else if (checkIfUserInPark(user) != -1 && check_distance(distance_temp) && user.getStatus().equals("offline")) {
                            setNewStatus(getString(R.string.online), user);
                            updateUsersStatusList(user);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateParkDatabase() {
        myRef = database.getReference("Parks").child(park.getPid());
        myRef.setValue(park);
    }

    private void updateUsersStatusList(User user) {
        int id = checkIfUserInPark(user);
        users.set(id, user);
        park.addUserToPark(user.getUid());
        userAdapter.notifyItemChanged(id);
    }

    private void updateUserDatabase(User user) {
        myRef = database.getReference("Users").child(user.getUid());
        myRef.setValue(user);
    }

    private void setNewStatus(String status, User user) {
        user.setStatus(status);
        updateUserDatabase(user);
    }

    private boolean check_distance(Distance dis) {
        return dis.checkDistance(park.getLat(), park.getLng());
    }

    private void removeUserFromList(User user) {
        int id = checkIfUserInPark(user);
        users.remove(id);
        userAdapter.notifyItemRemoved(id);
        userAdapter.notifyItemRangeChanged(id, users.size());
    }

    private void initDistance(Distance distance, User user) {
        distance
                .setUser_lat(user.getLastLat())
                .setUser_lng(user.getLastLng());
    }

    private void updateUsersList(User user) {
        users.add(user);
        userAdapter.notifyItemChanged(users.size());
    }

    private int checkIfUserInPark(User checkUser) {
        if (users != null) {
            for (User user : users) {
                if (user.getUid().equals(checkUser.getUid())){
                    park.addUserToPark(user.getUid());
                return users.indexOf(user);
            }
            }
        }
        return -1;
    }

    private void openGoogleMap() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + park.getLat() + "," + park.getLng() + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void openChatActivity() {
        Intent myIntent2 = new Intent(this, ChatActivity.class);
        myIntent2.putExtra("PARK_PID", park.getPid());
        startActivity(myIntent2);
    }

    private void openProfileActivity(User user) {
        Intent myIntent = new Intent(ParkActivity.this, ProfileActivity.class);
        myIntent.putExtra("USER_PROFILE", user);
        startActivity(myIntent);
    }

    private void registerToPark(ImageButton register) {
        //if user click on REGISTER
        if (!parkStatus) {
            // if user already registered to park
            if (!baseUser.getRegisterPark().equals("")) {
                Toast.makeText(this, "You already registered to park", Toast.LENGTH_SHORT).show();
            } else {
                //update register park
                userPreference(park.getPid(), this.getString(R.string.register), !parkStatus, register);
                park.addUserToPark(baseUser.getUid());
                Toast.makeText(this, "You have successfully registered to park", Toast.LENGTH_SHORT).show();
            }
        }
        //if user click to UNREGISTER park
        else {
            userPreference("", this.getString(R.string.unregister), !parkStatus, register);
            park.removeUserFromPark(baseUser.getUid());
        }
        //update park and user in FirebaseDatabase
        updateUserDatabase(baseUser);
        updateParkDatabase();
    }

    //update user Preference for current park
    private void userPreference(String pid, String img, boolean status, ImageButton register) {
        baseUser.setRegisterPark(pid);
        register.setImageResource(getUserImage(img));
        parkStatus = status;
    }

    //get img resource
    private int getUserImage(String imgName) {
        return getResources().getIdentifier(imgName, getString(R.string.drawable), getPackageName());
    }

    private void checkIfParkIsRegister(ImageButton register) {
        if (baseUser.getRegisterPark().equals(park.getPid())) {
            int img = getResources().getIdentifier(getString(R.string.register),getString(R.string.drawable), getPackageName());
            register.setImageResource(img);
            parkStatus = true;
        } else
            parkStatus = false;
    }
}