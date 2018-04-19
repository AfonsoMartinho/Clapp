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
import android.view.KeyEvent;
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
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.Event;
import com.project.clapp.models.User;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    TextView nameInput, localInput, timeInput, dateInput, creatorInput, numRegister, priceInput, descInput, placeInput;
    ImageView imgView;
    int numOfReg;
    private StorageReference mStorageRef;
    LatLng latlng;
    String address, place;
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
        place = EVENT.getPlace();
        double longitude = EVENT.getLongitude();
        double latitude = EVENT.getLatitude();
        latlng = new LatLng(latitude, longitude);

        dateInput = findViewById(R.id.eventDateInput);
        dateInput.setText("Date: " + EVENT.getDate());

        timeInput = findViewById(R.id.eventTimeInput);
        timeInput.setText("Time: " + EVENT.getTime());

        placeInput = findViewById(R.id.eventPlaceInput);
        placeInput.setText(place);

        String numR = Integer.toString(EVENT.getNumRegister());
        String maxR = Integer.toString(EVENT.getMaxRegisters());

        numRegister = findViewById(R.id.eventNumInput);
        numOfReg = Integer.parseInt(numR);
        if (numR.equals(maxR) && !maxR.equals("0")) {
            numRegister.setText("FULL");
        } else {
            numRegister.setText(Integer.toString(numOfReg));
        }

        creatorInput = findViewById(R.id.eventCreatorInput);
        creatorInput.setText("Created by: " + UserFirebaseManager.getInstance().getUser(EVENT.getuID()).getName());

        if (EVENT.getUserList().contains(mAuth.getCurrentUser().getUid())) {
            join = true;
            button.setBackgroundColor(Color.parseColor("#ffbb00"));
            button.setText("Cancel Registration");
        } else if (Integer.parseInt(maxR)!= 0 && Integer.parseInt(maxR) == Integer.parseInt(numR)) {
            button.setEnabled(false);
            button.setVisibility(View.GONE);
        }

        priceInput = findViewById(R.id.eventPriceInput);
        if (EVENT.getPriceEvent() == 0) {
            priceInput.setText("Price: FREE!");
        } else {
            priceInput.setText("Price: " + Double.toString(EVENT.getPriceEvent()) + "€");
        }

        descInput = findViewById(R.id.eventDescInput);
        descInput.setText(EVENT.getDescr());


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

    public void viewUsers(View view) {
        Intent i = new Intent(EventActivity.this, UserListEvent.class);
        i.putExtra("eventID", EVENT.getId());
        startActivity(i);
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
                            button.setBackgroundColor(Color.parseColor("#273469"));
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

    public void getMap(View view) {
        Intent intent = new Intent(EventActivity.this, EventMap.class);
        intent.putExtra("id", EVENT.getId());
        startActivity(intent);
    }

    public boolean registerEvent () {


        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();

        try {
            DatabaseReference userListRef = databaseEvents.child("events").child(EVENT.getId()).child("userList").child(mAuth.getCurrentUser().getUid());
            userListRef.setValue(mAuth.getCurrentUser().getUid());
            EVENT.setNumRegister(EVENT.getNumRegister() + 1);
            databaseEvents.child("events").child(EVENT.getId()).child("numRegister").setValue(EVENT.getNumRegister());
            numRegister.setText(Integer.toString(EVENT.getNumRegister()));
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
            EVENT.setNumRegister(EVENT.getNumRegister() - 1);
            databaseEvents.child("events").child(EVENT.getId()).child("numRegister").setValue(EVENT.getNumRegister());
            numRegister.setText(Integer.toString(EVENT.getNumRegister()));
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        System.out.println("back press");
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(EventActivity.this, Home.class);
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
