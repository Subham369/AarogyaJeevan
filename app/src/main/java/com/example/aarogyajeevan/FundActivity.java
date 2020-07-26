package com.example.aarogyajeevan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FundActivity extends AppCompatActivity {

    Spinner upi_id;
    EditText amount,note ,name;
    Button pay;
    final int UPI_PAYMENT=0;
    private int item_position=0;
    private String text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);

        initalizeMethod();

        ArrayList<String> coname = new ArrayList<>();
        coname.add("Enter the amount to transfer money");
        coname.add("pmcares@sbi");
        coname.add("pmnrf@central");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, coname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        upi_id.setAdapter(arrayAdapter);

        upi_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upi_id.setSelection(position);
                text=parent.getItemAtPosition(position).toString();
                item_position=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                upi_id.requestFocus();

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount_txt=amount.getText().toString();
                String name_txt=name.getText().toString();
                String note_text=note.getText().toString();
                String upi_txt=text;

                payUsingUPI(amount_txt,note_text,name_txt,upi_txt);
            }
        });


        getSupportActionBar().setTitle("PM SUPPORT");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void payUsingUPI(String amount_txt, String note_text, String name_txt, String upi_txt) {

        Uri uri=Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",upi_txt)
                .appendQueryParameter("pn",name_txt)
                .appendQueryParameter("tn",note_text)
                .appendQueryParameter("pm",amount_txt)
                .appendQueryParameter("cu","INR").build();
        Intent upi_Payment=new Intent(Intent.ACTION_VIEW);
        upi_Payment.setData(uri);
        Intent chooser=Intent.createChooser(upi_Payment,"Pay With");
        if (chooser.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(chooser,UPI_PAYMENT);
        }
        else
        {
            Toast.makeText(this, "NO UPI app found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if (RESULT_OK == resultCode || requestCode == 11) {
                    if (data != null) {
                        String text = data.getStringExtra("Response");
                        Log.d("UPI", "onActivityResult: " + text);
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add("Nothing");
                        upiPaymentDataoperation(arrayList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add("Nothing");
                        upiPaymentDataoperation(arrayList);
                    }
                }
                else
                {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("Nothing");
                    upiPaymentDataoperation(arrayList);
                }
                break;
        }
    }

    private void upiPaymentDataoperation(ArrayList<String> arrayList) {
        if (isConnectionAvabilable(getApplicationContext()))
        {
            String str=arrayList.get(0);
            Log.d("UPI", "upiPaymentDataoperation: "+str);

            String paymentCancle="";
            if (str!=null)
            {
                str="discard";
            }
            String status="";
            String approvalRef="";
            String response[]=str.split("&");
            for (int i=0;i<response.length;i++)
            {
                String equalStr[]=response[i].split("m");
                if (equalStr.length>=2)
                {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                    {
                        status=equalStr[i].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("approval Ref".toLowerCase())
                            || equalStr[0].toLowerCase().equals("txnRef".toLowerCase()))
                    {
                        approvalRef=equalStr[1];
                    }
                }
                else
                {
                    paymentCancle="Payment Cancle by user";
                    if (status.equals("success"))
                    {
                        Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                        Log.d("UPI", "responcestr: "+approvalRef);

                    }

                    else if ("payment cancle by user".equals(paymentCancle))
                    {
                        Toast.makeText(this, "Payment cancel by user", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    private boolean isConnectionAvabilable(Context context) {

        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null && networkInfo.isConnected()&& networkInfo.isConnectedOrConnecting()
                    && networkInfo.isAvailable())
            {
                return true;
            }

        }
        return false;
    }

    private void initalizeMethod() {

        pay=findViewById(R.id.btn_pay);
        name=findViewById(R.id.name);
        amount=findViewById(R.id.amount);
        upi_id=findViewById(R.id.upi_id);
        note=findViewById(R.id.note);

    }
}

