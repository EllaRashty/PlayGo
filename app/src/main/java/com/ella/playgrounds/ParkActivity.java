package com.ella.playgrounds;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkActivity extends BaseActivity {
    private Park park, newActivityPark;
    public static final String PARK = "PARK";
    private User currentUser;
    private ImageButton park_MBTN_favorite;

    public final int FAVORITE = 1;
    public final int UNFAVORITE = 0;
    private int parkStatus;
    public final String STAR_FILL = "ic_register";
    public final String STAR_EMPTY = "ic_register_empty";

    private ImageButton park_BTN_navigation;
    private TextView park_LBL_status_address;
    private TextView park_LBL_name;
    private TextView park_LBL_status_shade;
    private TextView park_LBL_status_lights;
    private TextView park_LBL_status_benches;
    private TextView park_LBL_status_water;
    private ImageButton park_IBTN_chat;
    private ImageButton back;
    private ImageButton reg;
    private ImageView park_IMG;
    private ImageView main_IMG_background;
    private FirebaseDatabase database;
    private ArrayList<User> users;
    private UserAdapter userAdapter;
    private DatabaseReference myRef;
    private RecyclerView list_RV_users;
    private FirebaseAuth firebaseAuth;

    private Rating rating;
    private RatingBar park_RB_rate;
    private MaterialButton park_MBTN_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        Objects.requireNonNull(getSupportActionBar()).hide();

        park = (Park) getIntent().getSerializableExtra(PARK);


        findView();
        init();
        getCurrentUserFromDatabase();
        setAdapter();

        park_MBTN_favorite = findViewById(R.id.park_MBTN_favorite);
        //make the park - favorite
        park_MBTN_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritePark();
            }
        });

    }

    private void findView() {

        park_LBL_status_address = findViewById(R.id.park_LBL_status_address);
        park_LBL_status_shade = findViewById(R.id.park_LBL_status_shade);
        park_LBL_status_lights = findViewById(R.id.park_LBL_status_lights);
        park_LBL_status_benches = findViewById(R.id.park_LBL_status_benches);
        park_LBL_status_water = findViewById(R.id.park_LBL_status_water);
        park_LBL_name = findViewById(R.id.park_LBL_name);

//        btn_online = findViewById(R.id.edit_online);


        park_BTN_navigation = findViewById(R.id.park_BTN_navigation);


        list_RV_users = findViewById(R.id.list_RV_users);

        park_IBTN_chat = findViewById(R.id.chat_BTM);
        reg = findViewById(R.id.register_BTM);
        park_RB_rate = findViewById(R.id.park_RB_rate);


    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        rating = new Rating();
        currentUser = new User();

        park_LBL_name.setText(park.getName());
        park_LBL_status_address.setText(park.getAddress());
        park_LBL_status_water.setText(park.getWater());
        park_LBL_status_shade.setText(park.getShade());
        park_LBL_status_lights.setText(park.getLights());
        park_LBL_status_benches.setText(park.getBenches());


//        park_BTN_navigation = findViewById(R.id.park_BTN_navigation);
        park_BTN_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMap();
            }
        });

        back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.park_IMG = findViewById(R.id.img_00);
        this.main_IMG_background=findViewById(R.id.main_IMG_background);
        Glide
                .with(this)
                .load(park.getParkImage1())
                .into(park_IMG);
        Glide
                .with(this)
                .load("https://indiagardening.com/wp-content/uploads/2019/12/1Grass.jpg")
                .into(main_IMG_background);





        //open chat
        park_IBTN_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity();
            }
        });

        park_MBTN_rate = findViewById(R.id.park_MBTN_rate);
        park_MBTN_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rating.checkIfUserExist(currentUser.getUid())) {
                    getUserRating();
                } else {
                    Toast.makeText(ParkActivity.this, "You have already rated the park !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter() {
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


    private void getCurrentUserFromDatabase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                baseUser = currentUser;
                checkIfParkIsFavorite();
                getUsersInParkList(database);
                getRatesFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserRating() {
        float rate = park_RB_rate.getRating();
        rating.setRating(rate);
        rating.calcRating();
        park.setRating(rating.getTotRating());
        rating.addUserToRatingList(currentUser.getUid());
        park_RB_rate.setRating(rating.getTotRating());
        park_RB_rate.setIsIndicator(true);

        Toast.makeText(ParkActivity.this, "Thank`s for rating !", Toast.LENGTH_SHORT).show();
        updateRateDatabase();
        updateParkDatabase();
    }

    private void getRatesFromDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Rating").child(park.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    updateRateDatabase();
                } else {
                    rating = snapshot.getValue(Rating.class);
                    checkRate();
                    park_RB_rate.setRating((float) rating.getTotRating());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkRate() {
        String uid = currentUser.getUid();
        if (rating.checkIfUserExist(uid)) {
            park_RB_rate.setIsIndicator(true);
        }
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
                        initDistance(distance_temp, user);
                        //if current park is user`s favorite park - add user to list
                        if (user.getFavoritePark().equals(park.getPid()) && checkIfUserInPark(user) == -1) {
                            updateUsersList(user);
                        }
                        //if the user removed a "favorite " from the park
                        else if (!user.getFavoritePark().equals(park.getPid()) && checkIfUserInPark(user) != -1) {
                            removeUserFromList(user);
                        }
                        //check if user is not at the park
                        if (checkIfUserInPark(user) != -1 && !check_distance(distance_temp) && user.getStatus().equals("online")) {
                            setNewStatus("offline", user);
                            updateUsersStatusList(user);
                        } else if (checkIfUserInPark(user) != -1 && check_distance(distance_temp) && user.getStatus().equals("offline")) {
                            setNewStatus("online", user);
                            updateUsersStatusList(user);
                        }
                    } catch (Exception ex) {
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

    private void initDistance(Distance dis, User user) {
        dis.setUser_lat(user.getLastLat())
                .setUser_lng(user.getLastLng());
    }

    private void updateUsersList(User user) {
        users.add(user);
        userAdapter.notifyItemChanged(users.size());
    }

    private int checkIfUserInPark(User checkUser) {
        if (users != null) {
            for (User user : users) {
                if (user.getUid().equals(checkUser.getUid())) {
                    return users.indexOf(user);
                }
            }
        }
        return -1;
    }

    private void openGoogleMap() {
        // Creates an Intent that will load a map of San Francisco
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
        //todo open profile activity
        //todo send user to profile activity
        //todo create profile activity
        Intent myIntent = new Intent(ParkActivity.this, ProfileActivity.class);
        myIntent.putExtra("USER_PROFILE", user);
        startActivity(myIntent);
    }

    private void favoritePark() {
        //if user click to FAVORITE park
        if (parkStatus == UNFAVORITE) {
            // if user already has a favorite park
            if (!currentUser.getFavoritePark().equals("")) {
                Toast.makeText(this, "You already have a favorite park", Toast.LENGTH_SHORT).show();
            } else {
                //update favorite park
                userPreference(park.getPid(), STAR_FILL, FAVORITE);
                park.addUserToPark(currentUser.getUid());
                Toast.makeText(this, "You have successfully added the park to favorites", Toast.LENGTH_SHORT).show();
            }
        }
        //if user click to UNFAVORITE park
        else {
            userPreference("", STAR_EMPTY, UNFAVORITE);
            park.removeUserFromPark(currentUser.getUid());
        }
        //update park and user in FirebaseDatabase
        updateUserDatabase(currentUser);
        updateParkDatabase();
    }

    //update user Preference for current park
    private void userPreference(String pid, String img, int status) {
        currentUser.setFavoritePark(pid);
        park_MBTN_favorite.setImageResource(getImage(img));
        parkStatus = status;
    }

    //get img resource
    private int getImage(String imgName) {
        return getResources().getIdentifier(imgName, "drawable", getPackageName());
    }

    private void checkIfParkIsFavorite() {
        if (currentUser.getFavoritePark().equals(park.getPid())) {
            int img = getResources().getIdentifier("ic_register", "drawable", getPackageName());
            park_MBTN_favorite.setImageResource(img);
            parkStatus = FAVORITE;
        } else {
            parkStatus = UNFAVORITE;
        }
    }
}