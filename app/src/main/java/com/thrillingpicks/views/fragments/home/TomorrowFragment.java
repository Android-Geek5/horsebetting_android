package com.thrillingpicks.views.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.TrackItemOnclick;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.views.activities.DetailActivity;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.adapters.TomorrowAdapter;

public class TomorrowFragment extends Fragment {
    View myview;
    RecyclerView tomorrow_recyler;
    LinearLayoutManager tomorrow_manager;
    TomorrowAdapter todayAdapter;
    String TAG = TomorrowFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate layout for this fragment
        myview = inflater.inflate(R.layout.fragment_tomorrow, container, false);
        //method call to initialize views
        initView();
        return myview;
    }

    /*initialize views*/
    private void initView() {
        tomorrow_recyler = myview.findViewById(R.id.tomorrow_recyler);
        //call method to set data in recylerview
        setAdapter();
    }

    /*--set adapter in recylerview--*/
    private void setAdapter() {
        try {
            Log.e(TAG,"check size tomorrowTracklist"+ HomeNavigationActivity.tomorrowTracklist);
                tomorrow_manager = new LinearLayoutManager(getActivity());
                tomorrow_recyler.setLayoutManager(tomorrow_manager);
                todayAdapter = new TomorrowAdapter(getActivity(), HomeNavigationActivity.tomorrowTracklist, new TrackItemOnclick() {
                    @Override
                    public void onItemClick(View view, int position,String userType) {
                        String date=CommonUtils.getTomorrowDate();
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("place", HomeNavigationActivity.tomorrowTracklist.get(position).getTrackName());
                            intent.putExtra("Id", String.valueOf(HomeNavigationActivity.tomorrowTracklist.get(position).getId()));
                            intent.putExtra("date", date);
                            getActivity().startActivity(intent);
                    }
                });
                tomorrow_recyler.setAdapter(todayAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
