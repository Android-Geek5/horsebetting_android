package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PicksBeen {

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
        @SerializedName("track_race_recent_show")
        @Expose
        private String trackRaceRecentShow;
        @SerializedName("track_race_paid_amount")
        @Expose
        private String trackRacePaidAmount;
        @SerializedName("track_race_bet_type")
        @Expose
        private String trackRaceBetType;
        @SerializedName("track_name")
        @Expose
        private String trackName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTrackRaceRecentShow() {
            return trackRaceRecentShow;
        }

        public void setTrackRaceRecentShow(String trackRaceRecentShow) {
            this.trackRaceRecentShow = trackRaceRecentShow;
        }

        public String getTrackRacePaidAmount() {
            return trackRacePaidAmount;
        }

        public void setTrackRacePaidAmount(String trackRacePaidAmount) {
            this.trackRacePaidAmount = trackRacePaidAmount;
        }

        public String getTrackRaceBetType() {
            return trackRaceBetType;
        }

        public void setTrackRaceBetType(String trackRaceBetType) {
            this.trackRaceBetType = trackRaceBetType;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }

    }

}