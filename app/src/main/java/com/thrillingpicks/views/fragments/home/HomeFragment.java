package com.thrillingpicks.views.fragments.home;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thrillingpicks.R;
import com.thrillingpicks.model.TracksBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.adapters.HomeTabsPagerAdapter;


import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thrillingpicks.views.activities.home.HomeNavigationActivity.tomorrowTracklist;
import static com.thrillingpicks.views.activities.home.HomeNavigationActivity.todayTracklist;

/*
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    View myView;
    private TabLayout homeTabLayout;
    private ViewPager homeViewPager;
    private static String TAG = HomeFragment.class.getSimpleName();

    Dialog pDialog;
    public static int selectedPostion = 0;
    SwipeRefreshLayout swipee_tracks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        //method call to initialize views
        initView();
        return myView;
    }

    /*initialize views*/
    private void initView() {
        homeTabLayout = myView.findViewById(R.id.homeTabLayout);
        homeViewPager = myView.findViewById(R.id.homeViewPager);
        swipee_tracks = myView.findViewById(R.id.swipee_tracks);
        homeTabLayout.addTab(homeTabLayout.newTab().setText("Today"));
        homeTabLayout.addTab(homeTabLayout.newTab().setText("Tomorrow"));
        homeTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        homeTabLayout.setOnTabSelectedListener(this);
        //check internet conection validation
        if (CommonUtils.isConnectingToInternet(getActivity())) {
            //call method to hit track api
            TracksApi(CommonVariables.TOKEN);
        } else {
            //show message in toast
            Toast.makeText(getActivity(), getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }

        /*----Creating our pager adapter----*/
        swipee_tracks.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //check internet conection validation
                        if (CommonUtils.isConnectingToInternet(getActivity())) {
                            //call method to hit track api
                            TracksApi(CommonVariables.TOKEN);
                        } else {
                            //show message in toast
                            Toast.makeText(getActivity(), getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                            swipee_tracks.setRefreshing(false);
                        }
                    }
                }
        );
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectedPostion = tab.getPosition();
        homeViewPager.setCurrentItem(selectedPostion);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /*hit track api*/
    public void TracksApi(String token) {
        Call<TracksBeen> tracksBeenCall;
        Log.e(TAG, "TracksApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            tracksBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .tracks(CommonVariables.AUTHORIZATIONKEY, token);
            tracksBeenCall.enqueue(new Callback<TracksBeen>() {
                @Override
                public void onResponse(Call<TracksBeen> call, Response<TracksBeen> response) {
                    try {
                        //check code is equal to 1
                        if (response.body().getCode().equals("1")) {
                            //initialize arraylist
                            todayTracklist = new ArrayList<>();
                            tomorrowTracklist = new ArrayList<>();
                            Log.e(TAG, "TracksApi--response--tomorrowTracklist-" + new Gson().toJson(response.body().getData()));
                            //assign data to arraylist
                            tomorrowTracklist = response.body().getData().getTomorrow();
                            todayTracklist = response.body().getData().getToday();
                            Log.e(TAG, "TracksApi--response--tomorrowTracklist-" + tomorrowTracklist.size());
                            swipee_tracks.setRefreshing(false);
                            try {
                                HomeTabsPagerAdapter adapter = new HomeTabsPagerAdapter(getResources(), getChildFragmentManager(), homeTabLayout.getTabCount());

                                homeViewPager.setAdapter(adapter);
                                homeTabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                                homeTabLayout.setTabTextColors(Color.parseColor("#000000"), getActivity().getResources().getColor(R.color.tabSelectedColor));
                                homeTabLayout.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.tabSelectedColor));
                                homeViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                                        homeTabLayout));
                                homeViewPager.setCurrentItem(selectedPostion);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        //cehck code is equal to 0
                        else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "TracksApi--response-0--" + new Gson().toJson(response.body().getData()));
                            Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "TracksApi--response--1--" + new Gson().toJson(response.body().getData()));
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", getActivity());
                            //show session expire message in toast
                            Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login activity
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "TracksApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TracksBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "TracksApi-On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "error" + ex);
            ex.printStackTrace();
        }

    }

}
