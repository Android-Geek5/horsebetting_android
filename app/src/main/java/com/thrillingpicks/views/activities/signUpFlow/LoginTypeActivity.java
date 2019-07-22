package com.thrillingpicks.views.activities.signUpFlow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thrillingpicks.R;
import com.thrillingpicks.model.RecentResultBeen;
import com.thrillingpicks.model.RecentResultBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.CardListActivity;
import com.thrillingpicks.views.adapters.RecentResultAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTypeActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG=LoginTypeActivity.class.getSimpleName();
    private LinearLayout guestUser;
    private LinearLayout loginUser;
    TextView login_type_app_name;
    RecyclerView recent_result_recyler;
    private Dialog pDialog;
    LinearLayoutManager recentResultManager;
    RecentResultAdapter recentResultAdapter;
    List<RecentResultBeen.Datum> recentResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
        //initialize views
        initView();
    }

    /*initializ activity views*/
    private void initView() {
        pDialog = new Dialog(LoginTypeActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        guestUser =  findViewById(R.id.guest_user);
        loginUser =  findViewById(R.id.login_user);
        login_type_app_name =  findViewById(R.id.login_type_app_name);
        recent_result_recyler =  findViewById(R.id.recent_result_recyler);
        recentResultManager=new LinearLayoutManager(this);

        Typeface Nebulous_content = Typeface.createFromAsset(getAssets(), "NebulousContent.otf");
        login_type_app_name.setTypeface(Nebulous_content,Typeface.BOLD_ITALIC);
        if (CommonUtils.isConnectingToInternet(LoginTypeActivity.this)) {
            getWinningResult();
        }else {
            Toast.makeText(this,  ""+getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }
        //initalize clickable views
        clickView();
    }

    /*clickable views*/
    private void clickView() {
        guestUser.setOnClickListener(this);
        loginUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guest_user:
                LoginManager.getInstance().logOut();
                //go to signup screen
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.login_user:
                LoginManager.getInstance().logOut();
                //go to login screen
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
    /*hit get Result api*/
    public void getWinningResult() {
        pDialog.show();
        Call<RecentResultBeen> sessionBeenCall;
        Log.e(TAG, "getWinningResult--params--"+  CommonVariables.AUTHORIZATIONKEY);
        try {
            sessionBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .getWinning(CommonVariables.AUTHORIZATIONKEY);
            sessionBeenCall.enqueue(new Callback<RecentResultBeen>() {
                @Override
                public void onResponse(Call<RecentResultBeen> call, Response<RecentResultBeen> response) {
                    try {
                        //check code is equal to 200
                        if (response.code() == 200) {
                            //check  code is equal to 1
                            if (response.body().getCode().equals("1")) {
                                recentResultList=new ArrayList<>();
                                recentResultList=response.body().getData();
                                Log.e(TAG, "getWinningResult--response--1-" + new Gson().toJson(response.body().getData()));
                                setRecentResultAdapter();
                            } //check code is equals to 0
                            else if (response.body().getCode().equals("0")) {
                                Log.e(TAG, "getWinningResult--response-0--" + new Gson().toJson(response.body().getData()));

                            }//check code is equals to -1
                            else if (response.body().getCode().equals("-1")) {
                                Log.e(TAG, "getWinningResult--response--1--" + new Gson().toJson(response.body().getData()));
                            }
                            pDialog.dismiss();
                        }//check response code is equal to 401
                        else if (response.code() == 401) {
                            pDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, "getWinningResult-code 401--"+jsonObject.getString(
                                    "message"));
                        }else if (response.code() == 400) {
                            pDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG,"getWinningResult code--400--"+ jsonObject.getString(
                                    "message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "getWinningResult--Exception--", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RecentResultBeen> call, Throwable t) {
                    Log.e(TAG, "getWinningResult---On failure--" + t);
                    t.printStackTrace();
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "getWinningResult---error--" + ex);
        }

    }


    private void setRecentResultAdapter() {
        try {
            if (recentResultList.size() != 0) {
                recent_result_recyler.setLayoutManager(recentResultManager);
                recentResultAdapter=new RecentResultAdapter(LoginTypeActivity.this,
                        recentResultList);
                recent_result_recyler.setAdapter(recentResultAdapter);
            } else {
                Log.i(TAG, "setrecentadapter--list size equal to zero");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
