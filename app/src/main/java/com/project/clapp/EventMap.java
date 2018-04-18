package com.project.clapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

public class EventMap extends AppCompatActivity {
    private GoogleMap mMap;
    LatLng latlng;
    String address;
    private static final float DEFAULT_ZOOM = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String eventId = extras.getString("id");
            Event event = EventFirebaseManager.getInstance().getEvent(eventId);
            latlng = new LatLng(event.getLatitude(), event.getLongitude());
            address = event.getLocal();
            initMap();
        }

    }

    private void moveCamera(LatLng latlng, float zoom, String address) {
        Log.d("MAP", "moveCamera: moving the camera to: lat: " + latlng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(address);
        mMap.addMarker(options);
        mMap.getUiSettings().setScrollGesturesEnabled(false);    }
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nearYouMap);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                moveCamera(latlng, DEFAULT_ZOOM, address);
            }
        });
    }
}
