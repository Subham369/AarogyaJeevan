package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.MessageActivity;
import com.example.aarogyajeevan.Model.EastDistrictProgress;
import com.example.aarogyajeevan.Model.Patient;
import com.example.aarogyajeevan.R;
import com.example.aarogyajeevan.VideoCallingActivity;

import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    private Context mContext;
    private List<Patient> mUpload;
    public PatientListAdapter(Context context, List<Patient> uploads){
        mContext=context;
        mUpload=uploads;
    }

    @NonNull
    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.patient_info,parent,false);
        return new PatientListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientListAdapter.ViewHolder holder, int position) {

        Patient uploadCurrent=mUpload.get(position);
        holder.txt_patient_name.setText(String.valueOf(uploadCurrent.getUserName()));
        holder.txt_patient_email.setText(String.valueOf(uploadCurrent.getEmail()));
        holder.txt_patient_phone.setText(String.valueOf(uploadCurrent.getPhone()));
        holder.txt_patient_address.setText(String.valueOf(uploadCurrent.getAddress()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, VideoCallingActivity.class);
                intent.putExtra("patientId",uploadCurrent.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_patient_name,txt_patient_email,txt_patient_phone,txt_patient_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_patient_name=itemView.findViewById(R.id.txt_patient_name);
            txt_patient_email=itemView.findViewById(R.id.txt_patient_email);
            txt_patient_phone=itemView.findViewById(R.id.txt_patient_phone);
            txt_patient_address=itemView.findViewById(R.id.txt_patient_address);
        }
    }
}
