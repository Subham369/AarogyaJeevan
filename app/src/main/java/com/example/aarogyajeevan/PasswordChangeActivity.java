package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class PasswordChangeActivity extends AppCompatActivity {

    private MaterialEditText new_password,confirm_password;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void newPasswordSubmit(View view) {
        String strnew_password=new_password.getText().toString();
        String strconfirm_password=confirm_password.getText().toString();
        if (strnew_password.compareTo(strconfirm_password)==0)
        uploadingPassword(strnew_password,strconfirm_password);
        else{
            Toast.makeText(this, "Password mismatch!! Please enter correct passwords", Toast.LENGTH_SHORT).show();
            new_password.setText("");
            confirm_password.setText("");
        }
    }

    private void uploadingPassword(String strnew_password, String strconfirm_password) {

//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
//
//        HashMap<String ,Object> hashMap=new HashMap<>();
//        hashMap.put("",userName);
//        hashMap.put("fullname",fullName);
//        hashMap.put("bio",bio);
//
//        reference.updateChildren(hashMap);
        Toast.makeText(this, "Password has been updated", Toast.LENGTH_SHORT).show();
        new_password.setText("");
        confirm_password.setText("");
    }
}
