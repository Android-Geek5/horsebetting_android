package com.thrillingpicks.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thrillingpicks.BuildConfig;
import com.thrillingpicks.R;
import com.thrillingpicks.model.InvitedBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.fragments.home.HomeFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView inviteBackIv;
    private TextView inviteHeadingTv;
    private TextView promoCodeTv;
    private TextView inviteFriendsNowTv;
    private TextView numberOfAcceptedTv;
    private TextView friendsWhoSubscribedTv;
    private TextView inviteFriendsBackBtnTv;
    private String TAG = InviteFriendsActivity.class.getSimpleName();
    String referToAmonut = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        initView();
    }

    private void initView() {
        inviteBackIv = findViewById(R.id.invite_back_iv);
        inviteHeadingTv = findViewById(R.id.invite_heading_tv);
        promoCodeTv = findViewById(R.id.promo_code_tv);
        inviteFriendsNowTv = findViewById(R.id.invite_friends_now_tv);
        numberOfAcceptedTv = findViewById(R.id.number_of_accepted_tv);
        friendsWhoSubscribedTv = findViewById(R.id.friends_who_subscribed_tv);
        inviteFriendsBackBtnTv = findViewById(R.id.invite_friends_back_btn_tv);
        //check internet conection validation
        if (CommonUtils.isConnectingToInternet(this)) {
            //hit session  api
            invitedApiHit(CommonVariables.TOKEN, CommonVariables.AUTHORIZATIONKEY);
        } else {
            //show message in toast
            Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }
        clickView();
    }

    private void invitedApiHit(String token, String authorizationkey) {
        //initalize progress dialog
        final Dialog pDialog = new Dialog(InviteFriendsActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        pDialog.show();
        Call<InvitedBeen> InvitedBeenCall;
        Log.e(TAG, "invitedApiHit---" + token + "---" + authorizationkey);
        try {
            InvitedBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .getInvited(authorizationkey, token);
            InvitedBeenCall.enqueue(new Callback<InvitedBeen>() {
                @Override
                public void onResponse(Call<InvitedBeen> call, Response<InvitedBeen> response) {
                    try {
                        //check code equal to 1
                        if (response.body().getCode().equals("1")) {
                            Log.e(TAG, "invitedApiHit--response--1-" + new Gson().toJson(response.body().getData()));
                            referToAmonut = response.body().getData().getReferedToAmount();
                            String message =
                                    "Give your friends " + response.body().getData().getReferedToAmount() +
                                            "% off with  " +
                                            "your " +
                                            "promo code. " +
                                            "You'll" +
                                            " get " + response.body().getData().getReferedByAmount() + "%" +
                                            " in credits, after " +
                                            "they subscribe to a monthly " +
                                            "plan,";
                            inviteHeadingTv.setText("" + message);
                            numberOfAcceptedTv.setText("" + response.body().getData().getInvitedAccepted());
                            friendsWhoSubscribedTv.setText("" + response.body().getData().getWhoSubscribed());
                            promoCodeTv.setText("Your invite code: " + CommonVariables.PROMOCODE);
                            //check code is equal to 0
                        } else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "invitedApiHit--response-0--" + new Gson().toJson(response.body().getData()));
                            //show alert message in toast
                            Toast.makeText(InviteFriendsActivity.this, "" + response.body().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG,
                                    "invitedApiHit--response-: -1--" + new Gson().toJson(response.body().getData()));
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", InviteFriendsActivity.this);
                            //show session expire message in toast
                            Toast.makeText(InviteFriendsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(InviteFriendsActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show alert message in toast
                            Toast.makeText(InviteFriendsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "invitedApiHit--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<InvitedBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "invitedApiHit--On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "invitedApiHit--error" + ex);
        }
    }

    private void clickView() {
        inviteBackIv.setOnClickListener(this);
        inviteFriendsNowTv.setOnClickListener(this);
        inviteFriendsBackBtnTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_back_iv:
                onBackPressed();
                break;
            case R.id.invite_friends_now_tv:
                inviteFriends();
                break;
            case R.id.invite_friends_back_btn_tv:
                onBackPressed();
                break;
        }
    }

    /*invite friend*/
    public void inviteFriends() {
        try {
            Uri PromoCode = Uri.parse(CommonVariables.PROMOCODE);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Thrilling Picks App ");
            String shareMessage = "\nNeed amazing betting tips? \nUse my code: " + PromoCode + " \nto " +
                    "save yourself " + referToAmonut + "% off signup for Thrilling Picks.\n\n Get" +
                    " the" +
                    " app:\n" +
                    "https" +
                    "://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            Log.e(TAG, "inviteFriends---" + CommonVariables.PROMOCODE);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


