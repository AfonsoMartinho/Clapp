package com.project.clapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEvent extends AppCompatActivity implements NumberPicker.OnValueChangeListener {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private EditText nameTxt;
    private String duration = "";
    private String date = "";
    private String time = "";
    private int limit = 0;
    private String desc = "";
    private String name = "";
    private String local = "";
    private String userId = "";
    private double preco = 0;
    private double latitude = 0;
    private double longitude = 0;
    private Bitmap bmp;
    private String tags = "";
    private String imgURL = "";
    private StorageReference mStorageRef;
    private static final String TAG = "Check";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2, REQUEST_LOCATION = 3;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        nameTxt = findViewById(R.id.eventNameTxt);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d(TAG, "onDateSet: date: " + i + "/" + i1 + "/" + i2);
                date += i + "/" + i1 + "/" + i2;
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.d(TAG, "onTimeSet: time: " + i + ":" + i1);
                time += i + ":" + i1;
            }
        };
    }

    public void saveImage(Uri file) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgURL = name + file.getLastPathSegment();
        StorageReference eventImg = mStorageRef.child("events").child(name + file.getLastPathSegment());
        eventImg.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        imgURL = downloadUrl.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK) {
            if (requestCode==REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");

            } else if (requestCode==SELECT_FILE) {

            } else if (requestCode==REQUEST_LOCATION) {
                Bundle bundle = data.getExtras();
                local = bundle.get("address").toString();
                longitude = Double.parseDouble(bundle.get("longitude").toString());
                latitude = Double.parseDouble(bundle.get("latitude").toString());

            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void addDate(View view) {

        java.util.Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                CreateEvent.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void addTime(View view) {
        java.util.Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                CreateEvent.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeSetListener,
                hour, min, false
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    //Adding a Listener for the number picker (the one that stores de duration of the event)
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.d(TAG, "" + i1);
    }


    public void addDuration(View view) {
        final Dialog mDurationDialog = new Dialog(this);
        mDurationDialog.setTitle("Duration");
        mDurationDialog.setContentView(R.layout.dialog_add_duration);
        Button b1 = (Button) mDurationDialog.findViewById(R.id.confirmDurationBtn);
        Button b2 = (Button) mDurationDialog.findViewById(R.id.cancelDurationBtn);
        final NumberPicker np1 = (NumberPicker) mDurationDialog.findViewById(R.id.dayPicker);
        final NumberPicker np2 = (NumberPicker) mDurationDialog.findViewById(R.id.hoursPicker);
        final NumberPicker np3 = (NumberPicker) mDurationDialog.findViewById(R.id.minutePicker);

        np1.setMaxValue(100);
        np1.setMinValue(0);
        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(this);

        np2.setMaxValue(24);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);

        np3.setMaxValue(60);
        np3.setMinValue(0);
        np3.setWrapSelectorWheel(false);
        np3.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "" + np1.getValue());
                Log.d(TAG, "" + np2.getValue());
                Log.d(TAG, "" + np3.getValue());
                duration += np1.getValue() +"D"+np2.getValue()+"H"+np3.getValue()+"m";
                mDurationDialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDurationDialog.dismiss();
            }
        });
        mDurationDialog.show();
    }

    public void addLimit(View view) {
        final Dialog mLimitDialog = new Dialog(this);
        mLimitDialog.setTitle("Limit");
        mLimitDialog.setContentView(R.layout.dialog_add_limit);
        Button btnConfirm = (Button) mLimitDialog.findViewById(R.id.confirmLimitBtn);
        final EditText limitTxt = mLimitDialog.findViewById(R.id.limitTxt);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "" + limitTxt.getText());
                limit = Integer.parseInt(limitTxt.getText().toString());
                mLimitDialog.dismiss();
            }
        });
        mLimitDialog.show();
    }
    public void addTags(View view) {

        final CharSequence[] items = {"Workshop","Lecture","Documentary","Tutorial","Dinner","Fun Activity"};

        final ArrayList selectedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select the tags for the Event")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(indexSelected);
                        } else if (selectedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tags = "";
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        for (int i = 0; i < selectedItems.size(); i++) {
                            tags = tags + "," + selectedItems.get(i).toString();
                        }
                        tags = tags.substring(1);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }



    public void addDescription(View view) {
        final Dialog mDescriptionDialog = new Dialog(this);
        mDescriptionDialog.setTitle("Description");
        mDescriptionDialog.setContentView(R.layout.dialog_add_description);
        Button btnConfirm = mDescriptionDialog.findViewById(R.id.confirmDescBtn);
        final EditText descTxt = mDescriptionDialog.findViewById(R.id.descTxt);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "" + descTxt.getText());
                desc += descTxt.getText();
                mDescriptionDialog.dismiss();
            }
        });
        mDescriptionDialog.show();
    }

    public void addImage(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("images/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public void createEvent(View view) {

        name = nameTxt.getText().toString();
        saveImage(getImageUri(this, bmp));
        EventFirebaseManager efm = EventFirebaseManager.getInstance();
        efm.addEvent(
                name,
                date,
                time,
                local,
                duration,
                preco,
                desc,
                limit,
                userId,
                latitude,
                longitude,
                imgURL,
                tags);
    }

    //MAP

    public void addLocation(View view) {
        if(isServicesOK()) {
            initMap();
        }
    }

    private void initMap() {
        Intent intent = new Intent(CreateEvent.this, MapActivity.class);
        startActivityForResult(intent, REQUEST_LOCATION);
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int avaiable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CreateEvent.this);
        if(avaiable == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avaiable)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CreateEvent.this, avaiable, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


}
