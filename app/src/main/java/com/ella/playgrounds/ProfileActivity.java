package com.ella.playgrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
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
    private ShapeableImageView profile_IMG_user;

    private TextView user_name;
    private TextView family_name;
    private TextView title;
    private TextView gender;
    private TextView child_name;
    private TextView age;
    private TextView child_gender;
    private TextView about;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //get user
        user = (User) getIntent().getSerializableExtra(USER_PROFILE);
        baseUser = user;
        findView();
        init_views();

    }

    private void init_views() {
        user_name.setText(user.getAdultName());
        family_name.setText(user.getFamilyName());
        title.setText(user.getAdultTitle());
        gender.setText(user.getAdultGender());
        child_name.setText(user.getChildName());
        age.setText(user.getChildAge());
        child_gender.setText(user.getChildGender());
        about.setText(user.getAbout());

        back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findView() {
        user_name = findViewById(R.id.user_name);
        family_name = findViewById(R.id.family_name);
        title = findViewById(R.id.title);
        gender = findViewById(R.id.gender);
        child_name = findViewById(R.id.child_name);
        age = findViewById(R.id.age);
        child_gender = findViewById(R.id.child_gender);
        about = findViewById(R.id.about);
        profile_IMG_user = findViewById(R.id.profile_IMG_user);
        showImage();

    }

    private void showImage() {
        if (user.getImageUrl() != null) {
            Glide.with(this /* context */)
                    .load(user.getImageUrl())
                    .into(profile_IMG_user);
        }
    }


}