package com.example.project.clapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.clapp.impl.EventFirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateEvent extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eventName;
    private EditText eventLocal;
    private EditText eventDate;
    private EditText eventTime;
    private EditText eventDur;
    private EditText eventPrice;
    private EditText eventDesc;
    private EditText eventCap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);



    }

    public void createEventFinal(View view) {
        mAuth = FirebaseAuth.getInstance();
        eventName = findViewById(R.id.nameEvent);
        eventDate = findViewById(R.id.dateEvent);
        eventTime = findViewById(R.id.timeEvent);
        eventLocal = findViewById(R.id.localEvent);
        eventDur = findViewById(R.id.durationEvent);
        eventPrice = findViewById(R.id.priceEvent);
        eventDesc = findViewById(R.id.descEvent);
        eventCap = findViewById(R.id.descEvent);
        String nameEvent = eventName.getText().toString();
        String dateEvent = eventDate.getText().toString();
        String timeEvent = eventTime.getText().toString();
        String localEvent = eventLocal.getText().toString();
        String durationEvent = eventDur.getText().toString();
        String priceEvent = eventPrice.getText().toString();
        String descEvent = eventDesc.getText().toString();
        String capEvent = eventCap.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        EventFirebaseManager efm = EventFirebaseManager.getInstance();
        efm.addEvent(nameEvent, dateEvent, timeEvent, localEvent, durationEvent, priceEvent, descEvent, capEvent, user.getUid());
    }
}
