package com.thrillingpicks.utils;

import android.app.Application;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class AppControllerClass extends Application{

   public static final String TAG = AppControllerClass.class
	  .getSimpleName();

   @Override
   public void onCreate() {
	  super.onCreate();
	  FirebaseApp.initializeApp(getApplicationContext());
	  Fabric fabric=new Fabric.Builder(this).kits(new Crashlytics()).
		 debuggable(BuildConfig.DEBUG) // Enables Crashlytics debugger
		 .build();
	  Fabric.with(fabric);
	  // TODO: Move this to where you establish a user session
	  logUser();
   }
   private void logUser() {
	  // TODO: Use the current user's information
	  // You can call any combination of these three methods
	  Crashlytics.setUserIdentifier("12345");
	  Crashlytics.setUserEmail("user@fabric.io");
	  Crashlytics.setUserName("Test User");
   }




}
