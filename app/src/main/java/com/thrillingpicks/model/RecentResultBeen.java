package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentResultBeen {

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
        @SerializedName("date_of_result")
        @Expose
        private String dateOfResult;
        @SerializedName("track_id")
        @Expose
        private Integer trackId;
        @SerializedName("race_number")
        @Expose
        private String raceNumber;
        @SerializedName("bet_type")
        @Expose
        private String betType;
        @SerializedName("selection")
        @Expose
        private String selection;
        @SerializedName("bet_total")
        @Expose
        private String betTotal;
        @SerializedName("amount_won")
        @Expose
        private String amountWon;
        @SerializedName("race_conditions")
        @Expose
        private String raceConditions;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("winning_status")
        @Expose
        private String winningStatus;
        @SerializedName("track_name")
        @Expose
        private String trackName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDateOfResult() {
            return dateOfResult;
        }

        public void setDateOfResult(String dateOfResult) {
            this.dateOfResult = dateOfResult;
        }

        public Integer getTrackId() {
            return trackId;
        }

        public void setTrackId(Integer trackId) {
            this.trackId = trackId;
        }

        public String getRaceNumber() {
            return raceNumber;
        }

        public void setRaceNumber(String raceNumber) {
            this.raceNumber = raceNumber;
        }

        public String getBetType() {
            return betType;
        }

        public void setBetType(String betType) {
            this.betType = betType;
        }

        public String getSelection() {
            return selection;
        }

        public void setSelection(String selection) {
            this.selection = selection;
        }

        public String getBetTotal() {
            return betTotal;
        }

        public void setBetTotal(String betTotal) {
            this.betTotal = betTotal;
        }

        public String getAmountWon() {
            return amountWon;
        }

        public void setAmountWon(String amountWon) {
            this.amountWon = amountWon;
        }

        public String getRaceConditions() {
            return raceConditions;
        }

        public void setRaceConditions(String raceConditions) {
            this.raceConditions = raceConditions;
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

        public String getWinningStatus() {
            return winningStatus;
        }

        public void setWinningStatus(String winningStatus) {
            this.winningStatus = winningStatus;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }

    }
}
