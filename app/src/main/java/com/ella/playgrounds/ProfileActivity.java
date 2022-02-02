package com.ella.playgrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends BaseActivity {
    private static String USER_PROFILE = "USER_PROFILE";
    private User user;
    private Park newActivityPark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //get user
        user = (User) getIntent().getSerializableExtra(USER_PROFILE);
        baseUser = user;

    }


    private void getParkByPid(String pid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parks");
        myRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newActivityPark = dataSnapshot.getValue(Park.class);
//                openParkActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}