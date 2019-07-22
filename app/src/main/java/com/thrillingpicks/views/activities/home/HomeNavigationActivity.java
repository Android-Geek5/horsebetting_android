package com.thrillingpicks.views.activities.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
import com.thrillingpicks.BuildConfig;
import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.AccountItemOnclick;
import com.thrillingpicks.interfaces.MenuRecylerOnClick;
import com.thrillingpicks.model.LogoutBeen;
import com.thrillingpicks.model.PicksBeen;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.model.TracksBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.ApplyPromoCodeActivity;
import com.thrillingpicks.views.activities.CardListActivity;
import com.thrillingpicks.views.activities.ChangePaswordActivity;
import com.thrillingpicks.views.activities.ChoosePlanActivity;
import com.thrillingpicks.views.activities.InviteFriendsActivity;
import com.thrillingpicks.views.activities.PricingActivity;
import com.thrillingpicks.views.activities.signUpFlow.ForgotPasswordActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.activities.signUpFlow.SignUpActivity;
import com.thrillingpicks.views.adapters.AccountAdapter;
import com.thrillingpicks.views.adapters.HomeTabsPagerAdapter;
import com.thrillingpicks.views.adapters.MenuRecylerAdapter;
import com.thrillingpicks.views.activities.AccountActivity;
import com.thrillingpicks.views.fragments.home.HomeFragment;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.FOCUS_DOWN;

public class HomeNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    String TAG = HomeNavigationActivity.class.getSimpleName();
    /*View Declaration*/
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private Fragment fragment = null;
    ImageView menu_iv, down_iv, up_iv;
    RecyclerView menu_recyler, accoun_items_recyler;
    LinearLayoutManager menuManager;
    LinearLayoutManager accountManager;
    MenuRecylerAdapter menuRecylerAdapter;
    TextView invite_friends_tv, cards_tv, navigation_user_name_tv, navigation_user_email_tv;
    RelativeLayout my_account_rl;
    CircleImageView navigation_user_img;
    ArrayList<String> accountItemList;
    AccountAdapter accountAdapter;
    Boolean visibile = false;
    NestedScrollView navigation_nested;
    Dialog pDialog;
    List<PicksBeen.Datum> recentWinsList;
    GoogleApiClient mGoogleApiClient;
    public static List<TracksBeen.Data.Today> todayTracklist;
    public static List<TracksBeen.Data.Tomorrow> tomorrowTracklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        //call method to initialize view
        initView();
    }

    /*initialization views*/
    private void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu_iv = findViewById(R.id.menu_iv);
        menu_recyler = findViewById(R.id.menu_recyler);
        my_account_rl = findViewById(R.id.my_account_rl);
        drawer = findViewById(R.id.drawer_layout);
        cards_tv = findViewById(R.id.cards_tv);
        invite_friends_tv = findViewById(R.id.invite_friends_tv);
        accoun_items_recyler = findViewById(R.id.accoun_items);
        navigation_user_name_tv = findViewById(R.id.navigation_user_name_tv);
        navigation_user_email_tv = findViewById(R.id.navigation_user_email_tv);
        navigation_user_img = findViewById(R.id.navigation_user_img);
        down_iv = findViewById(R.id.down_iv);
        up_iv = findViewById(R.id.up_iv);
        navigation_nested = findViewById(R.id.navigation_nested);

        //initalize progress dialog
        pDialog = new Dialog(HomeNavigationActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //call method to implement click listner on views
        clickView();

        /*
         * update common data
         * ID,TOKEN,NAME,EMAIL,DOB,IMAGEURL
         *
         * */
        ThrillingPicksPrefrences.updateCommonData(HomeNavigationActivity.this);

        //method call to set data in navigation drawer
        addDataList();
        googleLogin();
        //set feilds in navigation drawer recyler
        setAdapter();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        //check internet conection validation
        if (CommonUtils.isConnectingToInternet(this)) {
            //hit session  api
            sessionApi(CommonVariables.TOKEN);
        } else {
            //show message in toast
            Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }
    }

    /*hit session api*/
    public void sessionApi(String token) {
        pDialog.show();
        Call<SessionBeen> sessionBeenCall;
        Log.e(TAG, "SessionApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            sessionBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .sessionLogin(CommonVariables.AUTHORIZATIONKEY, token);
            sessionBeenCall.enqueue(new Callback<SessionBeen>() {
                @Override
                public void onResponse(Call<SessionBeen> call, Response<SessionBeen> response) {
                    try {
                        //check code equal to 1
                        if (response.body().getCode().equals("1")) {
                            Log.e(TAG, "session--response--1-" + new Gson().toJson(response.body().getData()));
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, HomeNavigationActivity.this);
                            //store data in local database
                            ThrillingPicksPrefrences.storeUserPreferences(HomeNavigationActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
                            //add home fgragment
                            fragment = new HomeFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.home_navigation_fragment, fragment, "MY_FRAGMENT");
                            ft.commitAllowingStateLoss();

                            //check internet connection
                            if (CommonUtils.isConnectingToInternet(HomeNavigationActivity.this)) {
                                //set profile data
                                setProfileData();
                                //hit get picks api
                                getPicksApi(CommonVariables.TOKEN);
                            } else {
                                //show message in toast
                                Toast.makeText(HomeNavigationActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                            }
                            //check code is equal to 0
                        } else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "session--response-0--" + new Gson().toJson(response.body().getData()));
                            //show alert message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "session--response--1--" + new Gson().toJson(response.body().getData()));
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", HomeNavigationActivity.this);
                            //show session expire message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(HomeNavigationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show alert message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<SessionBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "On failure" + t);
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "error" + ex);
        }
    }

    /*add data navigation list*/
    private void addDataList() {
        accountItemList = new ArrayList<>();
        accountItemList.add("Profile Settings");
        accountItemList.add("Subscription Plan");
        accountItemList.add("Change Password");
        accountItemList.add("Logout");
    }

    /*set user profile data from local database*/
    private void setProfileData() {
        try {
            navigation_user_email_tv.setText("" + CommonVariables.NAME);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.img_horse)
                    .error(R.drawable.img_horse)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();

            /*set user image using glide*/
            Glide.with(HomeNavigationActivity.this)
                    .load(CommonVariables.IMAGEURL)
                    .apply(options)
                    .into(navigation_user_img);
            invite_friends_tv.setText(getString(R.string.invite_friends_string) + " , Get " + CommonVariables.REFERTOAMOUNT + "%");
            Log.e(TAG, "user_image" + CommonVariables.IMAGEURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*clickable views initialize*/
    private void clickView() {
        menu_iv.setOnClickListener(this);
        my_account_rl.setOnClickListener(this);
        invite_friends_tv.setOnClickListener(this);
        cards_tv.setOnClickListener(this);
    }

    /*load navigation recyler*/
    private void setAdapter() {
        menuManager = new LinearLayoutManager(this);
        accountManager = new LinearLayoutManager(this);
        menu_recyler.setLayoutManager(menuManager);
        accoun_items_recyler.setLayoutManager(accountManager);
        accountAdapter = new AccountAdapter(this, accountItemList, new AccountItemOnclick() {
            @Override
            public void itemClick(View v, int position) {
                switch (position) {
                    case 0:
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                        //go to account screen
                        startActivity(new Intent(HomeNavigationActivity.this, AccountActivity.class));
                        break;
                    case 1:
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                        //go to pricing screen
                        Intent intent = new Intent(HomeNavigationActivity.this, PricingActivity.class);
                        intent.putExtra("From_activity", "HomeNavigationActivity");
                        startActivity(intent);
                        break;
                    case 2:
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                        //go to change password screen
                        startActivity(new Intent(HomeNavigationActivity.this, ChangePaswordActivity.class));

                        break;
                    case 3:
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                        //check internet connection
                        if (CommonUtils.isConnectingToInternet(HomeNavigationActivity.this)) {
                            //hit logout  api
                            logOutApi(CommonVariables.TOKEN);
                        } else {
                            //show message in toast
                            Toast.makeText(HomeNavigationActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        accoun_items_recyler.setAdapter(accountAdapter);

    }


    /*back press*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_iv:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.my_account_rl:
                if (visibile) {
                    accoun_items_recyler.setVisibility(View.GONE);
                    visibile = false;
                    up_iv.setVisibility(View.VISIBLE);
                    down_iv.setVisibility(View.GONE);
                } else {
                    accoun_items_recyler.setVisibility(View.VISIBLE);
                    navigation_nested.requestFocus(FOCUS_DOWN);
                    visibile = true;
                    up_iv.setVisibility(View.GONE);
                    down_iv.setVisibility(View.VISIBLE);
                    accoun_items_recyler.requestFocus(FOCUS_DOWN);
                }
                break;
            case R.id.invite_friends_tv:
                startActivity(new Intent(HomeNavigationActivity.this, InviteFriendsActivity.class));
                break;
            case R.id.cards_tv:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                //go to card list screen
                Intent intent = new Intent(HomeNavigationActivity.this, CardListActivity.class);
                intent.putExtra("activity", "HomeNavigationActivity");
                startActivity(intent);
                break;

        }
    }


    /*hit logout api*/
    public void logOutApi(String token) {
        final Dialog pDialog = new Dialog(this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        pDialog.show();
        Call<JsonObject> logoutBeenCall;
        Log.e(TAG, "logOutApi---" + token + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            logoutBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .logout(CommonVariables.AUTHORIZATIONKEY, token);
            logoutBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //check code is equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            Log.e(TAG, "logOutApi--response--1-" + jsonObject);
                            //disconnect google login
                            googleSignOut();
                            //disconnect facebook login
                            disconnectFromFacebook();
                            /*clear Data to Shared Preferences*/
                            ThrillingPicksPrefrences.clearAllData("TPData", HomeNavigationActivity.this);
                            /*show message in toast*/
                            Toast.makeText(HomeNavigationActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(HomeNavigationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //check code is equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "logOutApi--response-0--" + jsonObject);
                            //show message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("code").equals("-1")) {
                            Log.e(TAG, "logOutApi--response--+1--" + jsonObject);
                            /*for seeion expire */
                            ThrillingPicksPrefrences.clearAllData("TPData", HomeNavigationActivity.this);
                            //show message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(HomeNavigationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "logOutApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "logOutApi--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "logOutApi--error" + ex);
            ex.printStackTrace();
        }
    }

    /*get recent win using picks api hit*/
    public void getPicksApi(final String token) {
        pDialog.show();
        Call<PicksBeen> picksBeenCall;
        Log.e(TAG, "getPicksApi---" + token);
        try {
            picksBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .picks(CommonVariables.AUTHORIZATIONKEY, token);
            picksBeenCall.enqueue(new Callback<PicksBeen>() {
                @Override
                public void onResponse(Call<PicksBeen> call, Response<PicksBeen> response) {
                    try {
                        //check code equal to 1
                        if (response.body().getCode().equals("1")) {
                            Log.e(TAG, "getPicksApi--response--1-" + new Gson().toJson(response.body().getData()));
                            recentWinsList = response.body().getData();
                            /*set data in recent wins adatpter*/
                            menuRecylerAdapter = new MenuRecylerAdapter(HomeNavigationActivity.this, recentWinsList, new MenuRecylerOnClick() {
                                @Override
                                public void itemClick(View v, int position) {
                                }
                            });
                            menu_recyler.setAdapter(menuRecylerAdapter);
                            menu_recyler.smoothScrollToPosition(accountItemList.size());

                        } else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "getPicksApi--response--0-" + new Gson().toJson(response.body().getData()));
                            /*show message in toast*/
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "getPicksApi--response--3-" + new Gson().toJson(response.body().getData()));
                            /*show message in toast*/
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //clear local database
                            ThrillingPicksPrefrences.clearAllData("TPData", HomeNavigationActivity.this);
                            //go to login screen
                            Intent intent = new Intent(HomeNavigationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show message in toast
                            Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "getPicksApi--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PicksBeen> call, Throwable t) {
                    Log.e(TAG, "getPicksApi--On failure" + t);
                    if (CommonUtils.isConnectingToInternet(HomeNavigationActivity.this)) {
                        //method call to hit pickserror  api
                        getPicksApiError(token);
                    } else {
                        //show message in toast
                        Toast.makeText(HomeNavigationActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "getPicksApi--error" + ex);
            ex.printStackTrace();
        }

    }

    /*get picks exception api hit*/
    public void getPicksApiError(String token) {
        Call<LogoutBeen> pickupErrorBeenCall;
        Log.e(TAG, "getPicksApiError---" + token + "----" + CommonVariables.AUTHORIZATIONKEY);
        try {
            pickupErrorBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .picksError(CommonVariables.AUTHORIZATIONKEY, token);
            pickupErrorBeenCall.enqueue(new Callback<LogoutBeen>() {
                @Override
                public void onResponse(Call<LogoutBeen> call, Response<LogoutBeen> response) {
                    try {
                        //show message in toast
                        Toast.makeText(HomeNavigationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "getPicksApiError--Exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LogoutBeen> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "getPicksApiError--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "getPicksApiError--error" + ex);
            ex.printStackTrace();
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

    /*facebook sign out*/
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
