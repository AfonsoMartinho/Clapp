package com.project.clapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.Event;
import com.project.clapp.models.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    ImageView iv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        User user = new User();




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, CreateEvent.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);



        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("Profile");
                Intent intent = new Intent(Home.this, ProfileActivity.class);
                intent.putExtra("userID", mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        iv = headerView.findViewById(R.id.imgNavBar);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        ArrayList<User> userList = UserFirebaseManager.getInstance().getUserList();
        for (int i = 0; i < userList.size(); i++) {
            if (userId.equals(userList.get(i).getUserId())) {
                user = userList.get(i);
                loadPic(headerView, user);
            }

        }



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String goTo = extras.getString("goto");
            if (goTo.equals("calendar")) {
                setTitle("Calendar");
                Calendar calendar = new Calendar();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, calendar).commit();
            } else if (goTo.equals("qrScan")) {
                setTitle("QR Scan");
                QRScanner QRS = new QRScanner();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, QRS).commit();
            } else if (goTo.equals("myEvents")) {
                setTitle("My Events");
                MyEvents myE = new MyEvents();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, myE).commit();
            } else if (goTo.equals("going")) {
                setTitle("My Events");
                Going going = new Going();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, going).commit();
            }

        } else {
            setTitle("Calendar");
            Calendar calendar = new Calendar();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, calendar).commit();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            setTitle("Calendar");
            Calendar calendar = new Calendar();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, calendar).commit();

        } else if (id == R.id.myEvents) {
            setTitle("My Events");
            MyEvents myEvents = new MyEvents();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, myEvents).commit();

        } else if (id == R.id.allEvents) {
            setTitle("All Events");
            AllEvents allEvents = new AllEvents();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, allEvents).commit();
        }else if (id == R.id.going) {
            setTitle("Going");
            Going goingEvents = new Going();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, goingEvents).commit();
        } else if (id == R.id.qrCode) {
            setTitle("Scanner");
            QRScanner qrScanner = new QRScanner();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, qrScanner).commit();
        } else if (id == R.id.geolocation) {
            setTitle("Near You");
            NearYouMap nearYou = new NearYouMap();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment, nearYou).commit();
        } else if (id == R.id.logout) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Do you wish to logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Home.this, Authentication.class);
                            startActivity(intent);
                        }})
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }})
                    .create();
            dialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadPic(View headerView, User user) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userImg = mStorageRef.child("users").child(user.getImgURL());

        tv = headerView.findViewById(R.id.nameNavBar);
        tv.setText(user.getName());
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File newFile = localFile;
        userImg.getFile(newFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...

                        iv.setImageURI(Uri.fromFile(newFile));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }


        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Do you wish to logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Home.this, Authentication.class);
                            startActivity(intent);
                        }})
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }})
                    .create();
            dialog.show();
            return false;

        }
        return super.onKeyDown(keyCode, event);
    }
}
