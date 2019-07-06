package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCardBeen {

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
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("card_id")
        @Expose
        private String cardId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("card_payment_name")
        @Expose
        private String cardPaymentName;
        @SerializedName("card_last_four_number")
        @Expose
        private String cardLastFourNumber;
        @SerializedName("card_exp_month")
        @Expose
        private Integer cardExpMonth;
        @SerializedName("card_exp_year")
        @Expose
        private Integer cardExpYear;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCardPaymentName() {
            return cardPaymentName;
        }

        public void setCardPaymentName(String cardPaymentName) {
            this.cardPaymentName = cardPaymentName;
        }

        public String getCardLastFourNumber() {
            return cardLastFourNumber;
        }

        public void setCardLastFourNumber(String cardLastFourNumber) {
            this.cardLastFourNumber = cardLastFourNumber;
        }

        public Integer getCardExpMonth() {
            return cardExpMonth;
        }

        public void setCardExpMonth(Integer cardExpMonth) {
            this.cardExpMonth = cardExpMonth;
        }

        public Integer getCardExpYear() {
            return cardExpYear;
        }

        public void setCardExpYear(Integer cardExpYear) {
            this.cardExpYear = cardExpYear;
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