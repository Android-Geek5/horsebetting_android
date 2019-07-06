package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.model.TrackRacesBeen;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.DetailActivity;
import com.thrillingpicks.views.activities.PricingActivity;
import com.thrillingpicks.views.activities.signUpFlow.SplashActivity;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.Myholder> {
    Context context;
    List<TrackRacesBeen.Datum> tracklist;
    String userType;
    String TAG=DetailAdapter.class.getSimpleName();
    public DetailAdapter(Context context,List<TrackRacesBeen.Datum> tracklist,String userType) {
        this.context = context;
        this.tracklist = tracklist;
        this.userType = userType;

    }

    @NonNull
    @Override
    public DetailAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate view
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_detail_recyler, viewGroup, false);
        DetailAdapter.Myholder myholder = new DetailAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final DetailAdapter.Myholder myholder, int i) {

        myholder.race_number_tv.setText("Race #"+tracklist.get(i).getTrackRaceNumber());
        myholder.horse_detail_tv.setText("#"+tracklist.get(i).getTrackRaceHorseNumber()+" "+tracklist.get(i).getTrackRaceHorseName());

        myholder.desc_tv.setText(Html.fromHtml(tracklist.get(i).getTrackRaceDescription()));
        String date_time=tracklist.get(i).getTrackRaceDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        try {
            System.out.println(sdf2.format(sdf.parse(date_time)));
            String datetime=sdf2.format(sdf.parse(date_time));
            myholder.rate_estimate_tv.setText(" "+datetime+" EST");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"check user type----"+userType);
        //check end date not null
        if (isNullOrEmpty(userType)) {
            if(tracklist.get(i).getTrackRaceIsGuestAvailable().equals("yes")){
                myholder.guest_ll.setVisibility(View.VISIBLE);
                myholder.paid_ll.setVisibility(View.GONE);
            }else {
                myholder.guest_ll.setVisibility(View.GONE);
                myholder.paid_ll.setVisibility(View.VISIBLE);
            }
        } else {
            myholder.paid_ll.setVisibility(View.VISIBLE);

        }



//Race #
    }

    @Override
    public int getItemCount() {
        return tracklist.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView race_number_tv,desc_tv,rate_estimate_tv,horse_detail_tv;
LinearLayout paid_ll,guest_ll;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views
            race_number_tv = itemView.findViewById(R.id.race_number_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            rate_estimate_tv = itemView.findViewById(R.id.rate_estimate_tv);
            horse_detail_tv = itemView.findViewById(R.id.horse_detail_tv);
            paid_ll = itemView.findViewById(R.id.paid_ll);
            guest_ll = itemView.findViewById(R.id.guest_ll);

        }
    }
}
