package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.DoctorDescActivity;
import com.example.aarogyajeevan.Model.Doctor;
import com.example.aarogyajeevan.Model.Patient;
import com.example.aarogyajeevan.R;
import com.example.aarogyajeevan.VideoCallingActivity;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    private Context mContext;
    private List<Doctor> mUpload;
    public DoctorListAdapter(Context context, List<Doctor> uploads){
        mContext=context;
        mUpload=uploads;
    }

    @NonNull
    @Override
    public DoctorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.doctor_info,parent,false);
        return new DoctorListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListAdapter.ViewHolder holder, int position) {

        final Doctor uploadCurrent=mUpload.get(position);
        holder.txt_doctor_name.setText(String.valueOf(uploadCurrent.getUserName()));
        holder.txt_doctor_specialization.setText(String.valueOf(uploadCurrent.getSpecialization()));
        holder.txt_doctor_hospital.setText(String.valueOf(uploadCurrent.getHospital()));
        holder.txt_doctor_time.setText(String.valueOf(uploadCurrent.getBookingtime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, DoctorDescActivity.class);
                intent.putExtra("userid",uploadCurrent.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_doctor_name,txt_doctor_specialization,txt_doctor_hospital,txt_doctor_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_doctor_name=itemView.findViewById(R.id.txt_doctor_name);
            txt_doctor_specialization=itemView.findViewById(R.id.txt_doctor_specialization);
            txt_doctor_hospital=itemView.findViewById(R.id.txt_doctor_hospital);
            txt_doctor_time=itemView.findViewById(R.id.txt_doctor_time);
        }
    }
}
