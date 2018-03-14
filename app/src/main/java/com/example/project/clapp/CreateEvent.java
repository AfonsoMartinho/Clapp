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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);



    }

    public void createEventFinal(View view) {
        mAuth = FirebaseAuth.getInstance();
        eventName = findViewById(R.id.nameEvent);
        String event = eventName.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        EventFirebaseManager efm = EventFirebaseManager.getInstance();
        efm.addEvent(event, user.getUid());
    }
}
