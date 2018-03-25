package com.project.clapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.project.clapp.clapp.R;
import com.project.clapp.models.Event;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by ruigo on 16/03/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;




    public EventAdapter(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = eventList.get(position);
        holder.nameEvent.setText(event.getName());
        holder.typeEvent.setText(event.getDescr());
        holder.creatorEvent.setText(event.getuID());
        holder.localEvent.setText(event.getLocal());
        holder.hourEvent.setText(event.getTime());
        new DownloadImageTask(holder.imageEvent)
                .execute(event.getImgURL());

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("idEvent", event.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView nameEvent, typeEvent, creatorEvent, localEvent, hourEvent;
        protected ImageView imageEvent;
        private Button btnView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameEvent = itemView.findViewById(R.id.nameEvent);
            typeEvent = itemView.findViewById(R.id.typeEvent);
            creatorEvent = itemView.findViewById(R.id.creatorEvent);
            localEvent = itemView.findViewById(R.id.localEvent);
            hourEvent = itemView.findViewById(R.id.hourEvent);
            imageEvent = itemView.findViewById(R.id.imgEvent);
            btnView = itemView.findViewById(R.id.btnView);

        }

    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
