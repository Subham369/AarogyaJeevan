package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Fragment.ChatsFragment;
import com.example.aarogyajeevan.Fragment.ProfileChatsFragment;
import com.example.aarogyajeevan.Fragment.UsersFragment;
import com.example.aarogyajeevan.Model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference rootref;
    String groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo user=dataSnapshot.getValue(UserInfo.class);
                username.setText(user.getUsername());
                if (user.getImageURLW().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageURLW()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final TabLayout tabLayout=findViewById(R.id.tab_layout);
        final ViewPager viewPager=findViewById(R.id.view_pager);

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
                int unread=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if (unread==0){
                    viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
                }
                else
                {
                    viewPagerAdapter.addFragment(new ChatsFragment(),"("+unread+")CHATS");
                }
//                viewPagerAdapter.addFragment(new GroupFragment(),"GROUPS");
                viewPagerAdapter.addFragment(new UsersFragment(),"USERS");
                viewPagerAdapter.addFragment(new ProfileChatsFragment(),"PROFILE");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                startActivity(new Intent(ChattingActivity.this,CommunityPortalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.find_friends:
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;

            case R.id.create_group:
                Toast.makeText(this, "Create New Groups", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder group_builder=new AlertDialog.Builder(ChattingActivity.this,R.style.alert_dialog);
//                group_builder.setTitle("Enter the group name...");
//                final EditText group_name=new EditText(ChattingActivity.this);
//                group_name.setHint("e.g Team Prolific");
//                group_builder.setView(group_name);
//                group_builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String set_group_name=group_name.getText().toString();
//                        if (TextUtils.isEmpty(set_group_name))
//                            Toast.makeText(ChattingActivity.this, "Please enter the group name ...", Toast.LENGTH_SHORT).show();
//                            else{
//                                createGroup(set_group_name);
//                            }
//
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                group_builder.show();
                break;

            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChattingActivity.this,SettingsActivity.class));
                break;
        }
        return true;
    }

    private void createGroup(final String set_group_name) {
        groupId=firebaseUser.getUid();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("groupId",groupId);
        hashMap.put("groupname",set_group_name);
        hashMap.put("groupimg","default");
        rootref.child("GroupChat").child(groupId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChattingActivity.this, set_group_name+" is created succesfully...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChattingActivity.this, "Unable to create the group :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        rootref.child("Groups").child(set_group_name).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}


