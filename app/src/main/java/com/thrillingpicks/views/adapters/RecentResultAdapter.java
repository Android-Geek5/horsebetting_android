package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.model.RecentResultBeen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecentResultAdapter extends RecyclerView.Adapter<RecentResultAdapter.Myholder> {
    Context context;
    List<RecentResultBeen.Datum> recentResultList;

    public RecentResultAdapter(Context context,List<RecentResultBeen.Datum> recentResultList) {
        this.context = context;
        this.recentResultList = recentResultList;
    }

    @NonNull
    @Override
    public RecentResultAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_recent_result, viewGroup, false);
        RecentResultAdapter.Myholder myholder = new RecentResultAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final RecentResultAdapter.Myholder myholder, int i) {
        //set data in views
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(recentResultList.get(i).getDateOfResult());
            format = new SimpleDateFormat("MMM dd,yyyy");
            String date = format.format(newDate);
            myholder.recentRaceDateTv.setText(" "+date);
        } catch (ParseException e) {
            e.printStackTrace();
            myholder.recentRaceDateTv.setText(""+recentResultList.get(i).getDateOfResult());
        }
        myholder.recentRaceNameTv.setText(""+recentResultList.get(i).getTrackName());
        myholder.recentRaceId.setText(""+recentResultList.get(i).getTrackId());
        myholder.recentRaceBetTotalTv.setText("$"+recentResultList.get(i).getBetTotal());
        myholder.recentRaceBetType.setText(""+recentResultList.get(i).getBetType());
        myholder.recentRaceWinTv.setText("$"+recentResultList.get(i).getAmountWon());
        myholder.recentRaceSelectionTv.setText(Html.fromHtml(recentResultList.get(i).getSelection()));
        myholder.recentRaceDescTv.setText(Html.fromHtml(recentResultList.get(i).getRaceConditions()));
    }

    @Override
    public int getItemCount() {
        return recentResultList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        private TextView recentRaceDateTv;
        private TextView recentRaceNameTv;
        private TextView recentRaceId;
        private TextView recentRaceBetType;
        private TextView recentRaceSelectionTv;
        private TextView recentRaceBetTotalTv;
        private TextView recentRaceWinTv;
        private TextView recentRaceDescTv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initalize views
            recentRaceDateTv = (TextView) itemView.findViewById(R.id.recent_race_date_tv);
            recentRaceNameTv = (TextView) itemView.findViewById(R.id.recent_race_name_tv);
            recentRaceId = (TextView) itemView.findViewById(R.id.recent_race_id);
            recentRaceBetType = (TextView) itemView.findViewById(R.id.recent_race_bet_type);
            recentRaceSelectionTv = (TextView) itemView.findViewById(R.id.recent_race_selection_tv);
            recentRaceBetTotalTv = (TextView) itemView.findViewById(R.id.recent_race_bet_total_tv);
            recentRaceWinTv = (TextView) itemView.findViewById(R.id.recent_race_win_tv);
            recentRaceDescTv = (TextView) itemView.findViewById(R.id.recent_race_desc_tv);
        }
    }
}


