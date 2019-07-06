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
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.PricingOnClick;
import com.thrillingpicks.model.SubscriptionsBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginTypeActivity;
import com.thrillingpicks.views.adapters.PricingAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;

public class PricingActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = PricingActivity.class.getSimpleName();
    private TextView pricingTitleTv;
    private RecyclerView pricingListRecyler;
    Button pricing_next_btn;
    LinearLayoutManager pricingManager;
    LinearLayout cancel_auto_renew_ll, pricing_cancel_auto_renew_ll;
    PricingAdapter pricingAdapter;
    List<SubscriptionsBeen.Datum> priceList;
    public static int selectedPosition = -1;
    String select_price = "", subs_package_name = "";
    ImageView pricing_back_iv;
    String Subscription_id = "";
    String GoTo = "";
    TextView auto_renewal_date;
    private String plan_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        //call method to initialize views
        initView();
    }

    /*initialize views*/
    private void initView() {
        pricingTitleTv = (TextView) findViewById(R.id.pricing_title_tv);
        pricingListRecyler = (RecyclerView) findViewById(R.id.pricing_list);
        pricing_next_btn = findViewById(R.id.pricing_next_btn);
        pricing_back_iv = findViewById(R.id.pricing_back_iv);
        cancel_auto_renew_ll = findViewById(R.id.cancel_auto_renew_ll);
        pricing_cancel_auto_renew_ll = findViewById(R.id.pricing_cancel_auto_renew_ll);
        auto_renewal_date = findViewById(R.id.auto_renewal_date);
        //call method to initialize clickable views
        clickViews();
        //get intent data
        getIntentData();
        priceList = new ArrayList<>();

        //check internet connection
        if (CommonUtils.isConnectingToInternet(PricingActivity.this)) {
            //call method to hit subscription api
            subscriptionApi(CommonVariables.TOKEN);
        } else {
            //show message in toast
            Toast.makeText(PricingActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }
    }
    /*get intent data */
    private void getIntentData() {
        try {
            Log.e(TAG, "go to next----" + getIntent().getStringExtra("From_activity"));
            //check intent not null
            if (getIntent().getExtras() != null) {
                //check previous activity flow
                if (getIntent().getStringExtra("From_activity").equals("SignUpActivity")) {
                    cancel_auto_renew_ll.setVisibility(View.GONE);
                    GoTo = "addnewCard";
                } else if (getIntent().getStringExtra("From_activity").equals("HomeNavigationActivity")) {
                    GoTo = "cardlist";
                    Log.i(TAG,"subscription type:---"+CommonVariables.SUBSCRIPTION_TYPE);
                    try{
                        if(CommonVariables.SUBSCRIPTION_TYPE.equals("manual")){
                            cancel_auto_renew_ll.setVisibility(View.GONE);
                        }else if (CommonVariables.SUBSCRIPTION_TYPE.equals("autorenewal")){
                            cancel_auto_renew_ll.setVisibility(View.VISIBLE);
                            auto_renewal_date.setText(CommonVariables.SUBSCRIPTION_END_DATE);
                        }else {
                            cancel_auto_renew_ll.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    cancel_auto_renew_ll.setVisibility(View.GONE);
                    GoTo = "addnewCard";
                }
            } else {
                cancel_auto_renew_ll.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*hit subsription api*/
    private void subscriptionApi(String token) {
        //initialize progress dialog
        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        //show progress dialog
        pDialog.show();
        Call<SubscriptionsBeen> aboutUserBeenCall;
        Log.e(TAG, "subscriptionApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            aboutUserBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .subscription(CommonVariables.AUTHORIZATIONKEY, token);
            aboutUserBeenCall.enqueue(new Callback<SubscriptionsBeen>() {
                @Override
                public void onResponse(Call<SubscriptionsBeen> call, Response<SubscriptionsBeen> response) {
                    try {
                        //check code equal to 1
                        if (response.body().getCode().equals("1")) {
                            Log.e(TAG, "subscriptionApi--response---" + new Gson().toJson(response.body().getData()));
                            //initialize pricelist arraylist
                            priceList = new ArrayList<>();
                            //assign data to pricelist
                            priceList = response.body().getData();
                            //check intent not null
                            if (getIntent().getExtras() != null) {
                                //chekc previous activity flow
                                if (getIntent().getStringExtra("From_activity").equals("HomeNavigationActivity")) {

                                } else {
                                    //set data in price list
                                    SubscriptionsBeen.Datum datum = new SubscriptionsBeen.Datum();
                                    datum.setSubscriptionName("Guest Pass");
                                    datum.setSubscriptionType("Try before you pay w/limited access, less picks");
                                    datum.setSubscriptionPrice("");
                                    datum.setSubscriptionValidity(0);
                                    datum.setId(-1);
                                    priceList.add(datum);
                                }
                            } else {
                                //set data in price list
                                SubscriptionsBeen.Datum datum = new SubscriptionsBeen.Datum();
                                datum.setSubscriptionName("Guest Pass");
                                datum.setSubscriptionType("Try before you pay w/limited access, less picks");
                                datum.setSubscriptionPrice("");
                                datum.setSubscriptionInterval("");
                                datum.setSubscriptionValidity(0);
                                datum.setId(-1);
                                priceList.add(datum);
                            }
                            //method call to set data in recylerview
                            loadRecyler();
                        }
                        //check code equal to 0
                        else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "subscriptionApi--response---" + new Gson().toJson(response.body().getData()));
                            //show alert message in toast
                            Toast.makeText(PricingActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //check code equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "subscriptionApi--response---" + new Gson().toJson(response.body().getData()));
                            //method call to clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", PricingActivity.this);
                            //show session expire message in toast
                            Toast.makeText(PricingActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(PricingActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //dismiss progress dialog
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "subscriptionApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SubscriptionsBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "subscriptionApi--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "subscriptionApi--error" + ex);
            ex.printStackTrace();
        }
    }

    /*set data in recycler */
    private void loadRecyler() {
        pricingManager = new LinearLayoutManager(this);
        pricingListRecyler.setLayoutManager(pricingManager);
        pricingAdapter = new PricingAdapter(this, priceList, new PricingOnClick() {
            @Override
            public void onItemClick(View view, int position, String subscriptionId) {
                Subscription_id = subscriptionId;
                plan_name = "";
                subs_package_name = priceList.get(position).getSubscriptionName();
                select_price = priceList.get(position).getSubscriptionPrice();
                //check string not null
                if (isNullOrEmpty(select_price)) {
                    plan_name = subs_package_name;
                } else {
                    plan_name = subs_package_name;
                }
                Log.e(TAG, "plan_name--" + select_price);
                Log.e(TAG, "Subscription_id--" + Subscription_id);
                selectedPosition = position;
                pricingAdapter.notifyDataSetChanged();

            }
        });
        pricingListRecyler.setAdapter(pricingAdapter);
    }

    /*initalize clickable views*/
    private void clickViews() {
        pricing_next_btn.setOnClickListener(this);
        pricing_back_iv.setOnClickListener(this);
        pricing_cancel_auto_renew_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pricing_next_btn:
                if (selectedPosition == -1) {
                    Toast.makeText(this, "Select Plan", Toast.LENGTH_SHORT).show();
                } else {
                    if (subs_package_name.equals("Guest Pass")) {
                        //go to home screen
                        Intent intent = new Intent(PricingActivity.this, HomeNavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
//                        if (GoTo.equals("cardlist")) {
                            //go to card list screen
                            Intent intent = new Intent(PricingActivity.this, CardListActivity.class);
                            intent.putExtra("activity", "PricingActivity");
                            intent.putExtra("price", select_price);
                            intent.putExtra("Subscription_id", Subscription_id);
                            startActivity(intent);
//                        } else {
                            //go to add new card screen
//                            Intent intent = new Intent(PricingActivity.this, AddNewCardActivity.class);
//                            intent.putExtra("activity", "1");
//                            intent.putExtra("Subscription_id", Subscription_id);
//                            intent.putExtra("PromoId", "");
//                            startActivity(intent);
//                        }
                    }
                }
                break;
            case R.id.pricing_back_iv:
                //back press functionality
                onBackPressed();
                break;
            case R.id.pricing_cancel_auto_renew_ll:
                startActivity(new Intent(PricingActivity.this,CancelSubscriptionActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedPosition = -1;
        try {
            if (priceList.size() != 0) {
                pricingAdapter.notifyDataSetChanged();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*back press functionality*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Log.e(TAG, "" + getIntent().getStringExtra("From_activity"));
            //check intent not null
            if (getIntent().getExtras() != null) {
                //check previous activity flow
                if (getIntent().getStringExtra("From_activity").equals("SignUpActivity")) {
                    selectedPosition = -1;
                    //go to login screen
                    Intent intent = new Intent(PricingActivity.this, LoginTypeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (getIntent().getStringExtra("From_activity").equals("HomeNavigationActivity")) {
                    finish();
                } else {
                    finish();
                }
            } else {
                selectedPosition = -1;
                //go to login type screen
                Intent intent = new Intent(PricingActivity.this, LoginTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
