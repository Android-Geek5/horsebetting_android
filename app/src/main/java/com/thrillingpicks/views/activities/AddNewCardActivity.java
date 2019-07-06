package com.thrillingpicks.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;
import com.thrillingpicks.R;

import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginTypeActivity;
import com.thrillingpicks.views.activities.signUpFlow.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCardActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = AddNewCardActivity.class.getSimpleName();
      private static final String PUBLISHABLE_KEY = "pk_live_xFfIYb7MKishcUMq5Nej632b00MCFbAFdK";   // using publish key for stripe account pk_test_j1x6N6NRcxR8AolRflTBjQnp00Q58NjWYa
//    private static final String PUBLISHABLE_KEY = "pk_test_j1x6N6NRcxR8AolRflTBjQnp00Q58NjWYa";
    ImageView add_new_card_back;
    TextView delete_card_tv, save_card_tv;
    EditText account_holder_name;
    private CardNumberEditText mCardNumberEditText;
    private ExpiryDateEditText mExpiryDateEditText;
    private StripeEditText mCvcEditText, mPostalCodeEditText;
    private static Dialog pDialog;
    CardMultilineWidget mCardInputWidget;
    CheckBox quick_pay_chckbox;
    String save_card = "";
    LinearLayout quick_pay_ll;
    String Subscription_id = "";
    String cardID;
    String card_number = "", exp_year = "", expMonth = "", user_name = "";
    int exp_month = 0;
    String PromoId = "";
    LinearLayout add_new_card_main_ll;
    TextView detail_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        //initialize views
        initView();
    }

    /*initlize activity views*/
    private void initView() {
        delete_card_tv = findViewById(R.id.delete_card_tv);
        add_new_card_back = findViewById(R.id.add_new_card_back);
        save_card_tv = findViewById(R.id.save_card_tv);
        account_holder_name = findViewById(R.id.account_holder_name);
        quick_pay_chckbox = findViewById(R.id.quick_pay_chckbox);
        quick_pay_ll = findViewById(R.id.quick_pay_ll);
        add_new_card_main_ll = findViewById(R.id.add_new_card_main_ll);
        detail_title_tv = findViewById(R.id.detail_title_tv);

        mCardInputWidget = (CardMultilineWidget) findViewById(R.id.card_multiline_widget);
        mCardNumberEditText = findViewById(com.stripe.android.R.id.et_add_source_card_number_ml);
        mPostalCodeEditText = findViewById(com.stripe.android.R.id.et_add_source_postal_ml);
        mExpiryDateEditText = findViewById(com.stripe.android.R.id.et_add_source_expiry_ml);
        mCvcEditText = findViewById(com.stripe.android.R.id.et_add_source_cvc_ml);
        mCvcEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //initialize progress dialog
        pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        //get intent data
        getIntentData();
        //clickable views
        clickView();
    }

    /*get initent data
     *
     * subscription id,card id,car number,exp month,exp year,
     * user name
     *
     * activity variable is used to check previous flow
     */
    private void getIntentData() {
        try {
            Log.e(TAG, "" + getIntent().getStringExtra("activity"));
            if (getIntent().getExtras() != null) {
                Log.e(TAG, "check acitivity---" + getIntent().getStringExtra("activity"));
                if (getIntent().getStringExtra("activity").equals("1")) {
                    save_card_tv.setText("Add");
                    quick_pay_ll.setVisibility(View.GONE);
                   /* Subscription_id = getIntent().getStringExtra("Subscription_id");
                    quick_pay_ll.setVisibility(View.VISIBLE);
                    if (CommonUtils.isNullOrEmpty(getIntent().getStringExtra("PromoId"))) {
                        Log.e(TAG, "check promo---null");
                        PromoId = "";
                    } else {
                        PromoId = getIntent().getStringExtra("PromoId");
                        Log.e(TAG, "check promo---" + PromoId);
                    }*/

                } else if (getIntent().getStringExtra("activity").equals("3")) {
                    quick_pay_ll.setVisibility(View.GONE);
                    cardID = getIntent().getStringExtra("cardID");
                    card_number = getIntent().getStringExtra("card_number");
                    exp_month = Integer.parseInt(getIntent().getStringExtra("exp_month"));
                    exp_year = getIntent().getStringExtra("exp_year");
                    user_name = getIntent().getStringExtra("user_name");
                    mCardNumberEditText.setText(card_number);
                    mCvcEditText.setText("XXX");
                    if (exp_month < 10) {
                        expMonth = "0" + exp_month;

                    } else {
                        expMonth = String.valueOf(exp_month);
                    }
                    String str = String.valueOf(exp_year);
                    String substring = str.substring(Math.max(str.length() - 2, 0));
                    Log.e(TAG, "exp month and year" + substring + "--" + exp_month);
                    mExpiryDateEditText.setText(expMonth + substring);
                    mCardNumberEditText.setEnabled(false);
                    mCvcEditText.setEnabled(false);
                    save_card_tv.setText("Update");
                    detail_title_tv.setText("Update Card");
                    mPostalCodeEditText.setText("XXXXX");
                    mPostalCodeEditText.setEnabled(false);
                    account_holder_name.setText(user_name);
                } else {
                    quick_pay_ll.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*clickable views*/
    private void clickView() {
        delete_card_tv.setOnClickListener(this);
        add_new_card_back.setOnClickListener(this);
        save_card_tv.setOnClickListener(this);
        add_new_card_main_ll.setOnClickListener(this);
        quick_pay_chckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quick_pay_chckbox.isChecked()) {
                    save_card = "1";
                } else {
                    save_card = "0";
                }
                Log.e(TAG, "check_save card:---" + save_card);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_card_tv:
                break;
            case R.id.add_new_card_back:
                onBackPressed();
                break;
            case R.id.add_new_card_main_ll:
                hideKeyboard();
                break;
            case R.id.save_card_tv:
                hideKeyboard();
                if (save_card_tv.getText().equals("Update")) {
                    int[] cardDate = mExpiryDateEditText.getValidDateFields();
                    //get card expiry month and year
                    String month = String.valueOf(cardDate[0]);
                    String year = String.valueOf(cardDate[1]);
                    Log.e(TAG, "check month and year:--" + month + "--" + year);

                    //check account holder name null validation
                    if (CommonUtils.validateForNull(this, account_holder_name, getString(R.string.enter_name))) {
                        //check name pattren validation
                        if (CommonUtils.checkNameValidation(AddNewCardActivity.this, account_holder_name, getString(R.string.enter_valid_name))) {
                            //check internet connection
                            if (CommonUtils.isConnectingToInternet(AddNewCardActivity.this)) {
                                //hit edit card api
                                editcardApi(CommonVariables.AUTHORIZATIONKEY, CommonVariables.TOKEN, cardID, month, year, account_holder_name.getText().toString().trim());

                            } else {
                                //show alert message in toast
                                Toast.makeText(AddNewCardActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    //check card number not null validation
                    if (CommonUtils.validateForNull(this, mCardNumberEditText, getString(R.string.enter_card_number))) {
                        //check expiry date not null
                        if (CommonUtils.validateForNull(this, mExpiryDateEditText, getString(R.string.enter_exp_date))) {
                            //check name pattren validation
                            if (CommonUtils.validateForNull(AddNewCardActivity.this, mCvcEditText, getString(R.string.enter_cvv_number))) {
                                //check name pattren validation
                                if (CommonUtils.validateForNull(AddNewCardActivity.this, account_holder_name, getString(R.string.enter_user_name))) {
                                    //check internet connection
                                    if (CommonUtils.isConnectingToInternet(AddNewCardActivity.this)) {
                                        //add new card
                                        addNewCard();
                                    } else {
                                        //show alert message in toast
                                        Toast.makeText(AddNewCardActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }
                    }
                }
                break;
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


    /*add new card
     *
     * create stripe token and hit card add api
     * */
    private void addNewCard() {
        try {
            int[] cardDate = mExpiryDateEditText.getValidDateFields();
            String cardNumber = mCardNumberEditText.getCardNumber();
            String cvcValue = mCvcEditText.getText().toString();
            if ((cardNumber != null && cardDate != null && cvcValue != null)) {

                Card card = new Card(cardNumber, cardDate[0], cardDate[1], cvcValue, account_holder_name.getText().toString().trim(), "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "");
                card.setName(account_holder_name.getText().toString());
                if (!card.validateCard()) {
                    Toast.makeText(this, "Something wrong please try again", Toast.LENGTH_SHORT).show();
                    // Do not continue token creation.
                } else {
                    // TODO: replace with your own test key
                    final String publishableApiKey = PUBLISHABLE_KEY;  // use publish key create stripe token
                    if (pDialog.isShowing()) {

                    } else {
                        pDialog.show();
                    }

                    Log.e(TAG, "cardNumber---" + cardNumber);
                    /*method to hit stripe api */
                    Stripe stripe = new Stripe(AddNewCardActivity.this);

                    stripe.createToken(card, publishableApiKey, new TokenCallback() {

                        public void onSuccess(Token token) {
                            // TODO: Send Token information to your backend to initiate a charge
                            String stripeToken = token.getId();
                            Log.e(TAG, "token---" + token.getId());

                            try {
                                Log.e(TAG, "" + getIntent().getStringExtra("activity"));
                                //check intent not null
//                                if (getIntent().getExtras() != null) {
//                                    //check previous activity flow using activity variable
//                                    if (getIntent().getStringExtra("activity").equals("1")) {
//                                        //check internet connection
//                                        if (CommonUtils.isConnectingToInternet(AddNewCardActivity.this)) {
//                                            //hit make payment api to subscribe paln
//                                            makePaymentApi(CommonVariables.TOKEN, save_card, token.getId(), Subscription_id, "", PromoId);
//                                        } else {
//                                            //show alert message in toast
//                                            Toast.makeText(AddNewCardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                                        }
//                                    } else {
                                //check internet connection
                                if (CommonUtils.isConnectingToInternet(AddNewCardActivity.this)) {
                                    //hit api to add new card
                                    addNewCard(stripeToken, CommonVariables.TOKEN);
                                } else {
                                    //show alert message in toast
                                    Toast.makeText(AddNewCardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
//                                    }
//                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        public void onError(Exception error) {
                            Log.d("Stripe", error.getLocalizedMessage());
                            error.printStackTrace();
                        }
                    });

                }
            } else {
                //show alert message in toast
                Toast.makeText(this, "Please enter card details", Toast.LENGTH_SHORT).show();
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

    /*hit signin api*/
    public void addNewCard(String CardToken, String UserToken) {
        Call<JsonObject> signinBeenCall;
        Log.e(TAG, "addNewCard---" + CardToken + "--" + UserToken + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            signinBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .addCard(CommonVariables.AUTHORIZATIONKEY, UserToken, CardToken);
            signinBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //check code equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            Log.e(TAG, "addNewCard---response-1--" + jsonObject.getString("message"));
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            //check intent not null
//                            if (getIntent().getExtras() != null) {
//                                //get previous activity data and check it
//                                if (getIntent().getStringExtra("activity").equals("1")) {
//                                    //  go to login activity
//                                    Intent intent = new Intent(AddNewCardActivity.this, HomeNavigationActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
                            finish();
//                                } else {
//                                    // back press functionality
//                                    onBackPressed();
//                                }
//                            }

                        }
                        //check code is equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "addNewCard---response--2-" + jsonObject.getString("message"));
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (jsonObject.getString("code").equals("-1")) {
                            Log.e(TAG, "addNewCard---response-11--" + jsonObject.getString("message"));
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "addNewCard---response--66-" + jsonObject.getString("message"));
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //progress dialog dismiss
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "addNewCard--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "addNewCard---On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "addNewCard--error" + ex);
            ex.printStackTrace();
        }

    }

    /*hit make Payment api*/
    public void makePaymentApi(String token, String save_card, String stripe_token, String subscription_id, String card_id, String code_id) {
        //initialize progress dialog
//        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
//        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pDialog.setContentView(R.layout.custom_progress_dialog);
//        pDialog.setCancelable(false);
        //show progress dialog
        if (pDialog.isShowing()) {

        } else {
            pDialog.show();
        }
//        pDialog.show();
        Call<JsonObject> signinBeenCall;
        Log.e(TAG, "makePaymentApi---" + token + "--" + save_card + "---" + stripe_token + "---" + subscription_id + "---" + card_id + "---" + CommonVariables.AUTHORIZATIONKEY);
        Log.e(TAG, "code_id---" + code_id);
        try {
            signinBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .makePayment(CommonVariables.AUTHORIZATIONKEY, token, save_card, stripe_token, subscription_id, card_id, code_id);
            signinBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        Log.e(TAG, "check response---" + response.body());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e(TAG, "fdfdmfdk" + jsonObject);
                        //check code is equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            //show sucess alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            if (getIntent().getExtras() != null) {
                               /* if (getIntent().getStringExtra("activity").equals("3")) {

                                } else {
                                    //go to home screen
                                    Intent intent = new Intent(AddNewCardActivity.this, HomeNavigationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }*/
                                finish();
                            }


                        }
                        //check code equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "makePaymentApi--response--0-" + response.body());
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "makePaymentApi--response--111-" + response.body());
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //dismiss progress dialog
                            pDialog.dismiss();
                        }
                        //dismiss progress dialog
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "makePaymentApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "makePaymentApi--On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error" + ex);
            ex.printStackTrace();
        }

    }

    /*edit card api hit*/
    public void editcardApi(String Authorization, String user_token, String card_id, String exp_month, String exp_year, String user_name) {
        //initialize progress dialog
//        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
//        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pDialog.setContentView(R.layout.custom_progress_dialog);
//        pDialog.setCancelable(false);
//        //show progress dialog
//        pDialog.show();

        if (pDialog.isShowing()) {

        } else {
            pDialog.show();
        }
        Call<JsonObject> signinBeenCall;
        Log.e(TAG, "editcardApi---" + Authorization + "---" + Authorization + "--" + user_token + "--" + user_token + "--" + user_token
                + "--" + exp_year + "--" + user_name);
        try {
            signinBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .editCard(Authorization, user_token, card_id, exp_month, exp_year, user_name);
            signinBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        Log.e(TAG, "editcardApi---response--" + response.body());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //check code is equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            //show sucess mesaage in toast
                            Toast.makeText(AddNewCardActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            // back press functionality
                            onBackPressed();
                        } else if (jsonObject.getString("code").equals("1")) {
                            //show alert message in toast
                            Toast.makeText(AddNewCardActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddNewCardActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //dismiss progress dialog
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error" + ex);
            ex.printStackTrace();
        }

    }

}
