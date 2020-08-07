package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.ContactList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    EditText add_description,text_name,text_number;
    Button submit;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    int i=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        add_description=findViewById(R.id.add_description);
        text_name=findViewById(R.id.text_name);
        text_number=findViewById(R.id.text_number);
        submit=findViewById(R.id.submit);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description=add_description.getText().toString();
                String name=text_name.getText().toString();
                String number=text_number.getText().toString();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                ContactList helpAdderList=new ContactList(name,description,number);
                int value=i++;
                String pos=String.valueOf(value);
                FirebaseDatabase.getInstance().getReference("uploads").child(pos).setValue(helpAdderList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(AddActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddActivity.this, "Details not saved 1", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, "Details not saved 2", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

}
