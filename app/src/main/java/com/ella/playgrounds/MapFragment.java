package com.ella.playgrounds;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static GoogleMap mMap;
    private Marker prevMarker;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private boolean markerChanged = false;
    private boolean zoomOnce = false;
    public Marker marker;
    private CallBack_Location callBack_location;
    private CallBack_showPopUp callBack_showPopUp;


    public void setCallBack_location(CallBack_Location callBack_location){
        this.callBack_location =callBack_location;
    }
    public void setCallBack_showPopUp(CallBack_showPopUp callBack_showPopUp) {
        this.callBack_showPopUp = callBack_showPopUp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_mapi);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        return view;
    }

    //show current user marker
    public void showMarker(double lat, double lng) {
        if (markerChanged == true) {
            marker.remove();
            markerChanged = false;
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng)));
        marker.setIcon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerChanged = true;
        if (!zoomOnce) {
            zoomOnMarker(marker);
            zoomOnce = true;
        }

    }


    // add park marker on map
    public void addParkMarkers(double lat, double lng, String pid) {
        Marker parkMarker;
        if (mMap != null) {
            parkMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng)));
            assert parkMarker != null;
            parkMarker.setTag(pid);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//            LatLng latLng = new LatLng(27.1751,78.0421);
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Taj").snippet("wonder");
//            mMap.addMarker(markerOptions);
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16);
//            mMap.animateCamera(cameraUpdate);
        showParkDetails();

//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title("Taj");
//                        googleMap.clear();
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
//
//                        googleMap.addMarker(markerOptions);
//                    }
//                });

    }

    public void showParkDetails() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                if (marker.getTag() != null) {
                    if (prevMarker != null && prevMarker != marker) {
                        prevMarker.setIcon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    prevMarker = marker;
                    marker.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    zoomOnMarker(marker);
                    callBack_showPopUp.PopUpWindowOnMap((String) marker.getTag());
                    return true;
                }
                return false;
            }
        });

    }

    public void zoomOnMarker(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13f));
    }

//    private LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            if (locationResult == null) {
//                return;
//            }
//            Location location = locationResult.getLocations().get(0);
//            double lng = location.getLongitude();
//            double lat = location.getLatitude();
//            if (callBack_location != null) {
//                callBack_location.updateLocation();
//            }
//        }
//    };


}