package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherModal> weatherModalArrayList;

    public WeatherAdapter(Context context, ArrayList<WeatherModal> weatherModalArrayList) {
        this.context = context;
        this.weatherModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hinh_khi_hau,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherModal weatherModal=weatherModalArrayList.get(position);
        holder.tempTV.setText(weatherModal.getTemp()+"℃");
        holder.windSpeedTV.setText(weatherModal.getWindSpeed()+"km/h");
        holder.timeTV.setText(weatherModal.getTime());
        Picasso.get().load(weatherModal.getIcon()).into(holder.conditionIV);
    }

    @Override
    public int getItemCount() {
        return weatherModalArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView windSpeedTV,tempTV,timeTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windSpeedTV=itemView.findViewById(R.id.TVWindSpeed);
            tempTV=itemView.findViewById(R.id.TVTemp);
            timeTV=itemView.findViewById(R.id.TVTime);
            conditionIV=itemView.findViewById(R.id.IVCondition);
        }
    }
}
