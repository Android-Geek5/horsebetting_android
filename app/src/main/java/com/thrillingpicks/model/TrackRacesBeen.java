package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackRacesBeen {

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
        @SerializedName("track_id")
        @Expose
        private Integer trackId;
        @SerializedName("track_race_date_time")
        @Expose
        private String trackRaceDateTime;
        @SerializedName("track_race_number")
        @Expose
        private String trackRaceNumber;
        @SerializedName("track_race_horse_number")
        @Expose
        private String trackRaceHorseNumber;
        @SerializedName("track_race_horse_name")
        @Expose
        private String trackRaceHorseName;
        @SerializedName("track_race_description")
        @Expose
        private String trackRaceDescription;
        @SerializedName("track_race_is_guest_available")
        @Expose
        private String trackRaceIsGuestAvailable;
        @SerializedName("track_race_bet_type")
        @Expose
        private String trackRaceBetType;
        @SerializedName("track_race_paid_amount")
        @Expose
        private String trackRacePaidAmount;
        @SerializedName("track_race_recent_show")
        @Expose
        private String trackRaceRecentShow;
        @SerializedName("track_race_status")
        @Expose
        private String trackRaceStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private Object updatedAt;
        @SerializedName("track_name")
        @Expose
        private String trackName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTrackId() {
            return trackId;
        }

        public void setTrackId(Integer trackId) {
            this.trackId = trackId;
        }

        public String getTrackRaceDateTime() {
            return trackRaceDateTime;
        }

        public void setTrackRaceDateTime(String trackRaceDateTime) {
            this.trackRaceDateTime = trackRaceDateTime;
        }

        public String getTrackRaceNumber() {
            return trackRaceNumber;
        }

        public void setTrackRaceNumber(String trackRaceNumber) {
            this.trackRaceNumber = trackRaceNumber;
        }

        public String getTrackRaceHorseNumber() {
            return trackRaceHorseNumber;
        }

        public void setTrackRaceHorseNumber(String trackRaceHorseNumber) {
            this.trackRaceHorseNumber = trackRaceHorseNumber;
        }

        public String getTrackRaceHorseName() {
            return trackRaceHorseName;
        }

        public void setTrackRaceHorseName(String trackRaceHorseName) {
            this.trackRaceHorseName = trackRaceHorseName;
        }

        public String getTrackRaceDescription() {
            return trackRaceDescription;
        }

        public void setTrackRaceDescription(String trackRaceDescription) {
            this.trackRaceDescription = trackRaceDescription;
        }

        public String getTrackRaceIsGuestAvailable() {
            return trackRaceIsGuestAvailable;
        }

        public void setTrackRaceIsGuestAvailable(String trackRaceIsGuestAvailable) {
            this.trackRaceIsGuestAvailable = trackRaceIsGuestAvailable;
        }

        public String getTrackRaceBetType() {
            return trackRaceBetType;
        }

        public void setTrackRaceBetType(String trackRaceBetType) {
            this.trackRaceBetType = trackRaceBetType;
        }

        public String getTrackRacePaidAmount() {
            return trackRacePaidAmount;
        }

        public void setTrackRacePaidAmount(String trackRacePaidAmount) {
            this.trackRacePaidAmount = trackRacePaidAmount;
        }

        public String getTrackRaceRecentShow() {
            return trackRaceRecentShow;
        }

        public void setTrackRaceRecentShow(String trackRaceRecentShow) {
            this.trackRaceRecentShow = trackRaceRecentShow;
        }

        public String getTrackRaceStatus() {
            return trackRaceStatus;
        }

        public void setTrackRaceStatus(String trackRaceStatus) {
            this.trackRaceStatus = trackRaceStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }

    }
}
