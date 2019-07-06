package com.thrillingpicks.views.activities.signUpFlow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.thrillingpicks.R;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.model.SigninBeen;
import com.thrillingpicks.model.SocialLoginBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.PricingActivity;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.fragments.home.HomeFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thrillingpicks.utils.CommonUtils.compareDate;
import static com.thrillingpicks.utils.CommonUtils.isNullOrEmpty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
   /*Views declaration for Login Screen*/
   String TAG=LoginActivity.class.getSimpleName();
   private EditText loginEtUserName;
   private EditText loginEtPassword;
   private TextView loginTvForgotPassword;
   private Button loginBtn;
   private Button loginBtnFacebook;
   private Button loginBtnGoogle;
   private TextView loginTvSignup;
   LinearLayout login_main_ll;
   GoogleApiClient mGoogleApiClient;
   private static final int RC_SIGN_IN=007;
   LoginManager loginManager;
   CallbackManager callbackManager;
   String Email="", firstname="", lastname="", mediaId="", media="";
   Dialog pDialog;
   ImageView login_img_back;
   private boolean fBLogInClicked=false;
   private List<String> PERMISSIONS=Arrays.asList("public_profile", "email");

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  /*layout inflate*/
	  setContentView(R.layout.activity_login);
	  /*view initialization method calling*/
	  initView();
   }

   /*initView Method definition*/
   private void initView(){
	  FacebookSdk.sdkInitialize(LoginActivity.this);
	  callbackManager=CallbackManager.Factory.create();
	  loginEtUserName=findViewById(R.id.login_et_user_name);
	  loginEtPassword=findViewById(R.id.login_et_password);
	  loginTvForgotPassword=findViewById(R.id.login_tv_forgot_password);
	  loginBtn=findViewById(R.id.login_btn);
	  loginBtnFacebook=findViewById(R.id.login_btn_facebook);
	  loginBtnGoogle=findViewById(R.id.login_btn_google);
	  loginTvSignup=findViewById(R.id.login_tv_signup);
	  login_main_ll=findViewById(R.id.login_main_ll);
	  login_img_back=findViewById(R.id.login_img_back);
	  //initialize arraylist
	  ArrayList<String> fbPermissions=new ArrayList<String>();
	  fbPermissions.add("public_profile");
	  fbPermissions.add("email");
	  loginManager=LoginManager.getInstance();
	  //initialize progress dialog
	  pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  pDialog.setCancelable(false);
	  HomeFragment.selectedPostion=0;
	  /*Click listener method*/
	  clickListenerMethod();
	  //google login
	  googleLogin();
   }

   /*Click listener method*/
   private void clickListenerMethod(){
	  loginTvSignup.setOnClickListener(this);
	  loginTvForgotPassword.setOnClickListener(this);
	  loginBtn.setOnClickListener(this);
	  loginBtnGoogle.setOnClickListener(this);
	  loginBtnFacebook.setOnClickListener(this);
	  login_main_ll.setOnClickListener(this);
	  login_img_back.setOnClickListener(this);
   }

   /*overrideClick listener method*/
   @RequiresApi(api=Build.VERSION_CODES.JELLY_BEAN)
   @Override
   public void onClick(View v){
	  switch(v.getId()){
		 case R.id.login_tv_signup:
			//method call to hide keyboard
			hideKeyboard();
			//go to signup screen
			Intent signUpIntent=new Intent(LoginActivity.this, SignUpActivity.class);
			startActivity(signUpIntent);
			finish();
			break;
		 case R.id.login_tv_forgot_password:
			//method call to hide keyboard
			hideKeyboard();
			//go to forgot password screen
			Intent forgotPassIntent=new Intent(LoginActivity.this, ForgotPasswordActivity.class);
			startActivity(forgotPassIntent);
			break;
		 case R.id.login_btn:
			//method call to hide keyboard
			hideKeyboard();
			//method call to check feilds validations
			checkValidation();
			break;
		 case R.id.login_btn_google:
			//check internet connection
			if (CommonUtils.isConnectingToInternet(this)){
			   signOut();
			   //method call to google login
			   signIn();
			}else{
			   //show message in toast
			   Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			}
			break;
		 case R.id.login_btn_facebook:
			Log.i("check_btn_click", "facebook click");
			//check internet connection
			if (CommonUtils.isConnectingToInternet(this)){
			   if (fBLogInClicked){
				  LoginManager.getInstance().logOut();
//				  disconnectFromFacebook();
				  facebookLogins();
			   }else{
				  fBLogInClicked=true;
				  facebookLogins();
			   }
			}else{
			   //show alert message in toast
			   Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			}
			break;
		 case R.id.login_main_ll:
			//method call to hide keyboard
			hideKeyboard();
			break;
		 case R.id.login_img_back:
			//method call to hide keyboaerd
			hideKeyboard();
			//back press functionality
			onBackPressed();
			break;
	  }
   }

   public void facebookLogins(){
	  //initialize dialog
	  final Dialog pDialog=new Dialog(LoginActivity.this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  //set layout for dialog
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  //set outside clickable false
	  pDialog.setCancelable(false);
	  LoginManager.getInstance().logInWithReadPermissions(this, PERMISSIONS);
	  LoginManager.getInstance().registerCallback(callbackManager,
												  new FacebookCallback<LoginResult>(){
													 @Override
													 public void onSuccess(LoginResult loginResult){
														fBLogInClicked=true;
														System.out.println("loginResult is "+loginResult);
														loginResult.getAccessToken().getUserId();
														Log.e("check_accesstoken", ""+loginResult.getAccessToken().getToken());
														Log.e("check_currenttoken",
															  ""+AccessToken.getCurrentAccessToken());
														String mFacebookUserId=loginResult.getAccessToken().getUserId();
														GraphRequest request=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
																									   new GraphRequest.GraphJSONObjectCallback(){
																										  @Override
																										  public void onCompleted(
																											 JSONObject object,
																											 GraphResponse response){
																											 System.out.println("object is "+object);
																											 try{
																												Log.e("rrrrrr====", ""+response);
																												try{
																												if (object.has("name")){
																												   String str=object.getString("name");
																												   String[] parts=str.split(" ");
																												   String string1=parts[0];
																												   firstname=string1;
																												   String string2=parts[1];
																												   Log.e(TAG, "check_facebook"+str);
																												   lastname=string2;
																												}
																												   }catch(Exception e){
																													  e.printStackTrace();
																												   }
																												try{
																												   if (object.has("email")){
																													  Email=object.getString("email");
																												   }
																												   if (object.has("id")){
																													  mediaId=object.getString("id");
																												   }
																												}catch(Exception e){
																												   e.printStackTrace();
																												}
																												media="facebook";
																												Log.e(TAG, "check_face_book_key:--"+mediaId);
																												/*----hit google fb sign api method----*/
																												if (CommonUtils.isConnectingToInternet(LoginActivity.this)){
																												   //hit social login api
																												   socailLoginApi(mediaId, media);
																												}else{
																												   //show alert message in toast
																												   Toast.makeText(LoginActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
																												}
																											 }catch(Exception e){
																												e.printStackTrace();
																											 }
																											 pDialog.dismiss();
																										  }
																									   });
														Bundle parameters=new Bundle();
														parameters.putString("fields", "email,name");
														request.setParameters(parameters);
														request.executeAsync();
													 }

													 @Override
													 public void onCancel(){
														pDialog.dismiss();
														// App code
														System.out.println("Facebook cancelled");
													 }

													 @Override
													 public void onError(FacebookException exception){
														pDialog.dismiss();
														exception.printStackTrace();
														Log.e("check_fb_error", ""+exception);
														if (AccessToken.getCurrentAccessToken()!=null){
														   LoginManager.getInstance().logOut();
														   AccessToken.setCurrentAccessToken(null);
														   LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, PERMISSIONS);
														}
													 }
												  });
   }

   /*----google sign in method-----*/
   private void signIn(){
	  try{
		 Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		 startActivityForResult(signInIntent, RC_SIGN_IN);
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   /*check feild validation */
   private void checkValidation(){
	  try{
		 String email="", password="";
		 //check email not null validation
		 if (CommonUtils.validateForNull(this, loginEtUserName, getString(R.string.enter_email_msg_string))){
			//check email  pattren validation
			if (CommonUtils.validateEmail(this, loginEtUserName, getString(R.string.enter_valid_email_msg_string), getString(R.string.enter_valid_email_msg_string))){
			   email=loginEtUserName.getText().toString().trim();
			   //check password not null validation
			   if (CommonUtils.validateForNull(this, loginEtPassword, getString(R.string.enter_password))){
				  password=CommonUtils.convertMD5(loginEtPassword.getText().toString().trim());
				  Log.e(TAG, "password convert MD5:--"+password);
				  //check internet conection validation
				  if (CommonUtils.isConnectingToInternet(this)){
					 //hit sign in api
					 signInApi(email, password);
				  }else{
					 //show message in toast
					 Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
				  }
			   }
			}
		 }
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }
   /*logout form facebook */
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

   /*hit signin api*/
   public void signInApi(String email, String password){
	  //initialize dialog
	  final Dialog pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  //hide dialog title bar
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  //set layout for dialog
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  //set outside click false
	  pDialog.setCancelable(false);
	  //show dialog
	  pDialog.show();
	  Call<SigninBeen> signinBeenCall;
	  Log.e(TAG, "signInApi---"+CommonVariables.AUTHORIZATIONKEY+"---"+email+"--"+password);
	  try{
		 signinBeenCall=ApiClient.getClient().create(ApiInterface.class)
								 .signIn(CommonVariables.AUTHORIZATIONKEY, email, password);
		 signinBeenCall.enqueue(new Callback<SigninBeen>(){
			@Override
			public void onResponse(Call<SigninBeen> call, Response<SigninBeen> response){
			   try{
				  if (response.code()==200){
					 if (response.body().getCode().equals("1")){
						Log.e(TAG, "response---"+new Gson().toJson(response.body().getData()));
						//clear local database
						ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, LoginActivity.this);
						//shtore data in local data base
						ThrillingPicksPrefrences.storeUserPreferences(LoginActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
						//show success message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
						// check go to next screen flow
						checkNextFlow();
					 }else if (response.body().getCode().equals("0")){
						Log.e(TAG, "response---"+new Gson().toJson(response.body().getData()));
						//show message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 }else{
						Log.e(TAG, "response---"+new Gson().toJson(response.body().getData()));
						//show message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 }
					 pDialog.dismiss();
				  }else{
					 Log.e(TAG, "response code not equal to 200");
					 JSONObject jsonObject=new JSONObject(response.errorBody().string());
					 Log.e(TAG, jsonObject.getString("message"));
					 pDialog.dismiss();
				  }
			   }catch(Exception e){
				  Log.e(TAG, "Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<SigninBeen> call, Throwable t){
			   pDialog.dismiss();
			   Log.e(TAG, "On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "error"+ex);
		 ex.printStackTrace();
	  }
   }

   @RequiresApi(api=Build.VERSION_CODES.JELLY_BEAN)
   @Override
   public void onBackPressed(){
	  super.onBackPressed();
	  //go to login type screen
	  Intent intent=new Intent(LoginActivity.this, LoginTypeActivity.class);
	  startActivity(intent);
	  finish();
   }

   /*-----google login----*/
   private void googleLogin(){
	  try{
		 GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build();
		 mGoogleApiClient=new GoogleApiClient.Builder(this)
			.enableAutoManage(LoginActivity.this, this)
			.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
			.build();
	  }catch(Exception ex){
		 ex.printStackTrace();
	  }
   }

   /*google sign out*/
   private void signOut(){
	  try{
		 Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
			new ResultCallback<Status>(){
			   @Override
			   public void onResult(Status status){
			   }
			});
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
	  callbackManager.onActivityResult(requestCode, resultCode, data);
	  super.onActivityResult(requestCode, resultCode, data);
	  if (requestCode==RC_SIGN_IN){
		 GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		 int statusCode=result.getStatus().getStatusCode();
		 handleSignInResult(result);
		 Log.e(TAG, "onActivityResult:----"+result.isSuccess()+"--"+statusCode);
	  }
   }

   /*-----method after response----*/
   private void handleSignInResult(GoogleSignInResult result){
	  Log.e(TAG, "handleSignInResult:"+result.isSuccess());
	  try{
		 if (result.isSuccess()){
			/*--------Signed in successfully, show authenticated UI.------*/
			GoogleSignInAccount acct=result.getSignInAccount();
			String str=acct.getDisplayName();
			String str1=acct.getEmail();

			/*----------this line use split two string eg. firstname and lastname---------*/
			String[] parts=str.split(" ");
			String string1=parts[0];
			String string2=parts[1];
			//get basic detail from google account
			Email=str1;
			firstname=string1;
			lastname=string2;
			media="google";
			mediaId=acct.getId();
			Log.e(TAG, "handleSignInResult--check google user--"+Email+"--"+firstname+"--"+lastname+"--"+media+"--"+mediaId);
			//check internet connection validation
			if (CommonUtils.isConnectingToInternet(this)){
			   //hit social login api
			   socailLoginApi(mediaId, media);
			}else{
			   //show message in toast
			   Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			}
		 }
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   /*hit signin api*/
   public void socailLoginApi(final String social_media_id, final String social_media_name){
	  pDialog.show();
	  Call<SocialLoginBeen> aboutUserBeenCall;
	  Log.e(TAG, "socailLoginApi---"+social_media_id+"--"+social_media_name+"---"+CommonVariables.AUTHORIZATIONKEY);
	  try{
		 aboutUserBeenCall=ApiClient.getClient().create(ApiInterface.class)
									.socialLogin(CommonVariables.AUTHORIZATIONKEY, social_media_id, social_media_name);
		 aboutUserBeenCall.enqueue(new Callback<SocialLoginBeen>(){
			@Override
			public void onResponse(Call<SocialLoginBeen> call, Response<SocialLoginBeen> response){
			   try{
				  if (response.code()==200){
					 //check code equal to 1
					 if (response.body().getCode().equals("1")){
						Log.e(TAG, "socailLoginApi--response---"+new Gson().toJson(response.body()));
						//show success message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
						//save data in local database
						ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, LoginActivity.this);
						//save data in local database
						ThrillingPicksPrefrences.storeUserPreferences(LoginActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
						//go to home screen
						checkNextFlow();
					 }
					 //check code equal to 0
					 else if (response.body().getCode().equals("0")){
						Log.e(TAG, "socailLoginApi--response---"+new Gson().toJson(response.body()));
						//show alert message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 }
					 //check code equal to 2
					 else if (response.body().getCode().equals("2")){
						// if user not found
						Log.e(TAG, "socailLoginApi--response---"+new Gson().toJson(response.body()));
						Log.e(TAG, "socailLoginApi--response---"+firstname+"--"+lastname+"--"+Email);
						//redirect to signup screen
						Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
						intent.putExtra("name", firstname+" "+lastname);
						intent.putExtra("email", Email);
						intent.putExtra("socialID", social_media_id);
						intent.putExtra("socialType", social_media_name);
						startActivity(intent);
						finish();
					 }else{
						//show alert message in toast
						Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 }
					 //dismiss progress dialog
					 pDialog.dismiss();
				  }else{
					 Log.e(TAG, "response code not equal to 200");
					 JSONObject jsonObject=new JSONObject(response.errorBody().string());
					 Log.e(TAG, jsonObject.getString("message"));
					 pDialog.dismiss();
				  }
			   }catch(Exception e){
				  Log.e(TAG, "socailLoginApi--Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<SocialLoginBeen> call, Throwable t){
			   pDialog.dismiss();
			   Log.e(TAG, "socailLoginApi--On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "socailLoginApi--error"+ex);
		 ex.printStackTrace();
	  }
   }

   /*hide keyboard outside touch*/
   private void hideKeyboard(){
	  try{
		 InputMethodManager imm1=
			(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		 View view2=getCurrentFocus();
		 if (view2==null){
			view2=new View(this);
		 }
		 imm1.hideSoftInputFromWindow(view2.getWindowToken(), 0);     //hide keyboard
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   /*check go to next screen flow according to data base*/
   public void checkNextFlow(){
	  /*
	   *update common data
	   * ID,TOken,Name,Email,DOB,ProfileImage
	   * */
	  ThrillingPicksPrefrences.updateCommonData(LoginActivity.this);
	  String user_Subscription_End_Date=null;
	  String data=ThrillingPicksPrefrences.getUserPreferences(LoginActivity.this, CommonUtils.USER);
	  try{
		 JSONObject json=new JSONObject(data);
		 Log.e(TAG, "SharedPreferenceData---"+json);
		 if (!json.optString("user_subscription_end_date").equals("")){
			//get user subscripton end date form database
			user_Subscription_End_Date=json.getString("user_subscription_end_date");
			Log.e(TAG, "check subscripyion date:--"+user_Subscription_End_Date);
		 }else{
			Log.e(TAG, "user_subscription_end_date null");
		 }
	  }catch(Exception e){
		 e.printStackTrace();
	  }
	  //check subscription end date not null
	  if (isNullOrEmpty(user_Subscription_End_Date)){
		 //go to pricing screen
		 Intent i=new Intent(LoginActivity.this, PricingActivity.class);
		 startActivity(i);
		 finish();
	  }else{
		 Log.e(TAG, "comare date:--"+compareDate(user_Subscription_End_Date));
		 //check subscription end date and current date grater then or less then
		 if (compareDate(user_Subscription_End_Date).equals("after")){
			//go to pricing screen
			Intent i=new Intent(LoginActivity.this, PricingActivity.class);
			startActivity(i);
			finish();
		 }else{
			//go to home screen
			Intent i=new Intent(LoginActivity.this, HomeNavigationActivity.class);
			startActivity(i);
			finish();
		 }
	  }
   }
}
