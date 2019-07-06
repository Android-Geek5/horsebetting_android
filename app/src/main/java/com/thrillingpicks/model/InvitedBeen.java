package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitedBeen {

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

        @SerializedName("invitedAccepted")
        @Expose
        private Integer invitedAccepted;
        @SerializedName("whoSubscribed")
        @Expose
        private Integer whoSubscribed;
        @SerializedName("refered_by_amount")
        @Expose
        private String referedByAmount;
        @SerializedName("refered_to_amount")
        @Expose
        private String referedToAmount;

        public Integer getInvitedAccepted() {
            return invitedAccepted;
        }

        public void setInvitedAccepted(Integer invitedAccepted) {
            this.invitedAccepted = invitedAccepted;
        }

        public Integer getWhoSubscribed() {
            return whoSubscribed;
        }

        public void setWhoSubscribed(Integer whoSubscribed) {
            this.whoSubscribed = whoSubscribed;
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

    }
}