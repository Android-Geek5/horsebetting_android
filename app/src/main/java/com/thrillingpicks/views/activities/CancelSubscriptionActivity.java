package com.thrillingpicks.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.thrillingpicks.R;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelSubscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = CancelSubscriptionActivity.class.getSimpleName();
    private LinearLayout cancelSubscriptionBackLl;
    private TextView cancel_subscription_date_tv,cancel_subscription_price_tv;
    private Button cancelSubscriptionButton;
    private Dialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_subscription);
        initviews();
    }

    private void initviews() {
        cancelSubscriptionBackLl = findViewById(R.id.cancel_subscription_back_ll);
        cancelSubscriptionButton = findViewById(R.id.cancel_subscription_button);
        cancel_subscription_date_tv = findViewById(R.id.cancel_subscription_date_tv);
        cancel_subscription_price_tv = findViewById(R.id.cancel_subscription_price_tv);
        try {
            cancel_subscription_date_tv.setText("on " + CommonVariables.SUBSCRIPTION_END_DATE);
            cancel_subscription_price_tv.setText("for $" + CommonVariables.SUBSCRIPTION_PRICE);
        }catch (Exception e){
            e.printStackTrace();
        }
        clickView();
    }

    private void clickView() {
        cancelSubscriptionBackLl.setOnClickListener(this);
        cancelSubscriptionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_subscription_back_ll:
                onBackPressed();
                break;
            case R.id.cancel_subscription_button:
                //check internet connection
                if (CommonUtils.isConnectingToInternet(CancelSubscriptionActivity.this)) {
                    //call method to hit cancel subscription api
                    cancelSubscriptionApi(CommonVariables.TOKEN);
                } else {
                    //show message in toast
                    Toast.makeText(CancelSubscriptionActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    /*hit cancel subscription api*/
    public void cancelSubscriptionApi(String token) {
        Call<JsonObject> cancelSubscriptionBeenCall;
        pDialog = new Dialog(CancelSubscriptionActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e(TAG, "cancelSubscriptionApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            cancelSubscriptionBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .cancelSubscription(CommonVariables.AUTHORIZATIONKEY, token);
            cancelSubscriptionBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e(TAG, "cancel api response---" + jsonObject);
                        //check code is equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            //show sucess alert message in toast
                            Toast.makeText(CancelSubscriptionActivity.this, "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                            //go to home screen
                            Intent intent = new Intent(CancelSubscriptionActivity.this, HomeNavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //check code equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "cancelSubscriptionApi--response--0-" + response.body());
                            //show alert message in toast
                            Toast.makeText(CancelSubscriptionActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "cancelSubscriptionApi--response--111-" + response.body());
                            //show alert message in toast
                            Toast.makeText(CancelSubscriptionActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //dismiss progress dialog
                            pDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "On failure" + t);
                    pDialog.dismiss();
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "error" + ex);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
