package com.ella.playgrounds;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    private Button park_BTN_navigation;
    private TextView park_LBL_status_address;
    private TextView park_LBL_name;
    private TextView park_LBL_status_shade;
    private TextView park_LBL_status_lights;
    private TextView park_LBL_status_benches;
    private TextView park_LBL_status_water;
    private Button park_IBTN_chat;
    private ImageButton back;
    private AppCompatImageView park_IMG;
    private FirebaseDatabase database;
    private ArrayList<User> users;
    private UserAdapter userAdapter;
    private DatabaseReference myRef;
    private RecyclerView list_RV_users;
    private FirebaseAuth firebaseAuth;


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


    }

    private void findView() {

        park_LBL_status_address = findViewById(R.id.park_LBL_status_address);
        park_LBL_status_shade = findViewById(R.id.park_LBL_status_shade);
        park_LBL_status_lights = findViewById(R.id.park_LBL_status_lights);
        park_LBL_status_benches = findViewById(R.id.park_LBL_status_benches);
        park_LBL_status_water = findViewById(R.id.park_LBL_status_water);
        park_LBL_name = findViewById(R.id.park_LBL_name);


        park_BTN_navigation = findViewById(R.id.park_BTN_navigation);
        park_LBL_status_address.setText(park.getAddress());
        park_LBL_name.setText(park.getName());
        park_LBL_status_water.setText(park.getWater());
        park_LBL_status_shade.setText(park.getShade());
        park_LBL_status_lights.setText(park.getLights());
        park_LBL_status_benches.setText(park.getBenches());

        list_RV_users = findViewById(R.id.list_RV_users);

        park_IBTN_chat = findViewById(R.id.chat_BTM);


    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        currentUser = new User();

        park_BTN_navigation = findViewById(R.id.park_BTN_navigation);
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
        Glide
                .with(this)
                .load(park.getParkImage1())
                .into(park_IMG);

        //open chat
        park_IBTN_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity();
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
//                openProfileActivity(users.get(position));
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
//                checkIfParkIsFavorite();
                getUsersInParkList(database);
//                getRatesFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUsersInParkList(FirebaseDatabase database) {
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot key : snapshot.getChildren()) {
                    try{
                        User user = key.getValue(User.class);
                        //init distance with user coordinates
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
                    }catch (Exception ex){}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        if (users!=null) {
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

}