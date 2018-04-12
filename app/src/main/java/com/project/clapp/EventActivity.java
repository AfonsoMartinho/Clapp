package com.project.clapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    TextView nameInput, localInput, timeInput, dateInput, creatorInput, numRegister, maxRegister, priceInput;
    ImageView imgView;
    private StorageReference mStorageRef;
    private GoogleMap mMap;
    LatLng latlng;
    String address;
    Event EVENT = new Event();
    Boolean join = false;
    private FirebaseAuth mAuth;
    Button button;


    private static final float DEFAULT_ZOOM = 15f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btnJoin);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String eventId = extras.getString("id");
            Event event = EventFirebaseManager.getInstance().getEvent(eventId);
            EVENT = event;
            fillDataEvent();
        }

    }



    public void fillDataEvent() {
        nameInput = findViewById(R.id.eventNameInput);
        imgView = findViewById(R.id.imageViewInput);
        nameInput.setText(EVENT.getName());
        address = EVENT.getLocal();
        double longitude = EVENT.getLongitude();
        double latitude = EVENT.getLatitude();
        latlng = new LatLng(latitude, longitude);

        dateInput = findViewById(R.id.eventDateInput);
        dateInput.setText(EVENT.getDate());

        timeInput = findViewById(R.id.eventTimeInput);
        timeInput.setText(EVENT.getTime());

        localInput = findViewById(R.id.eventLocationInput);
        localInput.setText(address);

        String numR = Integer.toString(EVENT.getNumRegister());
        String maxR = Integer.toString(EVENT.getMaxRegisters());

        numRegister = findViewById(R.id.eventNumInput);
        numRegister.setText(numR);

        maxRegister = findViewById(R.id.eventMaxInput);
        maxRegister.setText(maxR);

        if (EVENT.getUserList().contains(mAuth.getCurrentUser().getUid())) {
            join = true;
            button.setBackgroundColor(Color.parseColor("#ffbb00"));
            button.setText("Cancel Registration");
        }

        priceInput = findViewById(R.id.eventPriceInput);
        if (EVENT.getPriceEvent() == 0) {
            priceInput.setText("FREE!");
        } else {
            priceInput.setText(Double.toString(EVENT.getPriceEvent()));
        }


        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference eventImg = mStorageRef.child("events").child(EVENT.getImgURL());
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

        String str = EVENT.getDate() + " " + EVENT.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz", Locale.ENGLISH);
        Date currentDate = new Date();
        df.format(currentDate);
        Date date;

        try {
            date = df.parse(str);
            if (currentDate.after(date)) {
                button.setEnabled(false);
                button.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            System.out.println(e);
        }

    }

    public void joinEvent(final View view) {
        if (!join) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Do you wish to register in the Event?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            registerEvent();
                            join = true;
                            button.setBackgroundColor(Color.parseColor("#ffbb00"));
                            button.setText("Cancel Registration");
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on Cancel
                        }
                    }).create();
            dialog.show();
        } else if (join) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Do you wish to cancel the registration")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            cancelRegist();
                            join = false;
                            button.setBackgroundColor(Color.parseColor("#ffbb00"));
                            button.setText("Join");
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on Cancel
                        }
                    }).create();
            dialog.show();
        }

    }

    public boolean registerEvent () {


        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();

        try {
            DatabaseReference userListRef = databaseEvents.child("events").child(EVENT.getId()).child("userList").child(mAuth.getCurrentUser().getUid());
            userListRef.setValue(mAuth.getCurrentUser().getUid());
            databaseEvents.child("events").child(EVENT.getId()).child("numRegister").setValue(EVENT.getNumRegister() + 1);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean cancelRegist () {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();

        try {
            DatabaseReference userListRef = databaseEvents.child("events").child(EVENT.getId()).child("userList").child(mAuth.getCurrentUser().getUid());
            userListRef.setValue(null);
            databaseEvents.child("events").child(EVENT.getId()).child("numRegister").setValue(EVENT.getNumRegister() - 1);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
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
