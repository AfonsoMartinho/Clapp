package com.project.clapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.util.ArrayList;

public class QRS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrs);
        init();

    }
    public void init() {
        Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanning cancelado.", Toast.LENGTH_LONG).show();
                System.out.println("cancel");
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                Event event = EventFirebaseManager.getInstance().getEvent(result.getContents());
                if (event.getId() == null) {
                    Toast.makeText(this, "This QR Code doesn't belong to an event", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(QRS.this, EventActivity.class);
                    intent.putExtra("id", result.getContents().toString());
                    startActivity(intent);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
