package com.example.seniorcitizensimulator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    private List<NameListModel> nameList;
    static String[] ColorsArray = new String []{"#b31302", "#b06402", "#24c704", "#0581b3", "#7905b3"};
    static int universal=0;

    public HomeRecyclerAdapter(List<NameListModel> nameList) {
        this.nameList = nameList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button name;

        MyViewHolder(View view) {

            super(view);
            name = view.findViewById(R.id.DoctorName);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recyclerview_design_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NameListModel movie = nameList.get(position);
        holder.name.setText(movie.getName());
        if(universal == 5)
            universal = 0;
        holder.name.setBackgroundColor(Color.parseColor(ColorsArray[universal]));
        universal++;
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
