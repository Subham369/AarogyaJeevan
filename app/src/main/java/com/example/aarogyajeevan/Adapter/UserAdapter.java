package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Fragment.ProfileFragment;
import com.example.aarogyajeevan.R;
import com.example.aarogyajeevan.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserInfo> mUser;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<UserInfo> mUser) {
        this.context = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final UserInfo user=mUser.get(position);
        holder.btn_follow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullname());
        Glide.with(context).load(user.getImageURLI()).into(holder.image_profile);

        isFollowing(user.getPhone_number(),holder.btn_follow);
        if (user.getPhone_number().equals(firebaseUser.getUid()))
        {
            holder.btn_follow.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getPhone_number());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_follow.getText().toString().equals("follow"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getPhone_number()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getPhone_number()).child("followers")
                            .child(firebaseUser.getUid()).setValue(true);
                    //addNotification(user.getId());
                }

                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getPhone_number()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getPhone_number()).child("followers")
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView fullname;
        CircleImageView image_profile;
        Button btn_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            fullname=itemView.findViewById(R.id.fullname);
            image_profile=itemView.findViewById(R.id.profile_image);
            btn_follow=itemView.findViewById(R.id.btn_follow);
        }
    }

    private void isFollowing(final String userid, final Button button)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists())
                {
                    button.setText("following");
                }
                else
                {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
