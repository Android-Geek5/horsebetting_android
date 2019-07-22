package com.thrillingpicks.views.activities.signUpFlow;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thrillingpicks.R;
import com.thrillingpicks.model.SignupBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.ChoosePlanActivity;
import com.thrillingpicks.views.activities.PricingActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    /*Views declaration for Login Screen*/
    String TAG = SignUpActivity.class.getSimpleName();
    private ImageView signUpImgBack;
    private EditText signUpEtFullName;
    private EditText signUpEtEmail;
    private EditText signUpEtPassword;
    private TextView signUpEtDob;
    private EditText signUpEtPromocode, sign_up_email_confirmation;
    private Button signUpBtn;
    private TextView signupTvLogin;
    private DatePicker datePicker;
    LinearLayout signup_main_ll;
    String date_of_birth = "";
    String mediaType = "", mediaId = "";
    private int dd, mm, yy, mmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*layout inflate*/
        setContentView(R.layout.activity_sign_up);
        /*view initialization method calling*/
        initView();
    }

    /*initView Method definition*/
    private void initView() {
        signUpImgBack = findViewById(R.id.sign_up_img_back);
        signUpEtFullName = findViewById(R.id.sign_up_et_full_name);
        signUpEtEmail = findViewById(R.id.sign_up_et_email);
        signUpEtPassword = findViewById(R.id.sign_up_et_password);
        signUpEtDob = findViewById(R.id.sign_up_et_dob);
        signUpEtPromocode = findViewById(R.id.sign_up_et_promocode);
        signUpBtn = findViewById(R.id.sign_up_btn);
        signupTvLogin = findViewById(R.id.signup_tv_login);
        sign_up_email_confirmation = findViewById(R.id.sign_up_email_confirmation);
        signup_main_ll = findViewById(R.id.signup_main_ll);
        //method call to get intent data
        getIntentData();
        /*Click listener method*/
        clickListenerMethod();
        Calendar calendar = Calendar.getInstance();
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        mmm = calendar.get(Calendar.MONTH);
        yy = calendar.get(Calendar.YEAR);
    }

    /*get data from intent
     *
     * email,name,social id, social type.
     * */
    private void getIntentData() {
        try {
            Bundle extras = getIntent().getExtras();
            //check intent not null
            if (extras != null) {
                Log.e(TAG, "getIntentData---1-" + getIntent().getStringExtra("email"));
                signUpEtEmail.setText(extras.getString("email"));
                signUpEtFullName.setText(extras.getString("name"));
                mediaId = extras.getString("socialID");
                mediaType = extras.getString("socialType");
                if(!CommonUtils.isNullOrEmpty(extras.getString("email"))){
                    signUpEtEmail.setEnabled(false);
                }

            } else {
                Log.e(TAG, "getIntentData--12--" + getIntent().getStringExtra("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Click listener method*/
    private void clickListenerMethod() {
        signUpImgBack.setOnClickListener(this);
        signupTvLogin.setOnClickListener(this);
        signUpEtDob.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        signup_main_ll.setOnClickListener(this);
    }

    /*overrideClick listener method*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_img_back:
                //perform back press functionality
                onBackPressed();
                break;
            case R.id.signup_tv_login:
                LoginManager.getInstance().logOut();
                //go to login screen
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.sign_up_et_dob:
                //open date picker dialog
                selectDateofBirth();
                break;
            case R.id.sign_up_btn:
                //check feilds validation
                checkValidation();
                break;
            case R.id.signup_main_ll:
                //hide keyboard on out side cick
                hideKeyboard();
                break;
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

    /*check feild validation*/
    private void checkValidation() {
        try {
            String name = "", email = "", password = "", dob = "", promocode = "", email_confirmation = "";
            //check name feild not null validation
            if (CommonUtils.validateForNull(this, signUpEtFullName, getString(R.string.enter_name))) {
                //check name pattren validation
                if (CommonUtils.checkNameValidation(SignUpActivity.this, signUpEtFullName, getString(R.string.enter_valid_name))) {
                    name = signUpEtFullName.getText().toString().trim();
                    //check email not null validation
                    if (CommonUtils.validateForNull(this, signUpEtEmail, getString(R.string.enter_email_msg_string))) {
                        //check email pattren validation
                        if (CommonUtils.validateEmail(this, signUpEtEmail, getString(R.string.enter_valid_email_msg_string), getString(R.string.enter_valid_email_msg_string))) {
                            email = signUpEtEmail.getText().toString().trim();
                            //check confirm email not null validation
                            if (CommonUtils.validateForNull(this, sign_up_email_confirmation, getString(R.string.enter_confirm_email))) {
                                //check confirm email pattren validation
                                if (CommonUtils.validateEmail(this, sign_up_email_confirmation, getString(R.string.enter_valid_email_msg_string), getString(R.string.enter_valid_email_msg_string))) {
                                    //check email or confirm email equal validation
                                    if (CommonUtils.EmailMatch(this, signUpEtEmail, sign_up_email_confirmation, getString(R.string.email_and_confirm_email_same))) {
                                        email_confirmation = sign_up_email_confirmation.getText().toString().trim();
                                        //check password not null validaton
                                        if (CommonUtils.validateForNull(this, signUpEtPassword, getString(R.string.enter_password))) {
                                            //check password length is greter then 6
                                            if (CommonUtils.validPassword(this, signUpEtPassword, getString(R.string.enter_valid_password))) {
                                                password = CommonUtils.convertMD5(signUpEtPassword.getText().toString().trim());
                                                Log.e(TAG, "password convert MD5:--" + password);
                                                //check date of birth not null validation
                                                if (!signUpEtDob.getText().toString().matches("")) {
                                                    dob = signUpEtDob.getText().toString().trim();
                                                    promocode = signUpEtPromocode.getText().toString().trim();
                                                    //check internet connection
                                                    if (CommonUtils.isConnectingToInternet(this)) {
                                                        //method call to hit signup api
                                                        signUpApi(name, email, email_confirmation, password, date_of_birth, promocode, mediaId, mediaType);
                                                    } else {
                                                        //show message in toast
                                                        Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    //show message in toast
                                                    Toast.makeText(this, getString(R.string.enter_dob), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*--date picker--*/
    private void selectDateofBirth() {
        try {
            final View datepickerView = getLayoutInflater().inflate(R.layout.date_picker, null);
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
            //set title
            alert.setTitle(getString(R.string.set_dob_string));
            // this is set the view from XML inside AlertDialog
            alert.setView(datepickerView);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            datePicker = (DatePicker) datepickerView.findViewById(R.id.datepicker);
            //set date picker maximum select date
            try {
                datePicker.updateDate(yy, mmm, dd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //negative button
            alert.setNegativeButton(getString(R.string.cancel_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            //positive button
            alert.setPositiveButton(getString(R.string.set_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dd = datePicker.getDayOfMonth();

                    mm = datePicker.getMonth() + 1;
                    mmm = datePicker.getMonth();
                    yy = datePicker.getYear();
                    String dateofmonth, monthofyear;
                    dateofmonth = String.valueOf(dd);
                    monthofyear = String.valueOf(mm);
                    //check date is less then 10
                    if (dd < 10) {
                        dateofmonth = "0" + dd;
                        dd = Integer.parseInt(dateofmonth);
                    } else {

                    }
                    //check month is less then 10
                    if (mm < 10) {
                        monthofyear = "0" + mm;
                        mm = Integer.parseInt(monthofyear);
                    }
                    String dob = monthofyear + "-" + dateofmonth + "-" + yy;
                    Log.e("check_date_datepicker", "" + dob);
                    //set date in date of birth text view
                    date_of_birth = yy + "-" + monthofyear + "-" + dateofmonth;
                    signUpEtDob.setText(dob);
                }
            });
            android.app.AlertDialog dialog = alert.create();
            dialog.show();
            Button positiveButton =
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton =
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            // Change the alert dialog buttons text and background color
            positiveButton.setTextColor(Color.parseColor("#F7D708"));
            negativeButton.setTextColor(Color.parseColor("#F7D708"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*hit signup api*/
    public void signUpApi(String name, String email, String email_confirmation, String password, String dob, String promocode, String mediaId, String mediaType) {
        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        pDialog.show();
        Call<SignupBeen> signupBeenCall;
        Log.e(TAG, "signUpApi---" + name + "--" + email + "---" + email_confirmation + "--" + password + "--" + dob + "--" + promocode + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            signupBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .signUp(CommonVariables.AUTHORIZATIONKEY, name, email, email_confirmation, password, dob, promocode, mediaType, mediaId);
            signupBeenCall.enqueue(new Callback<SignupBeen>() {
                @Override
                public void onResponse(Call<SignupBeen> call, Response<SignupBeen> response) {

                    try {
                        Log.e(TAG, "response---" + new Gson().toJson(response.body()));
                        if (response.code() == 200) {
                            //check code is equal to 1
                            if (response.body().getCode().equals("1")) {
                                Log.e(TAG, "response---" + new Gson().toJson(response.body().getData()));
                                //save data in local database
                                ThrillingPicksPrefrences.storeUserPreferences(SignUpActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
                                //show message in toast
                                Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                //go to pricing screen
                                Intent intent = new Intent(SignUpActivity.this, PricingActivity.class);
                                intent.putExtra("From_activity", "SignUpActivity");
                                startActivity(intent);
                                finish();
                            }
                            //check code is equal to 0
                            else if (response.body().getCode().equals("0")) {
                                Log.e(TAG, "response---" + new Gson().toJson(response.body().getData()));
                                //show message in toast
                                Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            //check code is equal to -1
                            else if (response.body().getCode().equals("-1")) {
                                Log.e(TAG, "response---" + new Gson().toJson(response.body().getData()));
                                Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                //clear local database
                                ThrillingPicksPrefrences.clearAllData("TPData", SignUpActivity.this);
                                //go to login screen
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                //show message in toast
                                Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } else {
                            Log.e(TAG, "response code not equal to 200");
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, jsonObject.getString("message"));
                            Toast.makeText(SignUpActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SignupBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            ex.printStackTrace();
            Log.e(TAG, "error" + ex);
        }
    }

    /*back press functionality*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(SignUpActivity.this, LoginTypeActivity.class));
        finish();
    }
}
