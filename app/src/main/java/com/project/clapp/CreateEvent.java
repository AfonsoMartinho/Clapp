package com.project.clapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.project.clapp.impl.EventFirebaseManager;

import java.util.Calendar;

public class CreateEvent extends AppCompatActivity implements NumberPicker.OnValueChangeListener {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private EditText nameTxt;
    private String duration = "";
    private String date = "";
    private String time = "";
    private String limit = "";
    private String desc = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        nameTxt = findViewById(R.id.eventNameTxt);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d("create", "onDateSet: date: " + i + "/" + i1 + "/" + i2);
                date += i + "/" + i1 + "/" + i2;
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.d("create", "onTimeSet: time: " + i + ":" + i1);
                time += i + ":" + i1;
            }
        };
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
        Log.d("number", "" + i1);
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
                Log.d("days", "" + np1.getValue());
                Log.d("hours", "" + np2.getValue());
                Log.d("minutes", "" + np3.getValue());
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
                Log.d("limit", "" + limitTxt.getText());
                limit += limitTxt +"";
                mLimitDialog.dismiss();
            }
        });
        mLimitDialog.show();
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
                Log.d("description", "" + descTxt.getText());
                desc += descTxt.getText();
                mDescriptionDialog.dismiss();
            }
        });
        mDescriptionDialog.show();
    }

    public void createEvent(View view) {
        EventFirebaseManager efm = EventFirebaseManager.getInstance();
        efm.addEvent(nameTxt.getText().toString(),date,time,"local",duration,"price",desc,limit,"12221231");

    }
}
