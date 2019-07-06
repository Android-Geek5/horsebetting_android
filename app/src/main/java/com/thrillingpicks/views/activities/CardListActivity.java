package com.thrillingpicks.views.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.CardOnClick;
import com.thrillingpicks.interfaces.CardOnSlide;
import com.thrillingpicks.interfaces.DeleteCardOnClick;
import com.thrillingpicks.model.GetCardBeen;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.model.ShowPriceBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginTypeActivity;
import com.thrillingpicks.views.adapters.CardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
   String TAG=CardListActivity.class.getSimpleName();
   RecyclerView card_recycler;
   LinearLayoutManager cardmanager;
   CardListAdapter cardListAdapter;
   ImageView add_new_card_back;
   TextView add_new_card;
   private Dialog pDialog;
   List<GetCardBeen.Datum> cardBeenList;
   String fromactivity="";
   RelativeLayout payment_rl;
   public static int selected=-1;
   public static int cardOpen=-1;
   String price="";
   TextView pay_price_tv, save_card_tv;
   String Subscription_id;
   private String card_id="";
   LinearLayout coupon_ll;
   RelativeLayout yes_rl, no_rl, single_spinner_layout;
   ImageView yes_unchecked, yes_checked, no_unchecked, no_checked;
   String check_promo="Yes";
   Spinner coupon_spinner;
   ArrayList<String> list;
   ArrayList<String> referedamountarray;
   String PromoId="";
   TextView detail_title_tv;
   String finalAmount="";

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_card_list);
	  //initialize views
	  initView();
   }

   /*initialize activity views*/
   private void initView(){
	  card_recycler=findViewById(R.id.card_recycler);
	  add_new_card_back=findViewById(R.id.add_new_card_back);
	  add_new_card=findViewById(R.id.add_new_card);
	  payment_rl=findViewById(R.id.payment_rl);
	  pay_price_tv=findViewById(R.id.pay_price_tv);
	  save_card_tv=findViewById(R.id.save_card_tv);
	  coupon_ll=findViewById(R.id.coupon_ll);
	  yes_rl=findViewById(R.id.yes_rl);
	  no_rl=findViewById(R.id.no_rl);
	  yes_unchecked=findViewById(R.id.yes_unchecked);
	  yes_checked=findViewById(R.id.yes_checked);
	  no_unchecked=findViewById(R.id.no_unchecked);
	  no_checked=findViewById(R.id.no_checked);
	  single_spinner_layout=findViewById(R.id.single_spinner_layout);
	  coupon_spinner=findViewById(R.id.coupon_spinner);
	  detail_title_tv=findViewById(R.id.detail_title_tv);
	  //initialize progress dialog
	  pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  pDialog.setCancelable(false);
	  //get data from intent
	  getIntentData();
	  //clickable views
	  clickView();
   }

   private void setData(){
	  try{
		 list=new ArrayList<>();
		 referedamountarray=new ArrayList<>();
		 list.add("Select Code");
		 referedamountarray.add("0");
		 for(int i=0; i<CommonVariables.CODEARRAY.length(); i++){
			try{
			   JSONObject object=CommonVariables.CODEARRAY.getJSONObject(i);
			   Log.e(TAG, "listtttt--"+object);
			   String value=object.getString("invite_code")+" ("+object.getString(
				  "refered_by_amount")+"%)";
			   list.add(value);
			   referedamountarray.add(object.getString(
				  "refered_by_amount"));
			}catch(JSONException e){
			   e.printStackTrace();
			}
		 }
		 Log.e("fdffdffffff", ""+list);
		 //Creating the ArrayAdapter instance having the country list
		 ArrayAdapter aa=new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
		 aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 coupon_spinner.setAdapter(aa);
		 coupon_spinner.setOnItemSelectedListener(this);
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   /*get data from intent*/
   private void getIntentData(){
	  try{
		 Log.e(TAG, ""+getIntent().getStringExtra("activity"));
		 //check intent not null
		 if (getIntent().getExtras()!=null){
			//check previous activity flow
			if (getIntent().getStringExtra("activity").equals("PricingActivity")){
			   fromactivity="PricingActivity";
			   payment_rl.setVisibility(View.VISIBLE);
			   price=getIntent().getStringExtra("price");
//                    pay_price_tv.setText("$ " + price);
			   detail_title_tv.setText("Payment");
			   Subscription_id=getIntent().getStringExtra("Subscription_id");
			   coupon_ll.setVisibility(View.VISIBLE);
			}else{
			   fromactivity="HomeNavigationActivity";
			   coupon_ll.setVisibility(View.GONE);
			}
		 }
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   /*hit show price api
	* url: http://13.59.184.51/api/show-price
	* params: @Authorization, @user_token,@subscription_id
	* */
   private void hitshowPriceApi(String authorizationkey, String token, String subscription_id){
	  Call<ShowPriceBeen> sessionBeenCall;
	  final Dialog pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  pDialog.setCancelable(false);
	  pDialog.show();
	  Log.e(TAG,
			"hitshowPriceApi---"+authorizationkey+"---"+token+"---"+subscription_id);
	  try{
		 sessionBeenCall=ApiClient.getClient().create(ApiInterface.class)
								  .getPrice(authorizationkey, token, subscription_id);
		 sessionBeenCall.enqueue(new Callback<ShowPriceBeen>(){
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(Call<ShowPriceBeen> call, Response<ShowPriceBeen> response){
			   try{
				  Log.e(TAG,
						"hitshowPriceApi--check response---"+new Gson().toJson(response.body()));
				  if (response.body().getCode().equals("1")){
					 if (CommonUtils.isNullOrEmpty(response.body().getData().getPercentOff())){
						pay_price_tv.setText("To Pay: $"+response.body().getData().getPrice());
						finalAmount="To Pay: $"+response.body().getData().getPrice();
					 }else{
						pay_price_tv.setText("To Pay: $"+response.body().getData().getPrice()+
												"\n"+"Applied Discount "+response.body().getData().getPercentOff()+"%");
						finalAmount="To Pay: $"+response.body().getData().getPrice()+
						   "\n"+"Applied Discount "+response.body().getData().getPercentOff()+"%";
					 }
					 setData();
				  }else if (response.body().getCode().equals("0")){
					 Toast.makeText(CardListActivity.this, ""+response.body().getMessage(),
									Toast.LENGTH_SHORT).show();
				  }else if (response.body().getCode().equals("-1")){
					 Toast.makeText(CardListActivity.this, ""+response.body().getMessage(),
									Toast.LENGTH_SHORT).show();
					 //clear local data base
					 ThrillingPicksPrefrences.clearAllData("TPData", CardListActivity.this);
					 //go to login type screen
					 Intent i=new Intent(CardListActivity.this, LoginTypeActivity.class);
					 startActivity(i);
					 finish();
				  }
				  pDialog.dismiss();
			   }catch(Exception e){
				  Log.e(TAG, "Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<ShowPriceBeen> call, Throwable t){
			   Log.e(TAG, "On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 pDialog.dismiss();
		 Log.e(TAG, "error"+ex);
	  }
   }

   /*clickable views*/
   private void clickView(){
	  add_new_card_back.setOnClickListener(this);
	  add_new_card.setOnClickListener(this);
	  save_card_tv.setOnClickListener(this);
	  no_rl.setOnClickListener(this);
	  yes_rl.setOnClickListener(this);
   }

   /*set recyclerview*/
   private void setAdapter(){
	  cardmanager=new LinearLayoutManager(this);
	  card_recycler.setLayoutManager(cardmanager);
	  cardListAdapter=new CardListAdapter(this, cardBeenList, fromactivity, new DeleteCardOnClick(){
		 @Override
		 public void OnItemclick(View view, int Position, String cardId){
			if (CommonUtils.isConnectingToInternet(CardListActivity.this)){
			   deleteCard(CommonVariables.TOKEN, cardId, Position);
			}else{
			   //show alert message in toast
			   Toast.makeText(CardListActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			}
		 }
	  }, new CardOnClick(){
		 @Override
		 public void ItemOnClick(View view, int Position, String cardID){
			if (fromactivity.equals("PricingActivity")){
			   selected=Position;
			   cardListAdapter.notifyDataSetChanged();
			   card_id=cardID;
			}else{
			   String card_number="", exp_month="", exp_year="", user_name="";
			   Log.e(TAG, "get_card:--"+cardBeenList.get(Position).getCardLastFourNumber());
			   card_number=cardBeenList.get(Position).getCardLastFourNumber();
			   exp_month=String.valueOf(cardBeenList.get(Position).getCardExpMonth());
			   exp_year=String.valueOf(cardBeenList.get(Position).getCardExpYear());
			   user_name=String.valueOf(cardBeenList.get(Position).getName());
			   payment_rl.setVisibility(View.GONE);
			   //go to add new card screen
			   Intent intent=new Intent(CardListActivity.this, AddNewCardActivity.class);
			   intent.putExtra("activity", "3");
			   intent.putExtra("cardID", cardID);
			   intent.putExtra("card_number", card_number);
			   intent.putExtra("exp_month", exp_month);
			   intent.putExtra("exp_year", exp_year);
			   intent.putExtra("user_name", user_name);
			   startActivity(intent);
			}
		 }
	  }, new CardOnSlide(){
		 @Override
		 public void onCardSlide(int Poistion){
			cardOpen=Poistion;
			cardListAdapter.notifyDataSetChanged();
		 }
	  });
	  card_recycler.setAdapter(cardListAdapter);
   }

   /*hit session api*/
   public void verifyCoponApi(String token, String code_id, String subscription_id, final int pos){
	  Call<JsonObject> sessionBeenCall;
	  pDialog.show();
	  Log.e(TAG, "SessionApi---"+token+"---"+CommonVariables.AUTHORIZATIONKEY+"----"+code_id+"---"+subscription_id);
	  try{
		 sessionBeenCall=ApiClient.getClient().create(ApiInterface.class)
								  .verifyCoupon(CommonVariables.AUTHORIZATIONKEY, token, code_id, subscription_id);
		 sessionBeenCall.enqueue(new Callback<JsonObject>(){
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
			   try{
				  try{
					 JSONObject jsonObject=new JSONObject(response.body().toString());
					 Log.e(TAG, "check response---"+jsonObject);
					 if (jsonObject.getString("code").equals("1")){
						Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
						JSONObject jsonObject1=jsonObject.getJSONObject("data");
						Log.e(TAG, "check jsonObject1---"+jsonObject1.getString("amount"));
						pay_price_tv.setText("To Pay: $"+jsonObject1.getString("amount")+
												"\n"+"Applied Discount "+referedamountarray.get(pos)+"%");
					 }else if (jsonObject.getString("code").equals("0")){
						Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
					 }else if (jsonObject.getString("code").equals("-1")){
						Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
					 }
					 pDialog.dismiss();
				  }catch(JSONException e1){
					 e1.printStackTrace();
				  }
			   }catch(Exception e){
				  Log.e(TAG, "Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t){
			   Log.e(TAG, "On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 pDialog.dismiss();
		 Log.e(TAG, "error"+ex);
	  }
   }

   @Override
   public void onClick(View v){
	  switch(v.getId()){
		 case R.id.add_new_card_back:
			//back press functionality
			onBackPressed();
			break;
		 case R.id.save_card_tv:
			//check internet connection
			if (CommonUtils.isConnectingToInternet(CardListActivity.this)){
			   if (cardBeenList.size()==0){
				  Toast.makeText(this, "Add Card", Toast.LENGTH_SHORT).show();
			   }else{
				  if (CommonUtils.isNullOrEmpty(card_id)){
					 Toast.makeText(this, "Choose card ", Toast.LENGTH_SHORT).show();
				  }else{
					 //hit make payment api to subscribe paln
					 makePaymentApi(CommonVariables.TOKEN, "0", "", Subscription_id, card_id, PromoId);
				  }
			   }
			}else{
			   //show alert message in toast
			   Toast.makeText(CardListActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			}
			break;
		 case R.id.add_new_card:
			//go to add new card screen
			if (fromactivity.equals("PricingActivity")){
			   //go to add new card screen
			   Intent intent=new Intent(this, AddNewCardActivity.class);
			   intent.putExtra("activity", "1");
			   intent.putExtra("Subscription_id", Subscription_id);
			   intent.putExtra("PromoId", PromoId);
			   startActivity(intent);
			}else{
			   //go to add new card screen
			   Intent intent=new Intent(this, AddNewCardActivity.class);
			   intent.putExtra("activity", "2");
			   startActivity(intent);
			}
			break;
		 case R.id.yes_rl:
			no_checked.setVisibility(View.GONE);
			no_unchecked.setVisibility(View.VISIBLE);
			yes_checked.setVisibility(View.VISIBLE);
			yes_unchecked.setVisibility(View.GONE);
			single_spinner_layout.setVisibility(View.VISIBLE);
			setData();
			break;
		 case R.id.no_rl:
			no_checked.setVisibility(View.VISIBLE);
			no_unchecked.setVisibility(View.GONE);
			yes_checked.setVisibility(View.GONE);
			yes_unchecked.setVisibility(View.VISIBLE);
			single_spinner_layout.setVisibility(View.GONE);
			pay_price_tv.setText(finalAmount);
			PromoId="";
			break;
	  }
   }

   @Override
   protected void onResume(){
	  super.onResume();
	  selected=-1;
	  //check internet conection validation
	  if (CommonUtils.isConnectingToInternet(this)){
		 //hit sign in api
		 sessionApi(CommonVariables.TOKEN);
	  }else{
		 //show message in toast
		 Toast.makeText(this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
	  }
   }

   /*back press functionality*/
   @Override
   public void onBackPressed(){
	  super.onBackPressed();
	  finish();
   }

   /*hit session api*/
   public void sessionApi(String token){
	  //initialize progress dialog
	  final Dialog pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  pDialog.setCancelable(false);
	  pDialog.show();
	  Call<SessionBeen> sessionBeenCall;
	  Log.e(TAG, "SessionApi---"+token+"---"+CommonVariables.AUTHORIZATIONKEY);
	  try{
		 sessionBeenCall=ApiClient.getClient().create(ApiInterface.class)
								  .sessionLogin(CommonVariables.AUTHORIZATIONKEY, CommonVariables.TOKEN);
		 sessionBeenCall.enqueue(new Callback<SessionBeen>(){
			@Override
			public void onResponse(Call<SessionBeen> call, Response<SessionBeen> response){
			   try{
				  //check code equal to 200
				  if (response.code()==200){
					 //check success code equal to 1
					 if (response.body().getCode().equals("1")){
						Log.e(TAG, "session--response--1-"+new Gson().toJson(response.body().getData()));
						//clear local data base
						ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, CardListActivity.this);
						//store data in local database
						ThrillingPicksPrefrences.storeUserPreferences(CardListActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
						getCardApi(CommonVariables.TOKEN);
						if (getIntent().getExtras()!=null){
						   //check previous activity flow
						   if (getIntent().getStringExtra("activity").equals("PricingActivity")){
							  hitshowPriceApi(CommonVariables.AUTHORIZATIONKEY, CommonVariables.TOKEN,
											  Subscription_id);
						   }
						}
					 }
					 //check code equal to 0
					 else if (response.body().getCode().equals("0")){
						Log.e(TAG, "session--response-0--"+new Gson().toJson(response.body().getData()));
						Toast.makeText(CardListActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 }
					 //check code equal to -1
					 else if (response.body().getCode().equals("-1")){
						Log.e(TAG,
							  "session--response--22--"+new Gson().toJson(response.body().getData()));
						//clear local data base
						ThrillingPicksPrefrences.clearAllData("TPData", CardListActivity.this);
						//go to login type api
						Intent i=new Intent(CardListActivity.this, LoginTypeActivity.class);
						startActivity(i);
						finish();
					 }
				  }
				  //check response code equal to 401
				  else if (response.code()==401){
					 JSONObject jsonObject=new JSONObject(response.errorBody().string());
					 Log.e(TAG, jsonObject.getString("message"));
				  }
				  pDialog.dismiss();
			   }catch(Exception e){
				  Log.e(TAG, "Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<SessionBeen> call, Throwable t){
			   Log.e(TAG, "On failure"+t);
			   t.printStackTrace();
			   pDialog.dismiss();
			}
		 });
	  }catch(Exception ex){
		 pDialog.dismiss();
		 Log.e(TAG, "error"+ex);
		 ex.printStackTrace();
	  }
   }

   /*hit make Payment api*/
   public void makePaymentApi(String token, String save_card, String stripe_token, String subscription_id, String card_id, String ApplyCode){
	  //initialize progress dialog
	  final Dialog pDialog=new Dialog(this, android.R.style.Theme_Translucent);
	  pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  pDialog.setContentView(R.layout.custom_progress_dialog);
	  pDialog.setCancelable(false);
	  //show progress dialog
	  pDialog.show();
	  Call<JsonObject> signinBeenCall;
	  Log.e(TAG, "makePaymentApi---"+token+"--"+save_card+"---"+stripe_token+"---"+subscription_id+"---"+card_id+"---"+CommonVariables.AUTHORIZATIONKEY+"---"+ApplyCode);
	  try{
		 signinBeenCall=ApiClient.getClient().create(ApiInterface.class)
								 .makePayment(CommonVariables.AUTHORIZATIONKEY, token, save_card, stripe_token, subscription_id, card_id, ApplyCode);
		 signinBeenCall.enqueue(new Callback<JsonObject>(){
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
			   try{
				  Log.e(TAG, "makePaymentApi--response---"+response.body());
				  JSONObject jsonObject=new JSONObject(response.body().toString());
				  //check code equal to 1
				  if (jsonObject.getString("code").equals("1")){
					 Log.e(TAG, "makePaymentApi--response---"+new Gson().toJson(response.body()));
					 //show sucess messagein toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            //  hit session api
//                            sessionApi(CommonVariables.TOKEN);
					 //go to home screen
					 Intent intent=new Intent(CardListActivity.this, HomeNavigationActivity.class);
					 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					 startActivity(intent);
					 finish();
				  }
				  //check code equal to 0
				  else if (jsonObject.getString("code").equals("0")){
					 Log.e(TAG, "makePaymentApi--response---"+new Gson().toJson(response.body()));
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
				  }
				  //check code is equal to -1
				  else if (jsonObject.getString("code").equals("-1")){
					 Log.e(TAG, "makePaymentApi--response---"+new Gson().toJson(response.body()));
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
				  }
				  pDialog.dismiss();
			   }catch(Exception e){
				  Log.e(TAG, "makePaymentApi--Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t){
			   pDialog.dismiss();
			   Log.e(TAG, "makePaymentApi--On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "error"+ex);
		 ex.printStackTrace();
	  }
   }

   /*hit signin api*/
   public void getCardApi(final String UserToken){
	  pDialog.show();
	  Call<GetCardBeen> signinBeenCall;
	  Log.e(TAG, "getCardApi---"+UserToken+"---"+CommonVariables.AUTHORIZATIONKEY);
	  try{
		 signinBeenCall=ApiClient.getClient().create(ApiInterface.class)
								 .getCard(CommonVariables.AUTHORIZATIONKEY, UserToken);
		 signinBeenCall.enqueue(new Callback<GetCardBeen>(){
			@Override
			public void onResponse(Call<GetCardBeen> call, Response<GetCardBeen> response){
			   try{
				  //check code is equal to 1
				  if (response.body().getCode().equals("1")){
					 Log.e(TAG, "getCardApi---response-1--"+new Gson().toJson(response.body()));
					 //initialize array list
					 cardBeenList=new ArrayList<>();
					 //assign data to card been list
					 cardBeenList=response.body().getData();
					 //set recylerview
					 setAdapter();
				  }
				  //check code is equal to 0
				  else if (response.body().getCode().equals("0")){
					 Log.e(TAG, "getCardApi---response--0-"+response.body().getMessage());
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
				  }
				  //check code is equal to -1
				  else if (response.body().getCode().equals("-1")){
					 Log.e(TAG, "getCardApi---response--11-"+response.body().getMessage());
					 //clear data base
					 ThrillingPicksPrefrences.clearAllData("TPData", CardListActivity.this);
					 //show session expire message in toast
					 Toast.makeText(CardListActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
					 //go to login screen
					 Intent intent=new Intent(CardListActivity.this, LoginActivity.class);
					 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					 startActivity(intent);
					 finish();
				  }else{
					 Log.e(TAG, "getCardApi---response--66-"+response.body().getMessage());
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
				  }
				  //dismiss progress dialog
				  pDialog.dismiss();
			   }catch(Exception e){
				  Log.e(TAG, "Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<GetCardBeen> call, Throwable t){
			   getCardErrorApi(UserToken);
			   Log.e(TAG, "On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "error"+ex);
		 ex.printStackTrace();
	  }
   }

   /*hit card error api*/
   public void getCardErrorApi(String UserToken){
	  Call<JsonObject> cardErrorCall;
	  Log.e(TAG, "getCardErrorApi---"+UserToken+"---"+CommonVariables.AUTHORIZATIONKEY);
	  try{
		 cardErrorCall=ApiClient.getClient().create(ApiInterface.class)
								.getCardError(CommonVariables.AUTHORIZATIONKEY, UserToken);
		 cardErrorCall.enqueue(new Callback<JsonObject>(){
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
			   try{
				  Log.e(TAG, "getCardErrorApi---response-1--"+new Gson().toJson(response.body()));
				  //show alert message in toast
				  Toast.makeText(CardListActivity.this, ""+response.body().get("message"), Toast.LENGTH_SHORT).show();
			   }catch(Exception e){
				  Log.e(TAG, "getCardErrorApi--Exception", e);
				  e.printStackTrace();
			   }
			   //dismiss progress dialog
			   pDialog.dismiss();
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t){
			   pDialog.dismiss();
			   Log.e(TAG, "getCardErrorApi--On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "getCardErrorApi--error"+ex);
		 ex.printStackTrace();
	  }
   }

   /*hit delete card api*/
   public void deleteCard(String UserToken, String CardId, final int position){
	  pDialog.show();
	  Call<JsonObject> deleteCardCall;
	  Log.e(TAG, "deleteCard--params--"+UserToken+"---"+CardId+"---"+CommonVariables.AUTHORIZATIONKEY);
	  try{
		 deleteCardCall=ApiClient.getClient().create(ApiInterface.class)
								 .deleteCard(CommonVariables.AUTHORIZATIONKEY, UserToken, CardId);
		 deleteCardCall.enqueue(new Callback<JsonObject>(){
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
			   try{
				  JSONObject jsonObject=new JSONObject(response.body().toString());
				  if (jsonObject.getString("code").equals("1")){
					 Log.e(TAG, "deleteCard---response---"+response.body());
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
					 //remove item from cardlist
					 cardBeenList.remove(position);
					 //update list after delete card
					 cardListAdapter.notifyItemRemoved(position);
					 //update card list postions and size
					 cardListAdapter.notifyItemRangeChanged(position, cardBeenList.size());
				  }
				  //check code equal to 0
				  else if (jsonObject.getString("code").equals("0")){
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
				  }
				  //check code equal to -1
				  else if (jsonObject.getString("code").equals("-1")){
					 //clear data base
					 ThrillingPicksPrefrences.clearAllData("TPData", CardListActivity.this);
					 //show session expire message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
					 //go to login screen
					 Intent intent=new Intent(CardListActivity.this, LoginActivity.class);
					 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					 startActivity(intent);
					 finish();
				  }else{
					 //show alert message in toast
					 Toast.makeText(CardListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
				  }
				  //dismiss progress dialog
				  pDialog.dismiss();
			   }catch(Exception e){
				  Log.e(TAG, "deleteCard--Exception", e);
				  e.printStackTrace();
			   }
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t){
			   pDialog.dismiss();
			   Log.e(TAG, "deleteCard--On failure"+t);
			   t.printStackTrace();
			}
		 });
	  }catch(Exception ex){
		 Log.e(TAG, "deleteCard--error"+ex);
		 ex.printStackTrace();
	  }
   }

   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
//        Toast.makeText(getApplicationContext(), list.get(position), Toast.LENGTH_LONG).show();
	  try{
		 if (position>0){
			try{
			   int kk=0;
			   JSONObject object=CommonVariables.CODEARRAY.getJSONObject(position-1);
			   PromoId=object.getString("id");
			   Log.e(TAG, "promo id---"+PromoId);
			   Log.e(TAG, "promo id---"+price);
			   if (CommonUtils.isConnectingToInternet(CardListActivity.this)){
				  verifyCoponApi(CommonVariables.TOKEN, PromoId, Subscription_id, position);
			   }else{
				  //show alert message in toast
				  Toast.makeText(CardListActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
			   }
			}catch(JSONException e){
			   e.printStackTrace();
			}
		 }else{
//                pay_price_tv.setText("$ " + price);
			pay_price_tv.setText(finalAmount);
			PromoId="";
		 }
	  }catch(Exception e){
		 e.printStackTrace();
	  }
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent){
   }
}
