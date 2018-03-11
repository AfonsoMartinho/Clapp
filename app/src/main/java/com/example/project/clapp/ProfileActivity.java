package com.example.project.clapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View view = getLayoutInflater().inflate(R.layout.listeventlayout, null);
        ImageView imageEvent=(ImageView)view.findViewById(R.id.imageView);
        TextView textName = (TextView)view.findViewById(R.id.textName);

        //imageEvent.setImageResource(R.drawable.luisfilipevieira);
    }
}
