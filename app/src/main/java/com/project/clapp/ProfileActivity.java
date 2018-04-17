package com.project.clapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.User;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    String userID;
    TextView name, email, telemovel;
    ImageView img;
    RatingBar rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = new User();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
            System.out.println(userID);
            user = UserFirebaseManager.getInstance().getUser(userID);
        }

        if (user.getUserId() != null) {
            name = findViewById(R.id.nameProfileTxt);
            name.setText(user.getName().toString());

            email = findViewById(R.id.emailTxt);
            email.setText(user.getMail());

            telemovel = findViewById(R.id.telnumTxt);
            telemovel.setText(Integer.toString(user.getTele()));

            rt = findViewById(R.id.ratingBar);
            rt.setEnabled(false);

            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference userImg = mStorageRef.child("users").child(user.getImgURL());

            img = findViewById(R.id.imgProfileStuff);

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

                            img.setImageURI(Uri.fromFile(newFile));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }


            });

        }
    }

    public void getEvents(View view) {
        Intent intent = new Intent(ProfileActivity.this, EventListByUser.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}
