package com.ella.playgrounds;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
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
    private Geocoder geocoder;
    private Marker prevMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment SupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_mapi);
        SupportMapFragment.getMapAsync(this);

//        geocoder = new Geocoder(MainActivity);

        return view;
    }

    // add park marker on map
    public void addParkMarkers(double lat, double lng, String pid) {
        Marker parkMarker;
        parkMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng)));
        parkMarker.setTag(pid);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap=googleMap;
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
            public boolean onMarkerClick(Marker marker) {

                if (marker.getTag() != null) {
                    if (prevMarker != null && prevMarker != marker) {
                        prevMarker.setIcon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    prevMarker = marker;
                    marker.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    zoomOnMarker(marker);
//                    callBack_showPopUp.PopUpWindowOnMap((String) marker.getTag());
                    return true;
                }
                return false;
            }
        });

    }

    public void zoomOnMarker(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13f));
    }
}