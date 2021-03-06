package com.ella.playgrounds;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean markerChanged = false;
    private boolean zoomOnce = false;
    public Marker marker;
    private CallBack_showPopUp callBack_showPopUp;
    private CallBack_Location callBack_location;

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
        if (markerChanged) {
            marker.remove();
            markerChanged = false;
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng)));
        assert marker != null;
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
        showParkDetails();
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
//                    marker.setIcon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon));
                    zoomOnMarker(marker);
                    callBack_showPopUp.PopUpWindowOnMap((String) marker.getTag());
                    callBack_location.updateLocation();
                    return true;
                }
                return false;
            }
        });
    }

    public void zoomOnMarker(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
    }
}