package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aarogyajeevan.Adapter.SliderAdapter;

public class OnBoardScreenActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;
    private Button prevBtn,nextBtn;
    private  int currentPage;
    private int f=0,f1=0,f2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_screen);

        mSlideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        mDotsLayout=(LinearLayout) findViewById(R.id.dotsLayout);
        prevBtn=findViewById(R.id.prevBtn);
        nextBtn=findViewById(R.id.nextBtn);
        sliderAdapter=new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(currentPage+1);
                f1=mSlideViewPager.getCurrentItem();
                if (f1==((dots.length)-1)){
                    f++;
                    if (f==2){
                        Intent intent=new Intent(OnBoardScreenActivity.this,UserDetails.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    f=0;
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(currentPage-1);
                f2=mSlideViewPager.getCurrentItem();
            }
        });
    }

    public void addDotsIndicator(int position){
        dots= new TextView[4];
        mDotsLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.color_Primary_Dark));
            mDotsLayout.addView(dots[i]);
        }

        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;

            if (position==0){
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                prevBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Next");
                prevBtn.setText("");
            }
            else if (position==((dots.length)-1)){
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Finish");
                prevBtn.setText("Back");
            }
            else
            {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Next");
                prevBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
