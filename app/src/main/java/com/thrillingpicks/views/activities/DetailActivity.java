package com.thrillingpicks.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thrillingpicks.R;
import com.thrillingpicks.model.LogoutBeen;
import com.thrillingpicks.model.TrackRacesBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.adapters.DetailAdapter;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = DetailActivity.class.getSimpleName();
    RecyclerView detail_recycler;
    LinearLayout detail_back_ll;
    TextView detail_back_tv, detail_title_tv;
    LinearLayoutManager detailmanager;
    DetailAdapter detailAdapter;
    String race_Id;
    Dialog pDialog;
    TextView race_date_tv;
    List<TrackRacesBeen.Datum> tracklist;
    private String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //initialize viewa
        initView();
    }

    /*---initialize activity view--*/
    private void initView() {
        detail_recycler = findViewById(R.id.detail_recycler);
        detail_back_ll = findViewById(R.id.detail_back_ll);
        detail_back_tv = findViewById(R.id.detail_back_tv);
        detail_title_tv = findViewById(R.id.detail_title_tv);
        race_date_tv = findViewById(R.id.race_date_tv);

        //initialize progress dialog
        pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        //call clicview method
        clickView();
        //call getintentdata method
        getIntentData();
        //check internet connection
        if (CommonUtils.isConnectingToInternet(DetailActivity.this)) {
            /*hit track races api*/
            Log.e(TAG,"check date:--"+date);
            trackRacesAPi(CommonVariables.TOKEN, race_Id,date);
        } else {
            //show message in toast
            Toast.makeText(DetailActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }
    }

    /*--get data from intent--*/
    private void getIntentData() {
        try {
            //check intent not null
            if (getIntent().getExtras() != null) {
            String name = getIntent().getStringExtra("place");
            date = getIntent().getStringExtra("date");
            Log.e(TAG, "getIntentData--Id" + getIntent().getStringExtra("Id")+"--date--"+date);
            race_Id = getIntent().getStringExtra("Id");
            detail_title_tv.setText("" + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*--set adapter in recycler view--*/
    private void setAdapter() {
        String userType=checkUserType();
        detailmanager = new LinearLayoutManager(this);
        detail_recycler.setLayoutManager(detailmanager);
        detailAdapter = new DetailAdapter(this, tracklist,userType);
        detail_recycler.setAdapter(detailAdapter);
    }

    /*--clickable views--*/
    private void clickView() {
        detail_back_ll.setOnClickListener(this);
        detail_back_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_back_ll:
                //back press functionality
                onBackPressed();
                break;
            case R.id.detail_back_tv:
                // back press functionality
                onBackPressed();
                break;
        }
    }

    //back press functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*--hit trackRaces api--*/
    public void trackRacesAPi(final String token, final String race_id,String date) {

        pDialog.show();
        Call<TrackRacesBeen> trackRacesBeenCall;
        Log.e(TAG, "trackRacesAPi---" + token + "--" + race_id + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            trackRacesBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .trackRaces(CommonVariables.AUTHORIZATIONKEY, token, race_id,date);
            trackRacesBeenCall.enqueue(new Callback<TrackRacesBeen>() {
                @Override
                public void onResponse(Call<TrackRacesBeen> call, Response<TrackRacesBeen> response) {
                    try {
                        if (response.body().getCode().equals("1")) {
                            Log.e(TAG, "trackRacesAPi--response---" + new Gson().toJson(response.body().getData()));
                            //initialize track list
                            tracklist=new ArrayList<>();
                            //assign data to tracklist
                            tracklist=response.body().getData();
                            //show success message in toast
                            String race_date_time = response.body().getData().get(0).getTrackRaceDateTime();
                            Log.e(TAG, "date and time:---" + race_date_time);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE MMM dd yyyy");
                            System.out.println(sdf2.format(sdf.parse(race_date_time)));
                            //change date format
                            String datetime=sdf2.format(sdf.parse(race_date_time));
                            //set date in feild
                            race_date_tv.setText(""+datetime);
                            //call method to set data in recyclerview
                            setAdapter();
                        }
                        //check code equal to 0
                        else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "trackRacesAPi--response---" + new Gson().toJson(response.body().getData()));
                            //show alert message in toast
                            Toast.makeText(DetailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //check code equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "trackRacesAPi--response---" + new Gson().toJson(response.body().getData()));
                            /*for seeion expire */
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", DetailActivity.this);
                            //show session expire message in toast
                            Toast.makeText(DetailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show alert message in toast
                            Toast.makeText(DetailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //progress dialog dismiss
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "trackRacesAPi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TrackRacesBeen> call, Throwable t) {
                    Log.e(TAG, "trackRacesAPi--On failure" + t);
                    //hit get track error api
                    trackRacesAPiError(token, race_id);
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error" + ex);
            ex.printStackTrace();
        }

    }

    /*--hit trackRacesError api--*/
    public void trackRacesAPiError(String token, String race_id) {
        Call<LogoutBeen> trackRacesErrorBeenCall;
        Log.e(TAG, "trackRacesAPi---" + token + "--" + race_id + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            trackRacesErrorBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .trackRacesError(CommonVariables.AUTHORIZATIONKEY, token, race_id);
            trackRacesErrorBeenCall.enqueue(new Callback<LogoutBeen>() {
                @Override
                public void onResponse(Call<LogoutBeen> call, Response<LogoutBeen> response) {
                    try {
                        //show alert messaege in toast
                        Toast.makeText(DetailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //dismiss progress dialog
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, "trackRacesAPi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LogoutBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "trackRacesAPi--On failure" + t);

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error" + ex);
        }

    }
    public String checkUserType(){
        String user_Subscription_End_Date="";
        String data = ThrillingPicksPrefrences.getUserPreferences(DetailActivity.this, CommonUtils.USER);
        try {
            JSONObject json = new JSONObject(data);
            if (!json.optString("user_subscription_end_date").equals("")) {
                //get subscription end date
                user_Subscription_End_Date = json.getString("user_subscription_end_date");
                Log.e(TAG, "subscription date" + user_Subscription_End_Date);
            }else {
                Log.e(TAG, "subscription date null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //check end date not null
        if (isNullOrEmpty(user_Subscription_End_Date)) {

           return "";
        } else {
            return user_Subscription_End_Date;
        }

    }

}
