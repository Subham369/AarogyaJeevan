package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aarogyajeevan.Model.HospitalCart;
import com.example.aarogyajeevan.ViewHolder.HospitalCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HospitalAdminUserProductActivity extends AppCompatActivity {


    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_admin_user_product);
        userID = getIntent().getStringExtra("uid");


        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("HospitalCartList").child("HospitalAdminView").child(userID).child("HospitalProducts");
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<HospitalCart> options =
                new FirebaseRecyclerOptions.Builder<HospitalCart>()
                        .setQuery(cartListRef, HospitalCart.class)
                        .build();

        FirebaseRecyclerAdapter<HospitalCart, HospitalCartViewHolder> adapter = new FirebaseRecyclerAdapter<HospitalCart, HospitalCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HospitalCartViewHolder holder, int position, @NonNull HospitalCart model)
            {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                holder.txtProductName.setText(model.getHname());
            }

            @NonNull
            @Override
            public HospitalCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                HospitalCartViewHolder holder = new HospitalCartViewHolder(view);
                return holder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}