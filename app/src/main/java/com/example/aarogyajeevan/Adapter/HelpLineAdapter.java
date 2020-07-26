package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.Model.ContactList;
import com.example.aarogyajeevan.R;

import java.util.List;

public class HelpLineAdapter extends RecyclerView.Adapter<HelpLineAdapter.ImageViewerHolder>{

    private Context mContext;
    private List<ContactList> mUpload;

    public HelpLineAdapter(Context context,List<ContactList> uploads)
    {
        mContext=context;
        mUpload=uploads;
    }
    @NonNull
    @Override
    public ImageViewerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.help_line_item,parent,false);
        return new ImageViewerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewerHolder holder, int position) {

        ContactList uploadCurrent=mUpload.get(position);
        holder.textViewName.setText(uploadCurrent.getTextname());
        holder.textViewDescription.setText(uploadCurrent.getTextDescription());
        holder.textViewNumber.setText(uploadCurrent.getTextphone());

    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ImageViewerHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewNumber;
        public ImageViewerHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.telecomm);
            textViewDescription=itemView.findViewById(R.id.description);
            textViewNumber=itemView.findViewById(R.id.txt_number);
        }
    }
}


