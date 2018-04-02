package com.project.clapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.R;
import com.project.clapp.models.Event;

import org.parceler.ParcelClass;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by ruigo on 16/03/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;
    private StorageReference mStorageRef;



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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Event event = eventList.get(position);
        holder.nameEvent.setText(event.getName());
        holder.typeEvent.setText(event.getDescr());
        holder.creatorEvent.setText(event.getuID());
        holder.localEvent.setText(event.getLocal());
        holder.hourEvent.setText(event.getTime());

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference eventImg = mStorageRef.child("events").child(event.getImgURL());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File newFile = localFile;
        eventImg.getFile(newFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...

                        holder.imageEvent.setImageURI(Uri.fromFile(newFile));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }


        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("id", event.getId());
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


}
