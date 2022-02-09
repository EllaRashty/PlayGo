package com.ella.playgrounds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

public class EditProfileActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 101;

    private TextInputLayout familyName, adultName, edit_adult_title;
    private MaterialButtonToggleGroup adult_gender, child_gender;
    private TextInputLayout childName, child_age, child_about;
    private User currentUser;
    private ImageView pick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        getCurrentUserFromDatabase();
        importPhoto();
        findViews();
        operations();
    }

    private void importPhoto() {
        String[] cameraPermission;
        String[] storagePermission;
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pick = findViewById(R.id.pick_img);
        pick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int picd = 0;
                if (picd == 0) {
                    if (!checkCameraPermission())
                        requestCameraPermission(cameraPermission);
                    else
                        pickFromGallery();
                } else if (picd == 1) {
                    if (!checkStoragePermission())
                        requestStoragePermission(storagePermission);
                    else
                        pickFromGallery();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(pick);
//                currentUser.setImageUrl(resultUri.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if (camera_accepted && storage_accepted)
                        pickFromGallery();
                    else
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT);
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_accepted)
                        pickFromGallery();
                    else
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT);
                }
            }
            break;
        }
    }

    private void requestStoragePermission(String storagePermission[]) {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    public void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(String cameraPermission[]) {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    private void operations() {
        //save all the details
        MaterialButton save_MBTN = findViewById(R.id.save_MBTN);
        save_MBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
                finish();
            }
        });

        childName.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    clearFocus(childName.getEditText());
                return false;
            }
        });


        edit_adult_title.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus(edit_adult_title.getEditText());
                }
                return false;
            }
        });

        child_age.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus(child_age.getEditText());
                }
                return false;
            }
        });

        adultName.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus(adultName.getEditText());
                }
                return false;
            }
        });

        familyName.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus(familyName.getEditText());
                }
                return false;
            }
        });

        MaterialButton cancel_MBTN = findViewById(R.id.cancel_MBTN);
        cancel_MBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void clearFocus(EditText EditText) {
        EditText.clearFocus();
    }

    private void saveDetails() {
        currentUser.setAbout(Objects.requireNonNull(child_about.getEditText()).getText().toString());
        currentUser.setAdultName(Objects.requireNonNull(adultName.getEditText()).getText().toString());
        currentUser.setChildName(Objects.requireNonNull(childName.getEditText()).getText().toString());
        currentUser.setFamilyName(Objects.requireNonNull(familyName.getEditText()).getText().toString());
        currentUser.setChildAge(Objects.requireNonNull(child_age.getEditText()).getText().toString());
        currentUser.setAdultTitle(Objects.requireNonNull(edit_adult_title.getEditText()).getText().toString());

        if (adult_gender.getCheckedButtonId() == R.id.btn_male)
            currentUser.setAdultGender("MALE");
        else if (adult_gender.getCheckedButtonId() == R.id.edit_female)
            currentUser.setAdultGender("FEMALE");

        if (child_gender.getCheckedButtonId() == R.id.child_boy)
            currentUser.setChildGender("BOY");
        else if (child_gender.getCheckedButtonId() == R.id.child_girl)
            currentUser.setChildGender("GIRL");

        updateUserDatabase();
    }

    private void getCurrentUserFromDatabase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                baseUser = currentUser;
                setUserText(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUserText(User currentUser) {
        if (currentUser.getAdultName() == null) {
            adultName.getEditText().setText("New Member");
        } else {
            adultName.getEditText().setText(currentUser.getAdultName());
        }
        childName.getEditText().setText(currentUser.getChildName());
        edit_adult_title.getEditText().setText(currentUser.getAdultTitle());
        child_about.getEditText().setText(currentUser.getAbout());
        child_age.getEditText().setText(currentUser.getChildAge());
        familyName.getEditText().setText(currentUser.getFamilyName());

        if (currentUser.getAdultGender().equals("MALE")) {
            adult_gender.check(R.id.btn_male);
        } else if (currentUser.getAdultGender().equals("FEMALE")) {
            adult_gender.check(R.id.edit_female);
        }
        if (currentUser.getChildGender().equals("BOY")) {
            child_gender.check(R.id.child_boy);
        } else if (currentUser.getChildGender().equals("GIRL")) {
            child_gender.check(R.id.child_girl);
        }
        showImage();
    }

    private void findViews() {
        adult_gender = findViewById(R.id.edit_gender);
        child_gender = findViewById(R.id.child_gender);
        edit_adult_title = findViewById(R.id.edit_title);
        child_age = findViewById(R.id.child_age);
        adultName = findViewById(R.id.edit_firstName);
        childName = findViewById(R.id.edit_childName);
        child_about = findViewById(R.id.child_about);
        familyName = findViewById(R.id.edit_familyName);
    }

    private void showImage() {
        ImageView pick_img = findViewById(R.id.pick_img);
        if (currentUser.getImageUrl() != null) {
            Glide.with(this)
                    .load(currentUser.getImageUrl())
                    .into(pick_img);
        }
    }

    private void updateUserDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());
        myRef.setValue(currentUser);
    }
}