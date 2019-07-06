package com.thrillingpicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class TracksBeen {

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

        @SerializedName("today")
        @Expose
        private List<Today> today = null;
        @SerializedName("tomorrow")
        @Expose
        private List<Tomorrow> tomorrow = null;

        public List<Today> getToday() {
            return today;
        }

        public void setToday(List<Today> today) {
            this.today = today;
        }

        public List<Tomorrow> getTomorrow() {
            return tomorrow;
        }

        public void setTomorrow(List<Tomorrow> tomorrow) {
            this.tomorrow = tomorrow;
        }
        public class Tomorrow {

            @SerializedName("track_name")
            @Expose
            private String trackName;
            @SerializedName("track_race_is_guest_available")
            @Expose
            private String trackRaceIsGuestAvailable;
            @SerializedName("id")
            @Expose
            private Integer id;

            public String getTrackName() {
                return trackName;
            }

            public void setTrackName(String trackName) {
                this.trackName = trackName;
            }

            public String getTrackRaceIsGuestAvailable() {
                return trackRaceIsGuestAvailable;
            }

            public void setTrackRaceIsGuestAvailable(String trackRaceIsGuestAvailable) {
                this.trackRaceIsGuestAvailable = trackRaceIsGuestAvailable;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

        }

        public class Today {

            @SerializedName("track_name")
            @Expose
            private String trackName;
            @SerializedName("track_race_is_guest_available")
            @Expose
            private String trackRaceIsGuestAvailable;
            @SerializedName("id")
            @Expose
            private Integer id;

            public String getTrackName() {
                return trackName;
            }

            public void setTrackName(String trackName) {
                this.trackName = trackName;
            }

            public String getTrackRaceIsGuestAvailable() {
                return trackRaceIsGuestAvailable;
            }

            public void setTrackRaceIsGuestAvailable(String trackRaceIsGuestAvailable) {
                this.trackRaceIsGuestAvailable = trackRaceIsGuestAvailable;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

        }
    }
}