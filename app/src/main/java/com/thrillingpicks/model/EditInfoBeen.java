package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditInfoBeen {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("user_token")
        @Expose
        private String userToken;
        @SerializedName("user_profile_image")
        @Expose
        private String userProfileImage;
        @SerializedName("user_profile_image_url")
        @Expose
        private String userProfileImageUrl;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getUserProfileImage() {
            return userProfileImage;
        }

        public void setUserProfileImage(String userProfileImage) {
            this.userProfileImage = userProfileImage;
        }

        public String getUserProfileImageUrl() {
            return userProfileImageUrl;
        }

        public void setUserProfileImageUrl(String userProfileImageUrl) {
            this.userProfileImageUrl = userProfileImageUrl;
        }

    }

}