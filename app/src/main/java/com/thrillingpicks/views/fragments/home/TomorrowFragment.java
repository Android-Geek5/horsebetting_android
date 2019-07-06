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
import android.widget.Toast;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.TrackItemOnclick;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.DetailActivity;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.adapters.TomorrowAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;
import static com.thrillingpicks.views.activities.home.HomeNavigationActivity.todayTracklist;
import static com.thrillingpicks.views.activities.home.HomeNavigationActivity.tomorrowTracklist;

public class TomorrowFragment extends Fragment {
    View myview;
    RecyclerView tomorrow_recyler;
    LinearLayoutManager tomorrow_manager;
    TomorrowAdapter todayAdapter;
    ArrayList<String> placelist;
    ArrayList<String> tracklist;
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
//                        if (userType.equals("yes")) {
                        String date=CommonUtils.getTomorrowDate();
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("place", HomeNavigationActivity.tomorrowTracklist.get(position).getTrackName());
                            intent.putExtra("Id", String.valueOf(HomeNavigationActivity.tomorrowTracklist.get(position).getId()));
                            intent.putExtra("date", date);
                            getActivity().startActivity(intent);
//                        }else {
//                            Toast.makeText(getActivity(), "You Are a Guest User", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                tomorrow_recyler.setAdapter(todayAdapter);

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
            if(json.optString("user_subscription_end_date").equals("")){
                Log.e(TAG, "user_subscription_end_date---null" );
            }else {
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
