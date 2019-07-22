package com.thrillingpicks.views.activities.signUpFlow;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.thrillingpicks.R;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.PricingActivity;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.fragments.home.HomeFragment;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thrillingpicks.utils.CommonUtils.compareDate;
import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;

public class SplashActivity extends AppCompatActivity {
    //declare variables
    String TAG = SplashActivity.class.getSimpleName();
    /*Timer declaration for Splash Screen*/
    private long SPLASH_TIME_OUT = 1500;
    LinearLayout splash_main_rl;
    TextView app_name;
    Dialog pDialog;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*inflate layout*/
        setContentView(R.layout.activity_splash);
        getCurrentDate();
        splash_main_rl = findViewById(R.id.splash_main_rl);
        app_name = findViewById(R.id.app_name);
        pDialog = new Dialog(SplashActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        Typeface Nebulous_content = Typeface.createFromAsset(getAssets(), "NebulousContent.otf");
        app_name.setTypeface(Nebulous_content, Typeface.ITALIC);
        HomeFragment.selectedPostion = 0;
        //get hash key
        getAppKeyHash();
        /*
         *update common fields
         * @ID,@Token,@User name ,@Email,@Profile image,@Dob
         * */
        ThrillingPicksPrefrences.updateCommonData(SplashActivity.this);
        /*method for next activity*/
        nextActivity();
    }


    /*go next Activity */
    private void nextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check internet connection validation
                if (CommonUtils.isConnectingToInternet(SplashActivity.this)) {
                    //check token not null
                    if (CommonUtils.isNullOrEmpty(CommonVariables.TOKEN)) {
                        Log.e(TAG, "check_flow--token empty");
                        //go to login type screen
                        Intent i = new Intent(SplashActivity.this, LoginTypeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Log.e(TAG, "check_flow--session api");
                        //hit social login api
                        sessionApi(CommonVariables.TOKEN);
                    }
                } else {
                    //show alert message in snackbar
                    snackbar = Snackbar
                            .make(splash_main_rl, getString(R.string.no_internet_string), Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sessionApi(CommonVariables.TOKEN);
                                }
                            });
                    snackbar.show();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     hit session api
     @param token
     */
    public void sessionApi(String token) {
        Call<SessionBeen> sessionBeenCall;
        Log.e(TAG, "SessionApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            sessionBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .sessionLogin(CommonVariables.AUTHORIZATIONKEY, token);
            sessionBeenCall.enqueue(new Callback<SessionBeen>() {
                @Override
                public void onResponse(Call<SessionBeen> call, Response<SessionBeen> response) {
                    try {
                        //check code is equal to 200
                        if (response.code() == 200) {
                            //check  code is equal to 1
                            if (response.body().getCode().equals("1")) {
                                Log.e(TAG, "session--response--1-" + new Gson().toJson(response.body().getData()));
                                //clear local data base
                                ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, SplashActivity.this);
                                //store data in local database
                                ThrillingPicksPrefrences.storeUserPreferences(SplashActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
                                //mehtod call to check plan sunbscription
                                checkSubscription();

                            } //check code is equals to 0
                            else if (response.body().getCode().equals("0")) {
                                Log.e(TAG, "session--response-0--" + new Gson().toJson(response.body().getData()));
                                //clear local data base
                                ThrillingPicksPrefrences.clearAllData("TPData", SplashActivity.this);
                                //go to login screen
                                ThrillingPicksPrefrences.clearAllData("TPData", SplashActivity.this);
                                //go to lgin type screen
                                Intent i = new Intent(SplashActivity.this, LoginTypeActivity.class);
                                startActivity(i);
                                finish();
                            }//check code is equals to -1
                            else if (response.body().getCode().equals("-1")) {
                                Log.e(TAG, "session--response--1--" + new Gson().toJson(response.body().getData()));
                                //clear local data base
                                ThrillingPicksPrefrences.clearAllData("TPData", SplashActivity.this);
                                //go to login type screen
                                Intent i = new Intent(SplashActivity.this, LoginTypeActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }//check response code is equal to 401
                        else if (response.code() == 401) {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, jsonObject.getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SessionBeen> call, Throwable t) {
                    Log.e(TAG, "On failure" + t);
                    nextActivity();
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "error" + ex);
        }

    }

    /*
     * check plan subscribe or not
     * */
    private void checkSubscription() {
        String user_Subscription_End_Date = null;
        //initialize local data in data
        String data = ThrillingPicksPrefrences.getUserPreferences(SplashActivity.this, CommonUtils.USER);
        try {
            JSONObject json = new JSONObject(data);
            Log.e(TAG, "SharedPreferenceData---" + json);
            if (!json.optString("user_subscription_end_date").equals("")) {
                //get subscription end date
                user_Subscription_End_Date = json.getString("user_subscription_end_date");
                Log.e(TAG, "subscription date" + user_Subscription_End_Date);
            } else {
                Log.e(TAG, "subscription date null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //check end date not null
        if (isNullOrEmpty(user_Subscription_End_Date)) {
            //go to pricing screen
            Intent i = new Intent(SplashActivity.this, PricingActivity.class);
            startActivity(i);
            finish();
        } else {
            //check subscritpion end date and current date is grater then or less then
            if (compareDate(user_Subscription_End_Date).equals("after")) {
                //go to pricing screen
                Intent i = new Intent(SplashActivity.this, PricingActivity.class);
                startActivity(i);
                finish();
            } else {
                //go to home screen
                Intent i = new Intent(SplashActivity.this, HomeNavigationActivity.class);
                startActivity(i);
                finish();
            }
        }

    }
    /*get key hash*/
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("HASH  " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {

            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    /*create MD5 authorization key*/
    private void convertMD5(String hashtext1) {
        try {
            String key = "0742c4383c7226f51a68c255e628367a" + hashtext1;
            Log.e(TAG, "convertMD5-----" + key);
            // Static getIn+stance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(key.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            Log.e(TAG, "MD5 key---" + hashtext);

            //set authorization key in common valiable
            CommonVariables.AUTHORIZATIONKEY = hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*get current date*/
    public void getCurrentDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Log.e(TAG, "Today date:--" + sdf.format(date));
            String currentdate = sdf.format(date);
            /*
             * convert date into MD5
             * */
            try {
                // Static getInstance method is called with hashing MD5
                MessageDigest md = MessageDigest.getInstance("MD5");
                // digest() method is called to calculate message digest
                //  of an input digest() return array of byte
                byte[] messageDigest = md.digest(currentdate.getBytes());
                // Convert byte array into signum representation
                BigInteger no = new BigInteger(1, messageDigest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                Log.e(TAG, "MD5text---" + hashtext);

                //mehtod call to generate authorization key
                convertMD5(hashtext);
            }

            // For specifying wrong message digest algorithms
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

