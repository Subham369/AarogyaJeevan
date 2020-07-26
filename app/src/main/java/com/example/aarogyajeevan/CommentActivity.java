package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Adapter.CommentAdapter;
import com.example.aarogyajeevan.Model.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comments> commentList;

    EditText add_comment;
    ImageView image_perofile;
    TextView post;

    String postid;
    String publisherid;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentList=new ArrayList<>();
        commentAdapter=new CommentAdapter(this,commentList,postid);
        recyclerView.setAdapter(commentAdapter);

        add_comment=findViewById(R.id.add_comment);
        image_perofile=findViewById(R.id.image_profile);
        post=findViewById(R.id.post);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_comment.getText().toString().equals(""))
                {
                    Toast.makeText(CommentActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    addComment();
                }
            }
        });

        getImage();
        readComment();

    }

    private void addComment()
    {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        String commentid=reference.push().getKey();
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("comment",add_comment.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());
        hashMap.put("commentid",commentid);
        reference.child(commentid).setValue(hashMap);
        add_comment.setText("");
    }



    private void getImage()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo user = dataSnapshot.getValue(UserInfo.class);
                Glide.with(getApplicationContext()).load(user.getImageURLI()).into(image_perofile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readComment()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Comments comments=snapshot.getValue(Comments.class);
                    commentList.add(comments);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

