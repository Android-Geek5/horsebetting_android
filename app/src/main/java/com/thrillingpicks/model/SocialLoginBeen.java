package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginBeen {

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
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("user_google_id")
        @Expose
        private String userGoogleId;
        @SerializedName("user_facebook_id")
        @Expose
        private Object userFacebookId;
        @SerializedName("stripe_customer_id")
        @Expose
        private Object stripeCustomerId;
        @SerializedName("email_verified_at")
        @Expose
        private Object emailVerifiedAt;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("promocode")
        @Expose
        private String promocode;
        @SerializedName("user_profile_image")
        @Expose
        private Object userProfileImage;
        @SerializedName("security_hash")
        @Expose
        private String securityHash;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("user_subscription_name")
        @Expose
        private Object userSubscriptionName;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user_subscription_start_date")
        @Expose
        private Object userSubscriptionStartDate;
        @SerializedName("user_subscription_end_date")
        @Expose
        private Object userSubscriptionEndDate;
        @SerializedName("user_token")
        @Expose
        private String userToken;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserGoogleId() {
            return userGoogleId;
        }

        public void setUserGoogleId(String userGoogleId) {
            this.userGoogleId = userGoogleId;
        }

        public Object getUserFacebookId() {
            return userFacebookId;
        }

        public void setUserFacebookId(Object userFacebookId) {
            this.userFacebookId = userFacebookId;
        }

        public Object getStripeCustomerId() {
            return stripeCustomerId;
        }

        public void setStripeCustomerId(Object stripeCustomerId) {
            this.stripeCustomerId = stripeCustomerId;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPromocode() {
            return promocode;
        }

        public void setPromocode(String promocode) {
            this.promocode = promocode;
        }

        public Object getUserProfileImage() {
            return userProfileImage;
        }

        public void setUserProfileImage(Object userProfileImage) {
            this.userProfileImage = userProfileImage;
        }

        public String getSecurityHash() {
            return securityHash;
        }

        public void setSecurityHash(String securityHash) {
            this.securityHash = securityHash;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public Object getUserSubscriptionName() {
            return userSubscriptionName;
        }

        public void setUserSubscriptionName(Object userSubscriptionName) {
            this.userSubscriptionName = userSubscriptionName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getUserSubscriptionStartDate() {
            return userSubscriptionStartDate;
        }

        public void setUserSubscriptionStartDate(Object userSubscriptionStartDate) {
            this.userSubscriptionStartDate = userSubscriptionStartDate;
        }

        public Object getUserSubscriptionEndDate() {
            return userSubscriptionEndDate;
        }

        public void setUserSubscriptionEndDate(Object userSubscriptionEndDate) {
            this.userSubscriptionEndDate = userSubscriptionEndDate;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

    }

}

