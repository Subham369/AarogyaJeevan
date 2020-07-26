package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.LocationHelper;
import com.example.aarogyajeevan.R;
import com.example.aarogyajeevan.UserInfo;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewerHolder>{

    private Context mContext;
    private List<LocationHelper> mUpload;

    public ImageAdapter(Context context, List<LocationHelper> uploads)
    {
        mContext=context;
        mUpload=uploads;
    }
    @NonNull
    @Override
    public ImageViewerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.custom_item,parent,false);
        return new ImageViewerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewerHolder holder, int position) {

        LocationHelper uploadCurrent=mUpload.get(position);
        holder.textViewLatitude.setText(String.valueOf(uploadCurrent.getLatitude()));
        holder.textViewLongitude.setText(String.valueOf(uploadCurrent.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ImageViewerHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewLatitude,textViewLongitude,textViewUserName;
        public ImageViewerHolder(@NonNull View itemView) {
            super(itemView);
            textViewLatitude=itemView.findViewById(R.id.tvlatitude);
            textViewLongitude=itemView.findViewById(R.id.tvlogitude);
        }
    }
}

