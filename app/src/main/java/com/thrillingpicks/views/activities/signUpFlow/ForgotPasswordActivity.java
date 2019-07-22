package com.thrillingpicks.views.activities.signUpFlow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thrillingpicks.R;

import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    /*Views declaration for forgot Screen*/
    String TAG = ForgotPasswordActivity.class.getSimpleName();
    private ImageView forgotPasswordImgBack;
    private EditText forgotPasswordEtEmail;
    private Button forgotPasswordBtnSubmit;
    LinearLayout forgot_main_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*layout inflate*/
        setContentView(R.layout.activity_forgot_password);
        //method call to initialize views
        initView();
    }

    /*view initialization method calling*/
    private void initView() {
        forgotPasswordImgBack = findViewById(R.id.forgot_password_img_back);
        forgotPasswordEtEmail = findViewById(R.id.forgot_password_et_email);
        forgotPasswordBtnSubmit = findViewById(R.id.forgot_password_btn_submit);
        forgot_main_ll = findViewById(R.id.forgot_main_ll);
        /*Click listener method*/
        clickListenerMethod();
    }

    /*Click listener method*/
    private void clickListenerMethod() {
        forgotPasswordImgBack.setOnClickListener(this);
        forgotPasswordBtnSubmit.setOnClickListener(this);
        forgot_main_ll.setOnClickListener(this);
    }

    /*overrideClick listener method*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_password_img_back:
                //back press
                onBackPressed();
                break;
            case R.id.forgot_password_btn_submit:
                //check validation
                checkValidation();
                break;
            case R.id.forgot_main_ll:
                // hide keyboard on outside click
                hideKeyboard();
                break;
        }
    }

    /*check edittext validation*/
    private void checkValidation() {
        try {
            String email = "";
            //cehck email not null validation
            if (CommonUtils.validateForNull(this, forgotPasswordEtEmail, getString(R.string.enter_email_msg_string))) {
                //check email length
                if (CommonUtils.validateEmail(this, forgotPasswordEtEmail, getString(R.string.enter_valid_email_msg_string),  getString(R.string.enter_valid_email_msg_string))) {
                    email = forgotPasswordEtEmail.getText().toString().trim();
                    if (CommonUtils.isConnectingToInternet(this)) {
                        //forgot password api hit
                        ForgotPasswordApi(email);
                    } else {
                        //show messages in toast
                        Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*hide keyboard out side touch*/
    private void hideKeyboard() {
        try {
            InputMethodManager imm1 =
                    (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 == null) {
                view2 = new View(this);
            }
            imm1.hideSoftInputFromWindow(view2.getWindowToken(), 0);     //hide keyboard
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*forgot api hit*/
    public void ForgotPasswordApi(String email) {
        //initialize dialog
        final Dialog pDialog = new Dialog(ForgotPasswordActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        pDialog.show();
        Call<JsonObject> forgotPasswordBeenCall;
        Log.e(TAG, "ForgotPasswordApi---" + email + "----" + CommonVariables.AUTHORIZATIONKEY);
        try {
            forgotPasswordBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .forgotPassword(CommonVariables.AUTHORIZATIONKEY, email);
            forgotPasswordBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (response.code() == 200) {
                        //check code is equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            Log.e(TAG, "ForgotPasswordApi--response--1-" + jsonObject);
                            //clear Data to Shared Preferences
                            ThrillingPicksPrefrences.clearAllData("TPData", ForgotPasswordActivity.this);
                            //show sucess message in toast
                            Toast.makeText(ForgotPasswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //check code is equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "ForgotPasswordApi--response-0--" + jsonObject);
                            //show message in toast
                            Toast.makeText(ForgotPasswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (jsonObject.getString("code").equals("-1")) {
                            Log.e(TAG, "ForgotPasswordApi--response--1--" + jsonObject);
                            /*show message in toast for session expire*/
                            Toast.makeText(ForgotPasswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //clear local database
                            ThrillingPicksPrefrences.clearAllData("TPData", ForgotPasswordActivity.this);
                            //go to login screen
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show message in toast
                            Toast.makeText(ForgotPasswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //cancel progress dialog
                        pDialog.dismiss();
                        }else {
                            Log.e(TAG,"response code not equal to 200");
                            JSONObject jsonObject1 = new JSONObject(response.errorBody().string());
                            Log.e(TAG, jsonObject1.getString("message"));
                            Toast.makeText(ForgotPasswordActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "ForgotPasswordApi---Exception", e);
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "ForgotPasswordApi--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "ForgotPasswordApi---error" + ex);
            ex.printStackTrace();
        }
    }
}
