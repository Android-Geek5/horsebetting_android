package com.thrillingpicks.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thrillingpicks.R;
import com.thrillingpicks.model.EditInfoBeen;
import com.thrillingpicks.model.EditInfoBeen;
import com.thrillingpicks.model.EditInfoBeen;
import com.thrillingpicks.model.SessionBeen;
import com.thrillingpicks.retrofit.ApiClient;
import com.thrillingpicks.retrofit.ApiInterface;
import com.thrillingpicks.utils.CommonUtils;
import com.thrillingpicks.utils.CommonVariables;
import com.thrillingpicks.utils.ThrillingPicksPrefrences;
import com.thrillingpicks.views.activities.home.HomeNavigationActivity;
import com.thrillingpicks.views.activities.signUpFlow.LoginActivity;
import com.thrillingpicks.views.activities.signUpFlow.SignUpActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = AccountActivity.class.getSimpleName();
    ImageView edit_profile, save_profile, account_back;
    EditText user_email, user_name;
    TextView logout_tv, user_dob, account_user_name;
    private DatePicker datePicker;
    int Dday = 0, Dmonth = 0, Dyear = 0;
    private String date_of_birth = "";
    private Dialog uploadDialog;
    LinearLayout upload_image_ll;
    private static int SELECT_GALLERY = 0;
    private static int REQUEST_CAMERA = 1;
    CircleImageView account_user_image;
    private MultipartBody.Part profileImage = null;
    RequestBody updated_user_name = null, updated_user_email = null, updated_dob = null, token = null;
    String path;
    int date_picker_month = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //method call to initialize views
        initView();
    }

    /*initaliz views*/
    private void initView() {
        edit_profile = findViewById(R.id.edit_profile);
        user_email = findViewById(R.id.user_email);
        account_user_name = findViewById(R.id.account_user_name);
        user_name = findViewById(R.id.user_name);
        save_profile = findViewById(R.id.save_profile);
        logout_tv = findViewById(R.id.logout_tv);
        account_back = findViewById(R.id.account_back);
        user_dob = findViewById(R.id.user_dob);
        upload_image_ll = findViewById(R.id.upload_image_ll);
        account_user_image = findViewById(R.id.account_user_image);
        user_email.setEnabled(false);
        //to implement click listener on views
        clickView();
        //to set user profile data in feilds
        setProfileData();
    }

    /*get and set user profile data in feilds*/
    private void setProfileData() {
        try {
            user_name.setText("" + CommonVariables.NAME);
            account_user_name.setText("" + CommonVariables.NAME);
            user_email.setText("" + CommonVariables.EMAIL);
            user_email.setEnabled(false);
            Log.e(TAG, "setProfileData--dob--" + CommonVariables.DOB);
            String splitstring[] = CommonVariables.DOB.split("-");
            Dyear = Integer.parseInt(splitstring[0]);
            Dmonth = Integer.parseInt(splitstring[1]);
            date_picker_month = Integer.parseInt(splitstring[1]);
            Dday = Integer.parseInt(splitstring[2]);
            String month = "", day = "";
            //check days is less ten 10 or not
            if (Dday < 10) {
                day = "0" + Dday;
            } else {
                day = String.valueOf(Dday);
            }
            //check month is less then 10
            if (Dmonth < 10) {

                month = "0" + Dmonth;

            } else {
                month = String.valueOf(Dmonth);
            }
            //set user date of birth
            user_dob.setText("" + Dyear + "-" + month + "-" + day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //set user profile image using glide
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.img_horse)
                    .error(R.drawable.img_horse)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();

            Glide.with(AccountActivity.this)
                    .load(CommonVariables.IMAGEURL)
                    .apply(options)
                    .into(account_user_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*initialize clickable views*/
    private void clickView() {
        edit_profile.setOnClickListener(this);
        save_profile.setOnClickListener(this);
        logout_tv.setOnClickListener(this);
        account_back.setOnClickListener(this);
        upload_image_ll.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
//                user_email.setEnabled(true);
                user_name.setEnabled(true);
                save_profile.setVisibility(View.VISIBLE);
                edit_profile.setVisibility(View.GONE);
                user_dob.setOnClickListener(this);
                break;
            case R.id.save_profile:
                user_email.setEnabled(false);
                user_name.setEnabled(false);
                save_profile.setVisibility(View.GONE);
                edit_profile.setVisibility(View.VISIBLE);
                user_dob.setOnClickListener(null);
                break;
            case R.id.logout_tv:
                //check button text is equal to edit or not
                if (logout_tv.getText().equals("Edit")) {
//                    user_email.setEnabled(true);
                    user_name.setEnabled(true);
                    user_dob.setOnClickListener(this);
                    logout_tv.setText("Save");
                } else {
                    //check feilds validation
                    checkValidation();
                }
                break;
            case R.id.account_back:
                //perform back press functionality
                onBackPressed();
                break;
            case R.id.user_dob:
                //method call to show date picker dialog
                selectDateofBirth();
                break;
            case R.id.upload_image_ll:
                //to upload image
                uploadFiles();
                break;
        }
    }

    /*--date picker--*/
    private void selectDateofBirth() {
        try {
            final View datepickerView = getLayoutInflater().inflate(R.layout.date_picker, null);
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

            alert.setTitle(getString(R.string.set_dob_string));

            // this is set the view from XML inside AlertDialog
            alert.setView(datepickerView);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            datePicker = (DatePicker) datepickerView.findViewById(R.id.datepicker);
            //set date picker maximum select date
            datePicker.setMaxDate(new Date().getTime());

            datePicker.updateDate(Dyear, Dmonth - 1, Dday);
            alert.setNegativeButton(getString(R.string.cancel_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.setPositiveButton(getString(R.string.set_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int month = datePicker.getMonth() + 1;
                    int dd = datePicker.getDayOfMonth();
                    int mm = datePicker.getMonth() + 1;
                    int yy = datePicker.getYear();
                    String dateofmonth, monthofyear;
                    dateofmonth = String.valueOf(dd);
                    monthofyear = String.valueOf(mm);
                    //check day is less then 10
                    if (dd < 10) {
                        dateofmonth = "0" + dd;
                        dd = Integer.parseInt(dateofmonth);
                    }
                    //check month is less then 10
                    if (mm < 10) {
                        monthofyear = "0" + mm;
                        mm = Integer.parseInt(monthofyear);

                    }
                    //set date in date feild
                    String dob = monthofyear + "-" + dateofmonth + "-" + yy;
                    Log.e("check_date_datepicker", "" + dob);
                    date_of_birth = yy + "-" + monthofyear + "-" + dateofmonth;

                    Dyear = yy;
                    Dmonth = Integer.parseInt(monthofyear);
                    date_picker_month = Integer.parseInt(monthofyear) - 1;
                    Dday = Integer.parseInt(dateofmonth);
                    user_dob.setText(date_of_birth);
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

    /*---Back press---*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*---method for upload files using camera and gallery---*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadFiles() {
        try {
            uploadDialog = new Dialog(AccountActivity.this, R.style.DialogSlideAnim);
            Objects.requireNonNull(uploadDialog.getWindow())
                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            uploadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            uploadDialog.setContentView(R.layout.dialog_upload_image);
            uploadDialog.setCancelable(false);
            TextView dialogTitle = uploadDialog.findViewById(R.id.dialog_title);
            LinearLayout linearUploadImage = uploadDialog.findViewById(R.id.linear_upload_image);
            LinearLayout linearUploadCamera = uploadDialog.findViewById(R.id.linear_upload_camera);
            LinearLayout textCancel = uploadDialog.findViewById(R.id.text_cancel);
            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadDialog.dismiss();

                }
            });
            linearUploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    getImageFromGallery();

                }
            });
            linearUploadCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickImageFromCamera();
                }
            });
            uploadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*----check permission---*/
    private boolean checkPermission() {
        int result =
                ContextCompat.checkSelfPermission(AccountActivity.this, READ_EXTERNAL_STORAGE);
        int result1 =
                ContextCompat.checkSelfPermission(AccountActivity.this, WRITE_EXTERNAL_STORAGE);
        int result2 =
                ContextCompat.checkSelfPermission(AccountActivity.this, CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    /*--request runtime permission--*/
    private void requestPermission() {
        ActivityCompat.requestPermissions(AccountActivity.this,
                new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, 1);
    }

    /*-----Choose image from camera------*/
    public void clickImageFromCamera() {
        try {
            boolean result = checkPermission();
            if (result) {
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (camIntent.resolveActivity(AccountActivity.this.getPackageManager()) != null) {
                    startActivityForResult(camIntent, REQUEST_CAMERA);
                    uploadDialog.dismiss();
                }
            } else {
                requestPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(checkPermission()){
//
//        }else {
//            requestPermission();
//        }
//    }

    /*-----Choose image from gallery------*/
    public void getImageFromGallery() {
        try {
            boolean result = checkPermission();
            if (result) {
                Intent galIntent =
                        new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galIntent, "Select Image From Gallery"),
                        SELECT_GALLERY);
                uploadDialog.dismiss();
            } else {
                requestPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*----on activity result----*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                imageSelectFromCamera(data);
            }
        } else if (requestCode == SELECT_GALLERY) {
            if (data != null) {
                imageSelectedFromGallery(data);
            }
        }
        user_email.setEnabled(false);
        user_name.setEnabled(true);
        user_dob.setOnClickListener(this);
        logout_tv.setText("Save");
    }

    /*---image from camera----*/
    private void imageSelectFromCamera(Intent data) {
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            final File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            path = destination.getAbsoluteFile().toString();

            File file = new File(path);
            Log.e(TAG, "image path---" + path);
            RequestBody requestFile = RequestBody
                    .create(MediaType.parse("multipart/form-data"), file);
            profileImage = MultipartBody.Part
                    .createFormData("user_profile_image", file.getName(), requestFile);
            Log.e(TAG, "imageSelectFromCamera-11-" + file);
            Glide.with(this)
                    .load(thumbnail)
                    .into(account_user_image);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*--image from gallery----*/
    private void imageSelectedFromGallery(Intent data) {
        try {
            Uri selectedImage = data.getData();
            //set image in image view
            Glide.with(this)
                    .load(selectedImage)
                    .into(account_user_image);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            final String picturePath = c.getString(columnIndex);
            c.close();

            path = picturePath;
            Log.e(TAG, "image path---" + path);
            Log.e(TAG, "imageSelectedFromGallery" + path);
            File file1 = new File(path);
            Log.e(TAG, "imageSelectedFromGallery--" + file1.getName());
            //convert image file into multipart
            RequestBody requestFile = RequestBody
                    .create(MediaType.parse("multipart/form-data"), file1);
            profileImage = MultipartBody.Part
                    .createFormData("user_profile_image", file1.getName(), requestFile);
            Log.e(TAG, "imageSelectFromCamera-99999-" + file1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*hit session api*/
    public void sessionApi(String token) {
        //initalize progress dialog
        final Dialog pDialog = new Dialog(AccountActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
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
                            ThrillingPicksPrefrences.clearAllData(CommonUtils.USER, AccountActivity.this);
                            //store data in local database
                            ThrillingPicksPrefrences.storeUserPreferences(AccountActivity.this, CommonUtils.USER, new Gson().toJson(response.body().getData()));
                            //check internet connection
                            if (CommonUtils.isConnectingToInternet(AccountActivity.this)) {
                                //set profile data
                                setProfileData();


                            } else {
                                //show message in toast
                                Toast.makeText(AccountActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
                            }
                            //check code is equal to 0
                        } else if (response.body().getCode().equals("0")) {
                            Log.e(TAG, "session--response-0--" + new Gson().toJson(response.body().getData()));
                            //show alert message in toast
                            Toast.makeText(AccountActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (response.body().getCode().equals("-1")) {
                            Log.e(TAG, "session--response--1--" + new Gson().toJson(response.body().getData()));
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", AccountActivity.this);
                            //show session expire message in toast
                            Toast.makeText(AccountActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //go to login screen
                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show alert message in toast
                            Toast.makeText(AccountActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    /*change dat into multipart */
    private void changetoMultipart() {
        try {
            //convert token into requestbody type
            token = RequestBody.create(MediaType.parse("text/plain"), CommonVariables.TOKEN);
            String name = user_name.getText().toString().trim();
            //convert user name into requestbody type
            updated_user_name = RequestBody.create(MediaType.parse("text/plain"), name);
            String email = user_email.getText().toString().trim();
            //convert email into requestbody type
            updated_user_email = RequestBody.create(MediaType.parse("text/plain"), email);
            String dob = Dyear + "-" + Dmonth + "-" + Dday;
            Log.e(TAG, "dob--" + dob);
            //convert date of birth into requestbody type
            updated_dob = RequestBody.create(MediaType.parse("text/plain"), dob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //check internet connection
        if (CommonUtils.isConnectingToInternet(AccountActivity.this)) {
            //method call to hit update account info api
            updateAccoutinfoApi(token, updated_user_name, updated_user_email, updated_dob, profileImage);
        } else {
            //show message in toast
            Toast.makeText(AccountActivity.this, getString(R.string.no_internet_string), Toast.LENGTH_SHORT).show();
        }

    }

    /*check feild validation*/
    private void checkValidation() {
        try {
            String name = "", email = "", dob = "";
            //check name feild not null validation
            if (CommonUtils.validateForNull(this, user_name, getString(R.string.enter_name))) {
                //check name pattren validation
                if (CommonUtils.checkNameValidation(AccountActivity.this, user_name, getString(R.string.enter_valid_name))) {
                    //check email not null validation
                    if (CommonUtils.validateForNull(this, user_email, getString(R.string.enter_email_msg_string))) {
                        //check email pattren validation
                        if (CommonUtils.validateEmail(this, user_email, getString(R.string.enter_valid_email_msg_string), getString(R.string.enter_valid_email_msg_string))) {
                            //check date of birth not null validation
                            if (!user_dob.getText().toString().matches("")) {
                                changetoMultipart();
                            } else {
                                //show message in toast
                                Toast.makeText(this, getString(R.string.enter_dob), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*update account info api hit*/
    public void updateAccoutinfoApi(RequestBody token, RequestBody name, RequestBody email, RequestBody dob, MultipartBody.Part user_profile_image) {
        //initialize progress dialog
        final Dialog pDialog = new Dialog(AccountActivity.this, android.R.style.Theme_Translucent);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_progress_dialog);
        pDialog.setCancelable(false);
        //show progress dialog
        pDialog.show();
        Call<JsonObject> editInfoBeenCall;
        Log.e(TAG, "updateAccoutinfoApi---" + "---" + token + "--" + name + "--" + email + "--" + dob + "--" + user_profile_image);
        Log.e(TAG, "updateAccoutinfoApi-9900--" + "---" + CommonVariables.TOKEN + "---" + user_name.getText().toString().trim() + "--" + user_email.getText().toString().trim() + "--" +
                Dyear + "-" + Dmonth + "-" + Dday + "---" + path + "---" + CommonVariables.AUTHORIZATIONKEY);
        try {
            editInfoBeenCall = ApiClient.getClient().create(ApiInterface.class)
                    .editInfo(CommonVariables.AUTHORIZATIONKEY, token, name, email, dob, user_profile_image);
            editInfoBeenCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e(TAG, "check edit profile response:--" + jsonObject);
                        //check code equal to 1
                        if (jsonObject.getString("code").equals("1")) {
                            Log.e(TAG, "updateAccoutinfoApi--response--1-" + jsonObject.getString("data"));
                            //show success message in toast
                            Toast.makeText(AccountActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", AccountActivity.this);
                            //store data in local database
                            ThrillingPicksPrefrences.storeUserPreferences(AccountActivity.this, CommonUtils.USER, jsonObject.getString("data"));
                            profileImage = null;
                            logout_tv.setText("Edit");
                            //set views clickable false after update
                            user_email.setEnabled(false);
                            user_name.setEnabled(false);
                            user_dob.setOnClickListener(null);
                            sessionApi(CommonVariables.TOKEN);
                        }
                        //check code equal to 0
                        else if (jsonObject.getString("code").equals("0")) {
                            Log.e(TAG, "updateAccoutinfoApi--response-0--" + new Gson().toJson(response.body()));
                            //show message in toast
                            Toast.makeText(AccountActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //check code is equal to -1
                        else if (jsonObject.getString("code").equals("-1")) {
                            Log.e(TAG, "updateAccoutinfoApi--response--3--" + new Gson().toJson(response.body()));
                            //show message in toast
                            Toast.makeText(AccountActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //clear local data base
                            ThrillingPicksPrefrences.clearAllData("TPData", AccountActivity.this);
                            //go to login screen
                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //show message in toast
                            Toast.makeText(AccountActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //dismiss progress dialog
                        pDialog.dismiss();

                    } catch (Exception e) {
                        Log.e(TAG, "updateAccoutinfoApi--Exception", e);
                        pDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e(TAG, "updateAccoutinfoApi--On failure" + t);
                    t.printStackTrace();

                }
            });
        } catch (Exception ex) {
            pDialog.dismiss();
            Log.e(TAG, "updateAccoutinfoApi--error" + ex);
            ex.printStackTrace();
            pDialog.dismiss();
        }
    }

}
