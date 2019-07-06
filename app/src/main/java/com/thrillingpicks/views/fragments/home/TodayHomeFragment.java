package com.thrillingpicks.views.fragments.home;


import android.content.Intent;
import android.os.Bundle;
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
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.DetailActivity;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.adapters.TodayAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;
import static com.thrillingpicks.views.activities.home.HomeNavigationActivity.todayTracklist;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayHomeFragment extends Fragment {
    static String TAG = TodayHomeFragment.class.getSimpleName();
    View myView;
    RecyclerView today_recylerview;
    LinearLayoutManager todayrecyler_manager;
    TodayAdapter todayAdapter;
    ArrayList<String> placelist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_today_home, container, false);
        //method call to initialize views
        initView();
        return myView;
    }

    private void initView() {
        today_recylerview = myView.findViewById(R.id.today_recylerview);
        //method call to set data in recylerview
        setAdapter();
    }

    /*--set adapter in recylerview--*/
    private void setAdapter() {

        try {
            Log.e(TAG,"check size toadya"+ HomeNavigationActivity.todayTracklist);
                todayrecyler_manager = new LinearLayoutManager(getActivity());
                //set layout manager in recylerview
                today_recylerview.setLayoutManager(todayrecyler_manager);
                todayAdapter = new TodayAdapter(getActivity(), HomeNavigationActivity.todayTracklist, new TrackItemOnclick() {
                    @Override
                    public void onItemClick(View view, int position,String userType) {
//                        if (userType.equals("yes")) {
                        String date=CommonUtils.getTodayDate();
                        Log.e(TAG,"check today date");
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("place", HomeNavigationActivity.todayTracklist.get(position).getTrackName());
                            intent.putExtra("Id", String.valueOf(HomeNavigationActivity.todayTracklist.get(position).getId()));
                            intent.putExtra("date", date);
                            startActivity(intent);
//                        }else {
//                            Toast.makeText(getActivity(), "You Are Guest User", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                //set adapter in recylerview
                today_recylerview.setAdapter(todayAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String checkUserType() {
        String user_Subscription_End_Date = null;
        String data = ThrillingPicksPrefrences.getUserPreferences(getActivity(), CommonUtils.USER);
        try {
            JSONObject json = new JSONObject(data);
            Log.e(TAG, "SharedPreferenceData---" + json);
            if (json.optString("user_subscription_end_date").equals("")) {
                Log.e(TAG, "user_subscription_end_date---null");
            } else {
                user_Subscription_End_Date = json.getString("user_subscription_end_date");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isNullOrEmpty(user_Subscription_End_Date)) {
            Log.e(TAG, "check end date:--null");
            return "Guest";
        } else {
            Log.e(TAG, "check end date:--" + user_Subscription_End_Date);
            return "Paid";
        }
    }
}
