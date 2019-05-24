package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.constants.Constants;


public class Guest extends Application implements Constants, Parcelable {

    private long id, guestTo, guestId, guestAllowShowOnline;

    private int verified;

    private String guestUsername, guestFullname, guestPhotoUrl, timeAgo;

    private Boolean online = false;

    public Guest() {


    }

    public Guest(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setGuestId(jsonData.getLong("guestId"));
                this.setGuestVerified(jsonData.getInt("guestVerified"));
                this.setGuestUsername(jsonData.getString("guestUsername"));
                this.setGuestFullname(jsonData.getString("guestFullname"));
                this.setGuestPhotoUrl(jsonData.getString("guestPhotoUrl"));
                this.setGuestTo(jsonData.getLong("guestTo"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
                this.setOnline(jsonData.getBoolean("guestOnline"));
                this.setGuestAllowShowOnline(jsonData.getInt("guestAllowShowOnline"));
            }

        } catch (Throwable t) {

            Log.e("Guest", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Guest", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setGuestTo(long guestTo) {

        this.guestTo = guestTo;
    }

    public long getGuestTo() {

        return this.guestTo;
    }

    public void setGuestAllowShowOnline(int guestAllowShowOnline) {

        this.guestAllowShowOnline = guestAllowShowOnline;
    }

    public long getGuestAllowShowOnline() {

        return this.guestAllowShowOnline;
    }

    public void setGuestId(long guestId) {

        this.guestId = guestId;
    }

    public long getGuestId() {

        return this.guestId;
    }

    public void setGuestVerified(int guestVerified) {

        this.verified = guestVerified;
    }

    public int getGusetVerified() {

        return this.verified;
    }

    public Boolean isVerified() {

        if (this.verified > 0) {

            return true;
        }

        return false;
    }

    public void setGuestUsername(String guestUsername) {

        this.guestUsername = guestUsername;
    }

    public String getGuestUsername() {

        return this.guestUsername;
    }

    public void setGuestFullname(String guestFullname) {

        this.guestFullname = guestFullname;
    }

    public String getGuestFullname() {

        return this.guestFullname;
    }

    public void setGuestPhotoUrl(String guestPhotoUrl) {

        this.guestPhotoUrl = guestPhotoUrl;
    }

    public String getGuestPhotoUrl() {

        return this.guestPhotoUrl;
    }

    public void setTimeAgo(String ago) {

        this.timeAgo = ago;
    }

    public String getTimeAgo() {

        return this.timeAgo;
    }

    public void setOnline(Boolean online) {

        this.online = online;
    }

    public Boolean isOnline() {

        return this.online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.guestTo);
        dest.writeLong(this.guestId);
        dest.writeInt(this.verified);
        dest.writeString(this.guestUsername);
        dest.writeString(this.guestFullname);
        dest.writeString(this.guestPhotoUrl);
        dest.writeString(this.timeAgo);
        dest.writeValue(this.online);
    }

    protected Guest(Parcel in) {
        this.id = in.readLong();
        this.guestTo = in.readLong();
        this.guestId = in.readLong();
        this.verified = in.readInt();
        this.guestUsername = in.readString();
        this.guestFullname = in.readString();
        this.guestPhotoUrl = in.readString();
        this.timeAgo = in.readString();
        this.online = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Guest> CREATOR = new Creator<Guest>() {
        @Override
        public Guest createFromParcel(Parcel source) {
            return new Guest(source);
        }

        @Override
        public Guest[] newArray(int size) {
            return new Guest[size];
        }
    };
}
