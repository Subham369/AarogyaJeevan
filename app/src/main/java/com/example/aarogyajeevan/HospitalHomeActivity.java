package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aarogyajeevan.Model.HospitalProducts;
import com.example.aarogyajeevan.Model.PrerelaventHospital;
import com.example.aarogyajeevan.ViewHolder.HospitalProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HospitalHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_home);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admins").toString();
        }


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("HospitalProducts");


        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!type.equals("Admins"))
                {
                    Intent intent = new Intent(HospitalHomeActivity.this, HospitalCartActivity.class);
                    startActivity(intent);
                }
            }
        });

//        if (type.equals("Admins")){
//            fab.setVisibility(View.GONE);
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("Admins"))
        {
            userNameTextView.setText(PrerelaventHospital.currentOnlineUser.getName());
            Picasso.get().load(PrerelaventHospital.currentOnlineUser.getImage()).placeholder(R.drawable.hospitalbooking).into(profileImageView);
        }


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<HospitalProducts> options =
                new FirebaseRecyclerOptions.Builder<HospitalProducts>()
                        .setQuery(ProductsRef, HospitalProducts.class)
                        .build();


        FirebaseRecyclerAdapter<HospitalProducts, HospitalProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<HospitalProducts, HospitalProductViewHolder>(options) {
                    @NonNull
                    @Override
                    public HospitalProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_product_item_layout, parent, false);
                        HospitalProductViewHolder holder = new HospitalProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull HospitalProductViewHolder holder, int position, @NonNull final HospitalProducts model)
                    {
                        holder.txtProductName.setText(model.getHname());
                        holder.txtProductDescription.setText("Total Bed:"+model.getTotalbed());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        holder.txtLocation.setText("Location:"+model.getHlocation());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admins"))
                                {
                                    Intent intent = new Intent(HospitalHomeActivity.this, HospitalAdminMaintainProducts.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HospitalHomeActivity.this, HospitalProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent=new Intent(HospitalHomeActivity.this,HospitalHomeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HospitalHomeActivity.this, HospitalCartActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_search)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HospitalHomeActivity.this, HospitalSearchProductsActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_booking)
        {
            Intent intent=new Intent(HospitalHomeActivity.this,UserBookingActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            if (!type.equals("Admins"))
            {
//                Intent intent = new Intent(HospitalHomeActivity.this, HospitalSettingsActivity.class);
//                startActivity(intent);
            }
        }
        else if (id == R.id.nav_logout)
        {
            if (!type.equals("Admins"))
            {
                Paper.book().destroy();

                Intent intent = new Intent(HospitalHomeActivity.this, HospitalBookingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
