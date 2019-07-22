package com.thrillingpicks.views.adapters;

import android.content.Context;
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
import com.thrillingpicks.interfaces.TrackItemOnclick;
import com.thrillingpicks.model.TracksBeen;

import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.Myholder> {
    String TAG = TodayAdapter.class.getSimpleName();
    Context context;
    List<TracksBeen.Data.Today> tracklist;
    TrackItemOnclick onclick;


    public TodayAdapter(Context context, List<TracksBeen.Data.Today> tracklist, TrackItemOnclick onclick) {
        this.context = context;
        this.tracklist = tracklist;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_today_recyler, viewGroup, false);
        Myholder myholder = new Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final Myholder myholder, final int i) {
        try {
            //set data in views
            myholder.palce_name_tv.setText(tracklist.get(i).getTrackName());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("TodayAdapter", "Id--" + tracklist.get(i).getId());
        //onclick listner
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onItemClick(v, i,tracklist.get(i).getTrackRaceIsGuestAvailable());
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tracklist.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView track_number_tv, palce_name_tv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initalize views
            palce_name_tv = itemView.findViewById(R.id.palce_name_tv);
            track_number_tv = itemView.findViewById(R.id.track_number_tv);
        }
    }
}
