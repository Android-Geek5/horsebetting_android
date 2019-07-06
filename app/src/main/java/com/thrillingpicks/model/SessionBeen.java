package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionBeen {

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
        private String userFacebookId;
        @SerializedName("stripe_customer_id")
        @Expose
        private String stripeCustomerId;
        @SerializedName("refered_by_id")
        @Expose
        private String referedById;
        @SerializedName("email_verified_at")
        @Expose
        private String emailVerifiedAt;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("promocode")
        @Expose
        private String promocode;
        @SerializedName("user_profile_image")
        @Expose
        private String userProfileImage;
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
        private String userSubscriptionName;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user_subscription_start_date")
        @Expose
        private String userSubscriptionStartDate;

        @SerializedName("user_subscription_end_date")
        @Expose
        private String userSubscriptionEndDate;
        @SerializedName("user_token")
        @Expose
        private String userToken;

        public String getReferedByAmount() {
            return referedByAmount;
        }

        public void setReferedByAmount(String referedByAmount) {
            this.referedByAmount = referedByAmount;
        }

        @SerializedName("refered_to_amount")
        @Expose
        private String referedToAmount;

        @SerializedName("refered_by_amount")
        @Expose
        private String referedByAmount;

        public String getReferedToAmount() {
            return referedToAmount;
        }

        public void setReferedToAmount(String referedToAmount) {
            this.referedToAmount = referedToAmount;
        }

        public String getSubscription_price() {
            return subscription_price;
        }

        public void setSubscription_price(String subscription_price) {
            this.subscription_price = subscription_price;
        }

        @SerializedName("user_subscription_type")
        @Expose
        private String user_subscription_type;
        @SerializedName("subscription_price")
        @Expose
        private String subscription_price;

        public String getUserSubscription_type() {
            return user_subscription_type;
        }

        public void setUserSubscription_type(String user_subscription_type) {
            this.user_subscription_type = user_subscription_type;
        }

        @SerializedName("codes_array")
        @Expose
        private List<CodesArray> codesArray = null;
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

        public String getUserFacebookId() {
            return userFacebookId;
        }

        public void setUserFacebookId(String userFacebookId) {
            this.userFacebookId = userFacebookId;
        }

        public String getStripeCustomerId() {
            return stripeCustomerId;
        }

        public void setStripeCustomerId(String stripeCustomerId) {
            this.stripeCustomerId = stripeCustomerId;
        }

        public String getReferedById() {
            return referedById;
        }

        public void setReferedById(String referedById) {
            this.referedById = referedById;
        }

        public String getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(String emailVerifiedAt) {
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

        public String getUserProfileImage() {
            return userProfileImage;
        }

        public void setUserProfileImage(String userProfileImage) {
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

        public String getUserSubscriptionName() {
            return userSubscriptionName;
        }

        public void setUserSubscriptionName(String userSubscriptionName) {
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

        public String getUserSubscriptionStartDate() {
            return userSubscriptionStartDate;
        }

        public void setUserSubscriptionStartDate(String userSubscriptionStartDate) {
            this.userSubscriptionStartDate = userSubscriptionStartDate;
        }

        public String getUserSubscriptionEndDate() {
            return userSubscriptionEndDate;
        }

        public void setUserSubscriptionEndDate(String userSubscriptionEndDate) {
            this.userSubscriptionEndDate = userSubscriptionEndDate;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public List<CodesArray> getCodesArray() {
            return codesArray;
        }

        public void setCodesArray(List<CodesArray> codesArray) {
            this.codesArray = codesArray;
        }

        public String getUserProfileImageUrl() {
            return userProfileImageUrl;
        }

        public void setUserProfileImageUrl(String userProfileImageUrl) {
            this.userProfileImageUrl = userProfileImageUrl;
        }

        public class CodesArray {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("refered_by_user_id")
            @Expose
            private Integer referedByUserId;
            @SerializedName("refered_to_user_id")
            @Expose
            private Integer referedToUserId;
            @SerializedName("invite_code")
            @Expose
            private String inviteCode;
            @SerializedName("discount")
            @Expose
            private String discount;
            @SerializedName("refered_by_amount")
            @Expose
            private String referedByAmount;
            @SerializedName("refered_to_amount")
            @Expose
            private String referedToAmount;
            @SerializedName("user_code_status")
            @Expose
            private String userCodeStatus;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getReferedByUserId() {
                return referedByUserId;
            }

            public void setReferedByUserId(Integer referedByUserId) {
                this.referedByUserId = referedByUserId;
            }

            public Integer getReferedToUserId() {
                return referedToUserId;
            }

            public void setReferedToUserId(Integer referedToUserId) {
                this.referedToUserId = referedToUserId;
            }

            public String getInviteCode() {
                return inviteCode;
            }

            public void setInviteCode(String inviteCode) {
                this.inviteCode = inviteCode;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getReferedByAmount() {
                return referedByAmount;
            }

            public void setReferedByAmount(String referedByAmount) {
                this.referedByAmount = referedByAmount;
            }

            public String getReferedToAmount() {
                return referedToAmount;
            }

            public void setReferedToAmount(String referedToAmount) {
                this.referedToAmount = referedToAmount;
            }

            public String getUserCodeStatus() {
                return userCodeStatus;
            }

            public void setUserCodeStatus(String userCodeStatus) {
                this.userCodeStatus = userCodeStatus;
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

        }
    }
}