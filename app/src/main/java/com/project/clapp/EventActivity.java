package com.project.clapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.io.File;
import java.io.IOException;

public class EventActivity extends AppCompatActivity {
    TextView nameInput, localInput, timeInput, dateInput, creatorInput, numRegister, maxRegister, priceInput;
    ImageView imgView;
    private StorageReference mStorageRef;
    private GoogleMap mMap;
    LatLng latlng;
    String address;
    private static final float DEFAULT_ZOOM = 15f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String eventId = extras.getString("id");
            Event event = EventFirebaseManager.getInstance().getEvent(eventId);
            fillDataEvent(event);
        }

    }

    public void fillDataEvent(Event event) {
        nameInput = findViewById(R.id.eventNameInput);
        imgView = findViewById(R.id.imageViewInput);
        nameInput.setText(event.getName());
        address = event.getLocal();
        double longitude = event.getLongitude();
        double latitude = event.getLatitude();
        latlng = new LatLng(latitude, longitude);

        dateInput = findViewById(R.id.eventDateInput);
        dateInput.setText(event.getDate());

        timeInput = findViewById(R.id.eventTimeInput);
        timeInput.setText(event.getTime());

        localInput = findViewById(R.id.eventLocationInput);
        localInput.setText(address);

        String numR = Integer.toString(event.getNumRegister());
        String maxR = Integer.toString(event.getMaxRegisters());

        numRegister = findViewById(R.id.eventNumInput);
        numRegister.setText(numR);

        maxRegister = findViewById(R.id.eventMaxInput);
        maxRegister.setText(maxR);

        priceInput = findViewById(R.id.eventPriceInput);
        if (event.getPriceEvent() == 0) {
            priceInput.setText("FREE!");
        } else {
            priceInput.setText(Double.toString(event.getPriceEvent()));
        }


        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference eventImg = mStorageRef.child("events").child(event.getImgURL());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File newFile = localFile;
        eventImg.getFile(newFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...

                        imgView.setImageURI(Uri.fromFile(newFile));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }


        });

        initMap();

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEvent);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                moveCamera(latlng, DEFAULT_ZOOM, address);
            }
        });
    }
}
