package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDetails extends AppCompatActivity {
    private TextView info,mForgotPassword;
    private FirebaseAuth firebaseAuth;
    private EditText mEmail,mPassword,mName;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        info=findViewById(R.id.info);
//        String place= getIntent().getStringExtra("places");
//        info.setText(place);
        firebaseAuth=FirebaseAuth.getInstance();
        mName=findViewById(R.id.edtmName);
        mEmail=findViewById(R.id.edtmEmail);
        mPassword=findViewById(R.id.edtmPassword);
        btnLogin=findViewById(R.id.btnLogin);
        mForgotPassword=findViewById(R.id.forgotPassword);
    }

    public void clkLogin(View view) {
        String name=mName.getText().toString();
        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            mName.setError("Enter your name");
            mName.requestFocus();
        }

        if (TextUtils.isEmpty(email)){
            mEmail.setError("Enter a valid phone number");
            mEmail.requestFocus();
        }

        if (TextUtils.isEmpty(password)||password.length()<8){
            mPassword.setError("Enter a valid password");
            mPassword.requestFocus();
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserDetails.this, "Verification Email hasbeen sent", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserDetails.this, "onFailure:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(UserDetails.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),AppFeatures.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDetails.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void clkSignUp(View view) {
        Intent intent=new Intent(getApplicationContext(),UserSignup.class);
        startActivity(intent);
        finish();
    }

    public void forgot_password(View view) {
        final EditText resetMail=new EditText(view.getContext());
        AlertDialog.Builder passwordReset=new AlertDialog.Builder(view.getContext());
        passwordReset.setTitle("Reset Password");
        passwordReset.setMessage("Enter your email to receive reset link");
        passwordReset.setView(resetMail);
        passwordReset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail=resetMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserDetails.this, "Reset link has been sent to the entered email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserDetails.this, "Error!! Reset link not send:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        passwordReset.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordReset.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(UserDetails.this, AppFeatures.class);
            startActivity(intent);
        }
    }
}
