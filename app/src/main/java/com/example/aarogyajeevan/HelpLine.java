package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.aarogyajeevan.Adapter.HelpLineAdapter;
import com.example.aarogyajeevan.Model.ContactList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HelpLine extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HelpLineAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<ContactList> mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);

        getSupportActionBar().setTitle("Helpline Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HelpLine.this));
        mRecyclerView.setHasFixedSize(true);


        mUpload=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    ContactList upload=postSnapshot.getValue(ContactList.class);
                    mUpload.add(upload);
                }
                mAdapter=new HelpLineAdapter(HelpLine.this,mUpload);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HelpLine.this, "Error:"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

