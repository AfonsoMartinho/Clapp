package com.example.project.clapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.clapp.impl.EventFirebaseManager;
import com.example.project.clapp.models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "FirebaseTest";
    RecyclerView rv;

    ArrayList<Event> EVENTS = new ArrayList<>();
    private EventAdapter eventAdapter;
    ListView listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        listEvents = findViewById(R.id.listEvents);

        Event event = new Event("fadas", "Rui", "asfa", "", "Aqui", "23/2/2019", "16:00", "2:00", "ola123", "", 0, 100, 0);
        EVENTS.add(event);


        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
        eventListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String id = ds.child("id").getValue(String.class);
                    String nome = ds.child("name").getValue(String.class);
                    String uID = ds.child("uID").getValue(String.class);
                    String imgURL = ds.child("imgURL").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String local = ds.child("local").getValue(String.class);
                    int maxR = ds.child("maxRegisters").getValue(int.class);
                    int numR = ds.child("numRegister").getValue(int.class);
                    String duration = ds.child("duration").getValue(String.class);
                    int price = ds.child("preco").getValue(int.class);
                    String descr = ds.child("descr").getValue(String.class);
                    String userList = ds.child("userList").getValue(String.class);

                    Event event = new Event(id, nome, uID, imgURL, local, date, time, duration, descr, userList, numR, maxR, price);
                    EVENTS.add(event);
                }
                eventAdapter = new EventAdapter(EVENTS);
                rv.setAdapter(eventAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_profile) {
                            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_myevents) {
                            Intent intent = new Intent(HomePageActivity.this, MyEvents.class);
                            startActivity(intent);
                        }

                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    @Override
    public Intent getIntent() {
        Intent intent = getIntent();
        FirebaseUser user = mAuth.getCurrentUser();
        return super.getIntent();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
