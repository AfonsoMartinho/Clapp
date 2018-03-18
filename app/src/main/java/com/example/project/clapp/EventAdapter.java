package com.example.project.clapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.clapp.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by ruigo on 16/03/2018.
 */

class EventAdapter extends ArrayAdapter<Event> {

    DatabaseReference dataEvents;

    public EventAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, R.layout.listeventlayout, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater eventInflater = LayoutInflater.from(getContext());
        View customView = eventInflater.inflate(R.layout.listeventlayout, parent, false);

        Event singleItem = getItem(position);
        TextView textName = customView.findViewById(R.id.textName);
        ImageView imageEvent = customView.findViewById(R.id.imageView);
        textName.setText(singleItem.getName());
        new DownloadImageTask(imageEvent)
                .execute(singleItem.getImgURL());

        return customView;

    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
