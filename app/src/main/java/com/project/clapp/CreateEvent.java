package com.project.clapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.clapp.impl.EventFirebaseManager;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private String place = "";
    private String local = "";
    private String userId = "";
    private double preco = 0;
    private double latitude = 0;
    private double longitude = 0;
    private Bitmap bmp;
    private ArrayList<String> tags = new ArrayList<>();
    private String imgURL = "";
    private StorageReference mStorageRef;
    private static final String TAG = "Check";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2, PLACE_PICKER_REQUEST = 3;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 321;
    private FirebaseAuth mAuth;
    private ImageButton dateInput, timeInput, localInput, tagsInput, durationInput, priceInput, descInput, imgInput, limitInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        nameTxt = findViewById(R.id.eventNameTxt);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();




        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        durationInput = findViewById(R.id.durInput);
        tagsInput = findViewById(R.id.tagsInput);
        localInput = findViewById(R.id.localInput);
        priceInput = findViewById(R.id.priceInput);
        limitInput = findViewById(R.id.limitInput);
        imgInput = findViewById(R.id.imgInput);
        descInput = findViewById(R.id.descInput);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = "";
                Log.d(TAG, "onDateSet: date: " + i + "/" + i1 + "/" + i2);
                final CharSequence[] items = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Set", "Oct", "Nov", "Dec"};
                if (i1 != 0 || i2 != 0|| i != 0) {
                    dateInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                    date += items[i1] + " " + i2 + " " + i;
                } else {
                    dateInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.d(TAG, "onTimeSet: time: " + i + ":" + i1);
                if (i1 != 0 && i1 != 0) {
                    timeInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                    time += i + ":" + i1 + " UTC";
                } else {
                    timeInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
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
                System.out.println("gato");
                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                if (bmp != null) {
                    imgInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    imgInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }

            } else if (requestCode==SELECT_FILE) {
                System.out.println("cao");
                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                if (bmp != null) {
                    imgInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    imgInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }

            } else if (requestCode==PLACE_PICKER_REQUEST) {
                Place myPlace = PlacePicker.getPlace(data, this);
                local = myPlace.getAddress().toString();
                place = myPlace.getName().toString();
                longitude = myPlace.getLatLng().longitude;
                latitude = myPlace.getLatLng().latitude;
                if (!local.equals("")) {
                    localInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    localInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }

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
                if (!duration.equals("")) {
                    durationInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    durationInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
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
                if (limit != 0) {
                    limitInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    limitInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
            }
        });
        mLimitDialog.show();
    }
    public void addTags(View view) {

        final CharSequence[] items = {"Workshop","Lecture","Documentary","Tutorial","Dinner","Fun Activity"};

        final ArrayList selectedItems= new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select the tags for the Event")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(items[indexSelected]);
                        } else if (selectedItems.contains(items[indexSelected])) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(items[indexSelected]);
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tags.clear();
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        for (int i = 0; i < selectedItems.size(); i++) {
                            tags.add(selectedItems.get(i).toString());
                        }

                        if (!tags.isEmpty()) {
                            tagsInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                        }

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
                if (!desc.equals("")) {
                    descInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    descInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
            }
        });
        mDescriptionDialog.show();
    }

    public void addImage(View view) {
        int writeCheck = ContextCompat.checkSelfPermission(CreateEvent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readCheck = ContextCompat.checkSelfPermission(CreateEvent.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writeCheck != PackageManager.PERMISSION_GRANTED || readCheck != PackageManager.PERMISSION_GRANTED) {
            if (writeCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        CreateEvent.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
            if (readCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        CreateEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }

        } else {
            checkImage();
        }
    }

    public void addPrice(View view) {
        final Dialog mPriceDialog = new Dialog(this);
        mPriceDialog.setTitle("Price");
        mPriceDialog.setContentView(R.layout.dialog_price);
        Button b1 = mPriceDialog.findViewById(R.id.btnAddPrice);
        Button b2 = mPriceDialog.findViewById(R.id.btnCancelPrice);
        final NumberPicker np1 = mPriceDialog.findViewById(R.id.euroPicker);
        final NumberPicker np2 = mPriceDialog.findViewById(R.id.centPicker);

        np1.setMaxValue(600);
        np1.setMinValue(0);
        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(this);

        np2.setMaxValue(99);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "" + np1.getValue());
                Log.d(TAG, "" + np2.getValue());
                String price = np1.getValue() + "." + np2.getValue();
                preco = Double.parseDouble(price);
                if (preco == 0) {
                    priceInput.setBackgroundColor(Color.parseColor("#ffbb00"));
                } else {
                    priceInput.setBackgroundColor(Color.parseColor("#ffaa22"));
                }
                mPriceDialog.dismiss();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPriceDialog.dismiss();
            }
        });
        mPriceDialog.show();
    }

    public void checkImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("images/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void createDialogWarn(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("The event " + error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }})
                .create();
        dialog.show();
    }

    public boolean verifyData() {
        String str = date + " " + time;
        boolean timeSkip = false;
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz", Locale.ENGLISH);
        Date dateCR;
        Date currentDate = new Date();
        df.format(currentDate);
        try {
            dateCR = df.parse(str);
            if (dateCR.before(currentDate)) {
                timeSkip = true;
            }

        } catch (ParseException e) {
            System.out.println(e);
        }

        name = nameTxt.getText().toString();
        if (name.equals("")) {
            createDialogWarn("needs a name");
            return false;
        } else if (local.equals("")) {
            createDialogWarn("needs a location");
            return false;
        } else if (time.equals("")) {
            createDialogWarn("needs a time");
            return false;
        } else if (date.equals("")) {
            createDialogWarn("needs a date");
            return false;
        } else if (tags.isEmpty()) {
            createDialogWarn("needs some tags");
            return false;
        } else if (bmp==null) {
            createDialogWarn("needs an image");
            return false;
        } else if (timeSkip==true){
            createDialogWarn("cannot be created in the past");
            return false;
        } else {
            return true;
        }

    }


    public void createEvent(View view) {

            if(verifyData()) {
                saveImage(getImageUri(this, bmp));

                try {
                    EventFirebaseManager efm = EventFirebaseManager.getInstance();
                    efm.addEvent(
                            name,
                            date,
                            time,
                            place,
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

                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Event created with success")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(CreateEvent.this, LoadingActivity.class);
                                    startActivity(intent);
                                }})
                            .create();
                    Intent intent = new Intent(CreateEvent.this, LoadingActivity.class);
                    startActivity(intent);
                    dialog.show();

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
    }

    //MAP

    public void addLocation(View view) {
        if(isServicesOK()) {
            initMap();
        }
    }

    private void initMap() {
        //Intent intent = new Intent(CreateEvent.this, MapActivity.class);
        //startActivityForResult(intent, REQUEST_LOCATION);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    int readCheck = ContextCompat.checkSelfPermission(CreateEvent.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (readCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                CreateEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        checkImage();
                    }

                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkImage();
                }
                break;
            default:
                break;
        }
    }


}
