package com.project.clapp;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearYouMap extends Fragment implements GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    LatLng latlng;
    String id_event;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseAuth mAuth;
    String userID;
    ArrayList<String> eventIDs = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();
    TextView nameEventMap, dateEventMap, placeEventMap;
    LinearLayout ln;

    public NearYouMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_near_you_map, container, false);
        nameEventMap = view.findViewById(R.id.nameEventMap);
        placeEventMap = view.findViewById(R.id.placeEventMap);
        dateEventMap = view.findViewById(R.id.dateEventMap);
        ln = view.findViewById(R.id.mapEventCard);
        ln.setVisibility(View.GONE);
        ln.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        // Inflate the layout for this fragment
        getLocationPermission();

        ln.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                getEvent();
            }
        });

        return view;

    }

    private void populateEvents() {
        ArrayList<Event> EVENTS = EventFirebaseManager.getInstance().getEventList();

        for (int i = 0; i < EVENTS.size(); i++) {
            LatLng lnglat = new LatLng(EVENTS.get(i).getLatitude(), EVENTS.get(i).getLongitude());
            setMarker(lnglat, EVENTS.get(i));
        }

    }

    public void getEvent() {
        Intent i = new Intent(getActivity().getBaseContext(), EventActivity.class);
        i.putExtra("id", id_event);
        startActivity(i);
    }



    private void setMarker(LatLng latlng, final Event event) {
        if (event.getuID().equals(userID)) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .snippet(event.getId())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(event.getName()));

            markerList.add(marker);
        } else if (event.getUserList().contains(userID)) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .snippet(event.getId())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(event.getName()));

            markerList.add(marker);
        } else {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .snippet(event.getId())
                    .title(event.getName()));

            markerList.add(marker);
        }

    }



    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM));
                        populateEvents();
                    }
                }
            });

        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }
    private void getLocationPermission() {
        String[] permissions = {
                FINE_LOCATION,
                COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(
                this.getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(
                    this.getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(
                        this.getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);

            }
        }
        else {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                        mLocationPermissionGranted = true;
                        initMap();
                    }

                }
            }
        }
    }

    public void markerClick() {
        mMap.setOnMarkerClickListener(this);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.nearYouMap);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (mLocationPermissionGranted) {
                    getDeviceLocation();

                    if (ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;

                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                    markerClick();

                }

            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ln.setVisibility(View.VISIBLE);
        ln.setEnabled(true);


        for (int i = 0;  i < markerList.size(); i++) {
            if (marker.getSnippet().equals(markerList.get(i).getSnippet()))
            {
                for (int z = 0; z < EventFirebaseManager.getInstance().getEventList().size(); z++) {
                    if (marker.getSnippet().equals(EventFirebaseManager.getInstance().getEventList().get(i).getId())) {
                        nameEventMap.setText(EventFirebaseManager.getInstance().getEventList().get(i).getName());
                        placeEventMap.setText(EventFirebaseManager.getInstance().getEventList().get(i).getPlace());
                        dateEventMap.setText(EventFirebaseManager.getInstance().getEventList().get(i).getDate() + " " + EventFirebaseManager.getInstance().getEventList().get(i).getTime());
                    }
                }
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                id_event = markerList.get(i).getSnippet();
            }
        }

        return false;
    }
}
