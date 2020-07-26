package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.aarogyajeevan.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    public  int[] slider_images={R.drawable.viewpager1,R.drawable.viewpager2,R.drawable.viewpager3,R.drawable.viewpager4};
//    public  String[] slider_images={"4199_location_search.json","4199_location_search.json","4199_location_search.json","4199_location_search.json"};
    public String[] slider_headings={"Tracking","Online Councelling","Community","News Portal"};
    public String[] slider_descriptions={"We provide you our exact location and help you to notify you to stay out of hotspot region.",
            "Provide you online councelling from our best doctors along with health,fit tips. Available for 24x7.","Opening to a new community where you can volenteer and know about people who are involved in social organisation.",
            "Latest news related COVID-19, keeping you update and aware about the true facts."};

    @Override
    public int getCount() {
        return slider_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView sliderImageView=view.findViewById(R.id.slideImage);
        TextView sliderHeading=view.findViewById(R.id.slideHeading);
        TextView sliderDescription=view.findViewById(R.id.slideDescription);

        sliderImageView.setImageResource(slider_images[position]);
        sliderHeading.setText(slider_headings[position]);
        sliderDescription.setText(slider_descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
