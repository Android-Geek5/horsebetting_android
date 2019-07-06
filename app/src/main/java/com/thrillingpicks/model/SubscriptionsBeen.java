package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionsBeen {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public static class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("subscription_plan_id")
        @Expose
        private String subscriptionPlanId;
        @SerializedName("subscription_name")
        @Expose
        private String subscriptionName;
        @SerializedName("subscription_price")
        @Expose
        private String subscriptionPrice;
        @SerializedName("subscription_slug")
        @Expose
        private String subscriptionSlug;
        @SerializedName("subscription_interval")
        @Expose
        private String subscriptionInterval;
        @SerializedName("subscription_validity")
        @Expose
        private Integer subscriptionValidity;
        @SerializedName("subscription_type")
        @Expose
        private String subscriptionType;

        @SerializedName("subscription_description")
        @Expose
        private String subscriptionDescription;
        @SerializedName("subscription_status")
        @Expose
        private String subscriptionStatus;
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

        public String getSubscriptionPlanId() {
            return subscriptionPlanId;
        }

        public void setSubscriptionPlanId(String subscriptionPlanId) {
            this.subscriptionPlanId = subscriptionPlanId;
        }

        public String getSubscriptionName() {
            return subscriptionName;
        }

        public void setSubscriptionName(String subscriptionName) {
            this.subscriptionName = subscriptionName;
        }

        public String getSubscriptionPrice() {
            return subscriptionPrice;
        }

        public void setSubscriptionPrice(String subscriptionPrice) {
            this.subscriptionPrice = subscriptionPrice;
        }

        public String getSubscriptionSlug() {
            return subscriptionSlug;
        }

        public void setSubscriptionSlug(String subscriptionSlug) {
            this.subscriptionSlug = subscriptionSlug;
        }

        public String getSubscriptionInterval() {
            return subscriptionInterval;
        }

        public void setSubscriptionInterval(String subscriptionInterval) {
            this.subscriptionInterval = subscriptionInterval;
        }

        public Integer getSubscriptionValidity() {
            return subscriptionValidity;
        }

        public void setSubscriptionValidity(Integer subscriptionValidity) {
            this.subscriptionValidity = subscriptionValidity;
        }

        public String getSubscriptionType() {
            return subscriptionType;
        }

        public void setSubscriptionType(String subscriptionType) {
            this.subscriptionType = subscriptionType;
        }

        public String getSubscriptionDescription() {
            return subscriptionDescription;
        }

        public void setSubscriptionDescription(String subscriptionDescription) {
            this.subscriptionDescription = subscriptionDescription;
        }

        public String getSubscriptionStatus() {
            return subscriptionStatus;
        }

        public void setSubscriptionStatus(String subscriptionStatus) {
            this.subscriptionStatus = subscriptionStatus;
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