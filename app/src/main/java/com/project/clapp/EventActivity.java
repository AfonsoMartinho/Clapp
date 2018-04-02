package com.project.clapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.R;
import com.project.clapp.models.Event;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class EventActivity extends AppCompatActivity {
    TextView nameInput;
    DatabaseReference dataEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final String eventId = extras.getString("id");

            dataEvents = FirebaseDatabase.getInstance().getReference();
            DatabaseReference eventListRef = dataEvents.child("events").child(eventId);
            eventListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nome = dataSnapshot.child("name").getValue(String.class);
                    System.out.println(eventId + " " + nome);
                    nameInput = findViewById(R.id.eventNameInput);
                    nameInput.setText(nome);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
