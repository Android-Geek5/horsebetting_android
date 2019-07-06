package com.thrillingpicks.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thrillingpicks.R;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePaswordActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    String TAG = ChangePaswordActivity.class.getSimpleName();
    private ImageView changePasswordBackIv;
    private EditText oldPasswordEt;
    private EditText newPasswordEt;
    private EditText confirmPasswordEt;
    private Button changePasswordBtnSubmit;
    LinearLayout change_password_main;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pasword);
        //initialize views
        initView();
    }

    /*initialize activity views*/
    private void initView() {
        changePasswordBackIv = (ImageView) findViewById(R.id.change_password_back_iv);
        oldPasswordEt = (EditText) findViewById(R.id.old_password_et);
        newPasswordEt = (EditText) findViewById(R.id.new_password_et);
        confirmPasswordEt = (EditText) findViewById(R.id.confirm_password_et);
        changePasswordBtnSubmit = (Button) findViewById(R.id.change_password_btn_submit);
        change_password_main = findViewById(R.id.change_password_main);
        //call clickview method
        clickView();
        googleLogin();
    }

    /*initialize clickable views*/
    private void clickView() {
        changePasswordBackIv.setOnClickListener(this);
        changePasswordBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password_back_iv:
                //back press functionality
                onBackPressed();
                break;
            case R.id.change_password_btn_submit:
                //check validation for api hit
                checkValidation();
                break;
            case R.id.change_password_main:
                //hide keyboard on outside click
                hideKeyboard();
                break;
        }
    }

    private void checkValidation() {
        try {
            String old_password = "", new_password = "", confirm_password = "";
            //check oldpassword not null
            if (CommonUtils.validateForNull(this, oldPasswordEt, getString(R.string.enter_old_password))) {
                //check old password length greater then 6
//                if (CommonUtils.validPassword(this, oldPasswordEt, getString(R.string.enter_valid_old_password))) {
                old_password = CommonUtils.convertMD5(oldPasswordEt.getText().toString().trim());
                Log.e(TAG, "password convert MD5:--" + old_password);
                //check new password not null
                if (CommonUtils.validateForNull(this, newPasswordEt, getString(R.string.enter_new_password))) {
                    //check new password lenght is greater then 6
                    if (CommonUtils.validPassword(this, newPasswordEt, getString(R.string.enter_valid_new_password))) {
                        new_password = CommonUtils.convertMD5(newPasswordEt.getText().toString().trim());
                        Log.e(TAG, "password convert MD5:--" + new_password);
                        //check confirm password not null
                        if (CommonUtils.validateForNull(this, confirmPasswordEt, getString(R.string.enter_confirm_password))) {
                            //check confirm password length is greater then 6
                            if (CommonUtils.validPassword(this, confirmPasswordEt, getString(R.string.enter_valid_confirm_password))) {
                                //check new password or confirm password same
                                if (CommonUtils.PasswordMatch(this, newPasswordEt, confirmPasswordEt, getString(R.string.enter_password_must_be_same))) {
                                    confirm_password = CommonUtils.convertMD5(confirmPasswordEt.getText().toString().trim());
                                    Log.e(TAG, "password convert MD5:--" + confirm_password);
                                    //check internet connection
                                    if (CommonUtils.isConnectingToInternet(this)) {
                                        //hit change password api
                                        ChangePasswordApi(CommonVariables.TOKEN, old_password, new_password, confirm_password);
                                    } else {
                                        //show message in toast
                                        Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                                    }
                                }
//                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*back press functionality*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*hit Change password api*/
    public void ChangePasswordApi(String token, String old_password, String new_password, String confirm_password) {
        //initialize progrss dialog
        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        //show progress dialog
        pDialog.show();
        Call<JsonObject> changePasswordBeenCall;
        Log.e(TAG, "ChangePasswordApi---" + token + "--" + old_password + "--" + new_password + "--" + confirm_password + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            changePasswordBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .changePassword(CommonVariables.AUTHORIZATIONKEY, token, old_password, new_password, confirm_password);
            changePasswordBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e(TAG, "ChangePasswordApi--response---" + jsonObject);
                        //check code equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            Log.e(TAG, "ChangePasswordApi--response--1-" + jsonObject);
                            //disconnect google login
                            googleSignOut();
                            //disconnect facebook login
                            disconnectFromFacebook();
                            //show alert message in toast
                            Toast.makeText(ChangePaswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            /*clear Data to Shared Preferences*/
                            ThrillingPicksPrefrences.clearAllData("TPData", ChangePaswordActivity.this);
                            //go to login screen
                            Intent intent = new Intent(ChangePaswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //check code equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "ChangePasswordApi--response-0--" + jsonObject);
                            //show message in toast
                            Toast.makeText(ChangePaswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //check code equal to -1
                        else if (jsonObject.getString("code").equals("-1")) {
                            Log.e(TAG, "ChangePasswordApi--response-3--" + jsonObject);
                            //clear data base
                            ThrillingPicksPrefrences.clearAllData("TPData", ChangePaswordActivity.this);
                            //show session expire message in toast
                            Toast.makeText(ChangePaswordActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(ChangePaswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //dismiss progress dialog
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "ChangePasswordApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "ChangePasswordApi--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "ChangePasswordApi--error" + ex);
            ex.printStackTrace();
        }
    }

    /*hide keyboard outside touch*/
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

    /*-----google login----*/
    private void googleLogin() {
        try {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*google sign out*/
    private void googleSignOut() {
        try {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*face book sign out*/
    public void disconnectFromFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() == null) {
                return;        // already logged out
            }
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                    new GraphRequest
                            .Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            LoginManager.getInstance().logOut();
                        }
                    }).executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
