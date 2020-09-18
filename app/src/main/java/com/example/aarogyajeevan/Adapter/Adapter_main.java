package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aarogyajeevan.DetailedActivity;
import com.example.aarogyajeevan.Model.Articles;
import com.example.aarogyajeevan.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Adapter_main extends RecyclerView.Adapter<Adapter_main.ViewHolder> {

    Context context;
    List<Articles> articles;

    public Adapter_main(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnews,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Articles a=articles.get(position);
        holder.tvTitle.setText(a.getTitle());
        holder.tvSource.setText(a.getSource().getName());
//        holder.tvDate.setText("\u2022"+dateTime(a.getPublishedAt()));

        final String setImage= a.getUrlToImage();
        String url=a.getUrl();
        String descr=a.getContent();
        Picasso.get().load(setImage).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedActivity.class);
                intent.putExtra("title",a.getTitle());
                intent.putExtra("source",a.getSource().getName());
                intent.putExtra("imageUrl",a.getUrlToImage());
                intent.putExtra("url",a.getUrl());
                intent.putExtra("desc",a.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvSource;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvSource=itemView.findViewById(R.id.tvSource);
            imageView=itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.cardView);



        }
    }

//    private String  dateTime(String time)
//    {
//        PrettyTime prettyTime=new PrettyTime(new Locale(getCountry()));
//        String localTime=null;
//        try {
//            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd'T'hh-mm:",Locale.ENGLISH);
//            Date date=dateFormat.parse(time);
//            localTime=prettyTime.format(date);
//        }
//        catch (ParseException e)
//        {
//            e.printStackTrace();
//        }
//        return localTime;
//    }

    public String getCountry()
    {
        Locale locale=Locale.getDefault();

        String country=locale.getCountry();
        return country.toLowerCase();
    }
}

