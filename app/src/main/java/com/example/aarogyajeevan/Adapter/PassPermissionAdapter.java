package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.LocationHelper;
import com.example.aarogyajeevan.Model.EastDistrictProgress;
import com.example.aarogyajeevan.R;

import java.util.List;

public class PassPermissionAdapter extends RecyclerView.Adapter<PassPermissionAdapter.ViewHolder> {

    private Context mContext;
    private List<EastDistrictProgress> mUpload;
    public PassPermissionAdapter(Context context, List<EastDistrictProgress> uploads){
        mContext=context;
        mUpload=uploads;
    }
    @NonNull
    @Override
    public PassPermissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.candidate_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassPermissionAdapter.ViewHolder holder, int position) {
        EastDistrictProgress uploadCurrent=mUpload.get(position);
        holder.txt_cand_name.setText(String.valueOf(uploadCurrent.getTrans_name()));
        holder.txt_cand_purpose_details.setText(String.valueOf(uploadCurrent.getTrans_purpose_details()));
        holder.txt_cand_source.setText(String.valueOf(uploadCurrent.getTrans_source()));
        holder.txt_cand_destination.setText(String.valueOf(uploadCurrent.getTrans_destination()));
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cand_name,txt_cand_purpose_details,txt_cand_source,txt_cand_destination;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_cand_name=itemView.findViewById(R.id.txt_cand_name);
            txt_cand_purpose_details=itemView.findViewById(R.id.txt_cand_purpose_details);
            txt_cand_source=itemView.findViewById(R.id.txt_cand_source);
            txt_cand_destination=itemView.findViewById(R.id.txt_cand_destination);
        }
    }
}
