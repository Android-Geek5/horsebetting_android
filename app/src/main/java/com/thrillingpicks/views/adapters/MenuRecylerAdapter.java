package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.MenuRecylerOnClick;
import com.thrillingpicks.model.PicksBeen;
import com.thrillingpicks.views.activities.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuRecylerAdapter extends RecyclerView.Adapter<MenuRecylerAdapter.Myholder> {
    Context context;
    List<PicksBeen.Datum> recentWinsList;
    MenuRecylerOnClick onClick;

    public MenuRecylerAdapter(Context context, List<PicksBeen.Datum> recentWinsList, MenuRecylerOnClick onClick) {
        this.context = context;
        this.onClick = onClick;
        this.recentWinsList = recentWinsList;
    }

    @NonNull
    @Override
    public MenuRecylerAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate views
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_menu_recy, viewGroup, false);
        MenuRecylerAdapter.Myholder myholder = new MenuRecylerAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final MenuRecylerAdapter.Myholder myholder, final int i) {
        try {
            //set data in views
            myholder.track_name_tv.setText(recentWinsList.get(i).getTrackName());
            myholder.price_tv.setText(recentWinsList.get(i).getTrackRaceBetType() + " $" + recentWinsList.get(i).getTrackRacePaidAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //on clickview
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.itemClick(v, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentWinsList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView track_name_tv, price_tv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views
            price_tv = itemView.findViewById(R.id.price_tv);
            track_name_tv = itemView.findViewById(R.id.track_name_tv);
        }
    }
}

