package com.ella.playgrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class Park_Activity2 extends AppCompatActivity {
    private Park park;
    public static final String PARK = "PARK";

    private TextView park_LBL_name;
    private TextView park_LBL_status_address;
    private TextView park_LBL_status_shade;
    private TextView park_LBL_status_benches;
    private TextView park_LBL_status_lights;
    private TextView park_LBL_status_water;
    private Button park_BTN_navigation;


    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park2);
        Objects.requireNonNull(getSupportActionBar()).hide();

        park = (Park) getIntent().getSerializableExtra(PARK);
        findView();
        init_views();
        operations();
    }

    private void operations() {
        back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        park_BTN_navigation = findViewById(R.id.park_BTN_navigation);
        park_BTN_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMap();
            }
        });
    }

    private void init_views() {
        park_LBL_name.setText(park.getName());
        park_LBL_status_address.setText(park.getAddress());
        park_LBL_status_water.setText(park.getWater());
        park_LBL_status_shade.setText(park.getShade());
        park_LBL_status_lights.setText(park.getLights());
        park_LBL_status_benches.setText(park.getBenches());

    }

    private void findView() {
        park_LBL_name = findViewById(R.id.park_LBL_name);
        park_LBL_status_address = findViewById(R.id.park_LBL_status_address);
        park_LBL_status_shade = findViewById(R.id.park_LBL_status_shade);
        park_LBL_status_benches = findViewById(R.id.park_LBL_status_benches);
        park_LBL_status_lights = findViewById(R.id.park_LBL_status_lights);
        park_LBL_status_water = findViewById(R.id.park_LBL_status_water);


    }

    private void openGoogleMap() {
        // Creates an Intent that will load a map of San Francisco
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + park.getLat() + "," + park.getLng() + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}