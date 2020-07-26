package com.example.aarogyajeevan.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Adapter.MyFotoAdapter;
import com.example.aarogyajeevan.EditProfileActivity;
import com.example.aarogyajeevan.Model.Post;
import com.example.aarogyajeevan.R;
import com.example.aarogyajeevan.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //this fragment have bugs

    ImageView image_profile,options;
    TextView posts, followers, following, fullname, bio, username;
    Button edit_profile;

    FirebaseUser firebaseUser;
    //Set<String> profileid;

    private RecyclerView recyclerView_save;
    private MyFotoAdapter myFotosAdapter_save;
    private List<Post> postList_save;

    private RecyclerView recyclerView;
    private MyFotoAdapter myFotosAdapter;
    private List<Post> postList;
    ImageButton my_fotos, saved_fotos;
    //String str_profileid;
    //String s;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        SharedPreferences prefs=getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
//        profileid=prefs.getStringSet("profileid", Collections.singleton("none"));
//        str_profileid=String.valueOf(profileid);
//        s=str_profileid.replaceAll("[^a-zA-Z0-9]", "");

        String data = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("profileid", "none");

        if (data.equals("none")) {
            profileId = firebaseUser.getUid();
//            profileId=getArguments().getString("phone_number");
        } else {
            profileId = data;
            getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().clear().apply();
        }

        image_profile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        edit_profile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.username);
        my_fotos = view.findViewById(R.id.my_fotos);
        saved_fotos = view.findViewById(R.id.saved_fotos);
        options = view.findViewById(R.id.options);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        postList=new ArrayList<>();
        myFotosAdapter=new MyFotoAdapter(getContext(),postList);
        recyclerView.setAdapter(myFotosAdapter);

        recyclerView_save=view.findViewById(R.id.recycler_view_save);
        recyclerView_save.setHasFixedSize(true);
        LinearLayoutManager layoutManager_save=new GridLayoutManager(getContext(),3);
        recyclerView_save.setLayoutManager(layoutManager_save);
        postList_save=new ArrayList<>();
        myFotosAdapter_save=new MyFotoAdapter(getContext(),postList_save);
        recyclerView_save.setAdapter(myFotosAdapter_save);



        userInfo();
        getFollowers();
        getNrPost();
        myFotos();
        mySaves();
        if (profileId.equals(firebaseUser.getUid()))
        {
            edit_profile.setText("Edit Profile");
        }
        else
        {
            checkFollow();
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn=edit_profile.getText().toString();
                if (btn.equals("Edit Profile"))
                {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                }
                else if (btn.equals("follow"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers")
                            .child(firebaseUser.getUid()).setValue(true);

                }
                else if (btn.equals("following"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers")
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_save.setVisibility(View.GONE);

        my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_save.setVisibility(View.GONE);
            }
        });

        saved_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerView_save.setVisibility(View.VISIBLE);
            }
        });
        return view ;
    }

//    private void addNotification() {
//
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(profileId);
//        HashMap<String, Object> map = new HashMap<>();
//
//        map.put("userid", firebaseUser.getUid());
//        map.put("text", "started following you");
//        map.put("postid", "");
//        map.put("isPost", false);
//
//        reference.setValue(map);
//    }

    private void userInfo()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("UserInfo").child(profileId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (getContext()==null)
                {
                    return;
                }
                UserInfo user=dataSnapshot.getValue(UserInfo.class);
                Glide.with(getContext()).load(user.getImageURLI()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists())
                {
                    edit_profile.setText("following");

                }
                else
                {
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileId).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                followers.setText(""+dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileId).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                following.setText(""+dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNrPost()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Post post=snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId))
                    {
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myFotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mySaves(){
        final List<String> savedIds = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    savedIds.add(snapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        postList_save.clear();

                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                            Post post = snapshot1.getValue(Post.class);

                            for (String id : savedIds) {
                                if (post.getPostId().equals(id)) {
                                    postList_save.add(post);
                                }
                            }
                        }

                        myFotosAdapter_save.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}