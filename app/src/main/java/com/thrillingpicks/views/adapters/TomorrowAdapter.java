package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.TrackItemOnclick;
import com.thrillingpicks.model.TracksBeen;
import com.thrillingpicks.views.activities.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class TomorrowAdapter extends RecyclerView.Adapter<TomorrowAdapter.Myholder> {
    Context context;
    List<TracksBeen.Data.Tomorrow> tomorrowTracklist;
    TrackItemOnclick onclick;

    public TomorrowAdapter(Context context, List<TracksBeen.Data.Tomorrow> tomorrowTracklist, TrackItemOnclick onclick) {
        this.context = context;
        this.tomorrowTracklist = tomorrowTracklist;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public TomorrowAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_today_recyler, viewGroup, false);
        TomorrowAdapter.Myholder myholder = new TomorrowAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final TomorrowAdapter.Myholder myholder, final int i) {
        try {
            //set data in views
            myholder.palce_name_tv.setText(tomorrowTracklist.get(i).getTrackName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //on click listener
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onItemClick(v, i,tomorrowTracklist.get(i).getTrackRaceIsGuestAvailable());

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tomorrowTracklist.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView track_number_tv, palce_name_tv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views
            palce_name_tv = itemView.findViewById(R.id.palce_name_tv);
            track_number_tv = itemView.findViewById(R.id.track_number_tv);
        }
    }
}

