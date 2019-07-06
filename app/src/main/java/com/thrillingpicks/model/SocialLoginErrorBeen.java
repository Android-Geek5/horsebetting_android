package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginErrorBeen {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Redirect to login")
    @Expose
    private RedirectToLogin redirectToLogin;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RedirectToLogin getRedirectToLogin() {
        return redirectToLogin;
    }

    public void setRedirectToLogin(RedirectToLogin redirectToLogin) {
        this.redirectToLogin = redirectToLogin;
    }
    public  class RedirectToLogin{

    }

}
