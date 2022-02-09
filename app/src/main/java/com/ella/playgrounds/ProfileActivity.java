package com.ella.playgrounds;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.Objects;

public class ProfileActivity extends BaseActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String USER_PROFILE = "USER_PROFILE";
        user = (User) getIntent().getSerializableExtra(USER_PROFILE); //get user
        findView();
    }

    private void display_text() {
        TextView user_name = findViewById(R.id.user_name);
        user_name.setText(user.getAdultName());

        TextView family_name = findViewById(R.id.family_name);
        family_name.setText(user.getFamilyName());

        TextView title = findViewById(R.id.title);
        title.setText(user.getAdultTitle());

        TextView gender = findViewById(R.id.gender);
        gender.setText(user.getAdultGender());

        TextView child_name = findViewById(R.id.child_name);
        child_name.setText(user.getChildName());

        TextView age = findViewById(R.id.age);
        age.setText(user.getChildAge());

        TextView child_gender = findViewById(R.id.child_gender);
        child_gender.setText(user.getChildGender());

        TextView about = findViewById(R.id.about);
        about.setText(user.getAbout());
    }

    private void findView() {
        showImage();
        display_text();
        ImageButton back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showImage() {
        ShapeableImageView profile_IMG_user = findViewById(R.id.profile_IMG_user);
        if (user.getImageUrl() != null) {
            Glide.with(this)
                    .load(user.getImageUrl())
                    .into(profile_IMG_user);
        }
    }
}