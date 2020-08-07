package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HospitalAdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Tbed, Bprice, Hname, Hlocation,Hcontact,saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputHospitalName, InputTotalBed, InputBedPrice,InputHospitalLocation,InputHospitalContact;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_admin_add_new_product);
        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("HospitalProductImages");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("HospitalProducts");


        AddNewProductButton = (Button) findViewById(R.id.add_new_hospital);
        InputProductImage = (ImageView) findViewById(R.id.select_hospital_image);
        InputHospitalName = (EditText) findViewById(R.id.hospital_name);
        InputTotalBed = (EditText) findViewById(R.id.total_bed);
        InputBedPrice = (EditText) findViewById(R.id.bed_price);
        InputHospitalLocation=findViewById(R.id.hospital_location);
        InputHospitalContact=findViewById(R.id.hospital_contact);
        loadingBar = new ProgressDialog(this);
        Toast.makeText(this, "Welcome Admin...", Toast.LENGTH_SHORT).show();

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateHospitalData();
            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateHospitalData()
    {
        Tbed = InputTotalBed.getText().toString();
        Bprice = InputBedPrice.getText().toString();
        Hlocation = InputHospitalLocation.getText().toString();
        Hcontact=InputHospitalContact.getText().toString();
        Hname=InputHospitalName.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Hospital image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Tbed))
        {
            Toast.makeText(this, "Please Total Bed Number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Bprice))
        {
            Toast.makeText(this, "Please write bed price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Hname))
        {
            Toast.makeText(this, "Please write hospital name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Hlocation))
        {
            Toast.makeText(this, "Please write Hospital Location...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Hcontact))
        {
            Toast.makeText(this, "Please Write Contact Number...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreHospitalInformation();
        }
    }

    private void StoreHospitalInformation() {

        loadingBar.setTitle("Add New Hospital");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new Hospital.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(HospitalAdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(HospitalAdminAddNewProductActivity.this, "Hospital Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(HospitalAdminAddNewProductActivity.this, "got the Hospital image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveHospitalInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveHospitalInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("totalbed", Tbed);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Bprice);
        productMap.put("Hname", Hname);
        productMap.put("Hlocation",Hlocation);
        productMap.put("Hcontact",Hcontact);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(HospitalAdminAddNewProductActivity.this, HospitalAdminCatagoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(HospitalAdminAddNewProductActivity.this, "Hospital is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(HospitalAdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}