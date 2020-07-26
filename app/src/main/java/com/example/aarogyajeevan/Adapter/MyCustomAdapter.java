package com.example.aarogyajeevan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.AffectedCountries;
import com.example.aarogyajeevan.CountryModel;
import com.example.aarogyajeevan.R;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<CountryModel> {
    private Context context;
    private List<CountryModel> countryModelList;
    private List<CountryModel> countryModelListFilter;
    public MyCustomAdapter( Context context, List<CountryModel> countryModelList) {
        super(context, R.layout.list_custom_item,countryModelList);
        this.context=context;
        this.countryModelList=countryModelList;
        this.countryModelListFilter=countryModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.list_custom_item,null,true);
        ImageView imageView=view.findViewById(R.id.imgFlag);
        TextView tvcountryname=view.findViewById(R.id.tvcountryname);
        tvcountryname.setText(countryModelListFilter.get(position).getCountry());
        Glide.with(context).load(countryModelListFilter.get(position).getFlag()).into(imageView);

        return view;
    }

    @Override
    public int getCount() {
        return countryModelListFilter.size();
    }

    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelListFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter= new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if (constraint==null || constraint.length()==0){
                    filterResults.count=countryModelList.size();
                    filterResults.values=countryModelList;
                }
                else{
                    List<CountryModel> resultModel=new ArrayList<>();
                    String searchStr=constraint.toString().toLowerCase();
                    for (CountryModel itemModel:countryModelList){
                        if (itemModel.getCountry().toLowerCase().contains(searchStr)){
                            resultModel.add(itemModel);
                        }
                        filterResults.count=resultModel.size();
                        filterResults.values=resultModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryModelListFilter=(List<CountryModel>)results.values;
                AffectedCountries.countryModelsList=(List<CountryModel>)results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}


