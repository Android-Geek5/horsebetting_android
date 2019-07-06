package com.thrillingpicks.retrofit;


import com.google.gson.JsonObject;
import com.thrillingpicks.model.GetCardBeen;
import com.thrillingpicks.model.InvitedBeen;
import com.thrillingpicks.model.LogoutBeen;
import com.thrillingpicks.model.PicksBeen;
import com.thrillingpicks.model.RecentResultBeen;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.model.ShowPriceBeen;
import com.thrillingpicks.model.SigninBeen;
import com.thrillingpicks.model.SignupBeen;
import com.thrillingpicks.model.SocialLoginBeen;
import com.thrillingpicks.model.SocialLoginErrorBeen;
import com.thrillingpicks.model.SubscriptionsBeen;
import com.thrillingpicks.model.TrackRacesBeen;
import com.thrillingpicks.model.TracksBeen;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    Call<SignupBeen> signUp(@Header("Authorization") String Authorization,
                            @Field("name") String name,
                            @Field("email") String email,
                            @Field("email_confirmation") String email_confirmation,
                            @Field("password") String password,
                            @Field("dob") String dob,
                            @Field("promocode") String promocode,
                            @Field("social_media_name") String social_media_name,
                            @Field("social_media_id") String social_media_id);

    @FormUrlEncoded
    @POST("login")
    Call<SigninBeen> signIn(
            @Header("Authorization") String Authorization,
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("session-login")
    Call<SessionBeen> sessionLogin(@Header("Authorization") String Authorization,
                                   @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("logout")
    Call<JsonObject> logout(@Header("Authorization") String Authorization,
                            @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<JsonObject> forgotPassword(@Header("Authorization") String Authorization,
                                            @Field("login_email") String login_email);

    @FormUrlEncoded
    @POST("change-password")
    Call<JsonObject> changePassword(@Header("Authorization") String Authorization,
                                            @Field("user_token") String user_token,
                                            @Field("old_password") String old_password,
                                            @Field("password") String password,
                                            @Field("password_confirmation") String password_confirmation);


    @Multipart
    @POST("edit-profile")
    Call<JsonObject> editInfo(@Header("Authorization") String Authorization,
                                @Part("user_token") RequestBody user_token,
                                @Part("name") RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("dob") RequestBody dob,
                                @Part MultipartBody.Part user_profile_image);

    @FormUrlEncoded
    @POST("subscriptions")
    Call<SubscriptionsBeen> subscription(@Header("Authorization") String Authorization,
                                         @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("tracks")
    Call<TracksBeen> tracks(@Header("Authorization") String Authorization,
                            @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("track-races")
    Call<TrackRacesBeen> trackRaces(@Header("Authorization") String Authorization,
                                    @Field("user_token") String user_token,
                                    @Field("track_id") String track_id,
                                    @Field("date") String date);

    @FormUrlEncoded
    @POST("track-races")
    Call<LogoutBeen> trackRacesError(@Header("Authorization") String Authorization,
                                     @Field("user_token") String user_token,
                                     @Field("track_id") String track_id);

    @FormUrlEncoded
    @POST("picks")
    Call<PicksBeen> picks(@Header("Authorization") String Authorization,
                          @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("picks")
    Call<LogoutBeen> picksError(@Header("Authorization") String Authorization,
                                @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("social-login")
    Call<SocialLoginBeen> socialLogin(@Header("Authorization") String Authorization,
                                      @Field("social_media_id") String social_media_id,
                                      @Field("social_media_name") String social_media_name);

    @FormUrlEncoded
    @POST("social-login")
    Call<SocialLoginErrorBeen> socialLoginError(@Header("Authorization") String Authorization,
                                                @Field("social_media_id") String social_media_id,
                                                @Field("social_media_name") String social_media_name);

    @FormUrlEncoded
    @POST("add-card")
    Call<JsonObject> addCard(@Header("Authorization") String Authorization,
                              @Field("user_token") String user_token,
                              @Field("stripe_token") String stripe_token);

    @FormUrlEncoded
    @POST("get-card")
    Call<GetCardBeen> getCard(@Header("Authorization") String Authorization,
                              @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("get-card")
    Call<JsonObject> getCardError(@Header("Authorization") String Authorization,
                                  @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("delete-card")
    Call<JsonObject> deleteCard(@Header("Authorization") String Authorization,
                                    @Field("user_token") String user_token,
                                    @Field("card_id") String card_id);

    @FormUrlEncoded
    @POST("make-payment")
    Call<JsonObject> makePayment(@Header("Authorization") String Authorization,
                                 @Field("user_token") String user_token,
                                 @Field("save_card") String save_card,
                                 @Field("stripe_token") String stripe_token,
                                 @Field("subscription_id") String subscription_id,
                                 @Field("card_id") String card_id,
                                 @Field("user_code_id") String user_code_id);

    @FormUrlEncoded
    @POST("edit-card")
    Call<JsonObject> editCard(@Header("Authorization") String Authorization,
                              @Field("user_token") String user_token,
                              @Field("card_id") String card_id,
                              @Field("card_exp_month") String card_exp_month,
                              @Field("card_exp_year") String card_exp_year,
                              @Field("name") String name);

    @FormUrlEncoded
    @POST("verify-coupon")
    Call<JsonObject> verifyCoupon(@Header("Authorization") String Authorization,
                              @Field("user_token") String user_token,
                              @Field("code_id") String card_id,
                              @Field("subscription_id") String subscription_id);
    @FormUrlEncoded
    @POST("cancel-subscription")
    Call<JsonObject> cancelSubscription(@Header("Authorization") String Authorization,
                              @Field("user_token") String user_token);

    @GET("get-winning")
    Call<RecentResultBeen> getWinning(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("invite")
    Call<InvitedBeen> getInvited(@Header("Authorization") String Authorization,
                                         @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("show-price")
    Call<ShowPriceBeen> getPrice(@Header("Authorization") String Authorization,
                                 @Field("user_token") String user_token,
                                 @Field("subscription_id") String subscription_id);
}
