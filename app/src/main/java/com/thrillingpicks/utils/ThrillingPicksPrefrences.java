package com.thrillingpicks.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.thrillingpicks.utils.CommonVariables.CODEARRAY;

public class ThrillingPicksPrefrences {

    static String TAG = ThrillingPicksPrefrences.class.getSimpleName();

    // All the preferences saved are either String or boolean.Store String preferences here
    public static void storeUserPreferences(Context context, String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("TPData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
        updateCommonData(context);
    }

    // All the preferences saved are either String or boolean.Get String preferences here
    public static String getUserPreferences(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("TPData", Context.MODE_PRIVATE);
        SharedPreferences prefs = sharedpreferences;
        String string = prefs.getString(key, null);
        return string;
    }

    /*Clear All Data*/
    public static void clearAllData(String name, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().commit();
    }

    /*method for get Shared Preferences*/
    public static void updateCommonData(Context context) {
        String data = ThrillingPicksPrefrences.getUserPreferences(context, CommonUtils.USER);
        CODEARRAY = new JSONArray();
        try {
            if (data != null && !data.isEmpty()) {
                JSONObject json = new JSONObject(data);
                Log.e(TAG, "SharedPreferenceData---" + json);

                if (!json.optString("promocode").equals("")) {
                    CommonVariables.PROMOCODE = json.getString("promocode");
                    Log.e(TAG, "promo code ---" + json.getString("promocode"));
                } else {
                    Log.e(TAG, "prmocode null");
                    CommonVariables.PROMOCODE="";
                }
                if (!json.optString("id").equals("")) {
                    CommonVariables.ID = json.getString("id");
                    Log.e(TAG, "id --" + json.getString("id"));
                } else {
                    Log.e(TAG, "id null");
                    CommonVariables.ID="";
                }
                if (!json.optString("user_token").equals("")) {
                    CommonVariables.TOKEN = json.getString("user_token");
                    Log.e(TAG, "user_token --" + json.getString("user_token"));
                } else {
                    Log.e(TAG, "user_token null");
                    CommonVariables.TOKEN="";
                }
                if (!json.optString("name").equals("")) {
                    CommonVariables.NAME = json.getString("name");
                    Log.e(TAG, "name ---" + json.getString("name"));
                } else {
                    Log.e(TAG, "name null");
                    CommonVariables.NAME="";
                }
                if (!json.optString("subscription_price").equals("")) {
                    CommonVariables.SUBSCRIPTION_PRICE = json.getString("subscription_price");
                    Log.e(TAG, "name ---" + json.getString("subscription_price"));
                } else {
                    Log.e(TAG, "name null");
                    CommonVariables.SUBSCRIPTION_PRICE="";
                }
                if (!json.optString("user_subscription_type").equals("")) {
                    CommonVariables.SUBSCRIPTION_TYPE = json.getString("user_subscription_type");
                    Log.e(TAG, "user_subscription_type ---" + json.getString("user_subscription_type"));
                } else {
                    Log.e(TAG, "name null");
                    CommonVariables.SUBSCRIPTION_TYPE="";
                }
                if (!json.optString("email").equals("")) {
                    CommonVariables.EMAIL = json.getString("email");
                    Log.e(TAG, "email ----" + json.getString("email"));
                } else {
                    Log.e(TAG, "email null");
                    CommonVariables.EMAIL="";
                }
                if (!json.optString("refered_by_amount").equals("")) {
                    CommonVariables.REFERTOAMOUNT = json.getString("refered_by_amount");
                    Log.e(TAG, "refered_by_amount ----" + json.getString("refered_by_amount"));
                } else {
                    Log.e(TAG, "refer by amount null");
                    CommonVariables.REFERTOAMOUNT="";
                }
                if (!json.optString("user_subscription_end_date").equals("")) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date newDate = null;
                    try {
                        newDate = format.parse(json.getString("user_subscription_end_date"));
                        format = new SimpleDateFormat("MM/dd/yyyy");
                        String date = format.format(newDate);
                        CommonVariables.SUBSCRIPTION_END_DATE = date;
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                    Log.e(TAG, "email ----" + json.getString("user_subscription_end_date"));
                } else {
                    Log.e(TAG, "email null");
                }
                if (!json.optString("dob").equals("")) {
                    CommonVariables.DOB = json.getString("dob");
                    Log.e(TAG, "dob ----" + json.getString("dob"));
                } else {
                    CommonVariables.DOB="";
                    Log.e(TAG, "dob null");
                }
                if (!json.optString("user_profile_image_url").equals("")) {
                    CommonVariables.IMAGEURL = json.getString("user_profile_image_url");
                    Log.e(TAG, "user_profile_image_url ----" + json.getString("user_profile_image_url"));
                } else {
                    CommonVariables.IMAGEURL="";
                    Log.e(TAG, "user_profile_image_url null");
                }

                if (!json.optString("codes_array").equals("")) {
                    if (json.getJSONArray("codes_array").length() != 0) {
                        CODEARRAY = json.getJSONArray("codes_array");
                        Log.e(TAG, "codes_array ----" + json.getJSONArray("codes_array"));
                        Log.e(TAG, "CODEARRAY ----" + CODEARRAY);
                    } else {
                        Log.e(TAG, "codes_array --null--");
                    }
                } else {
                    Log.e(TAG, "user_profile_image_url null");
                }
            } else {
                Log.e(TAG, "sharedpref null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
