package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.constants.Constants;

public class Gift extends Application implements Constants, Parcelable {

    private long id, fromUserId, toUserId;
    private int createAt, fromUserVerified, anonymous, giftId, fromUserAllowShowOnline;
    private String timeAgo, date, message, imgUrl, fromUserUsername, fromUserFullname, fromUserPhotoUrl;
    private Boolean fromUserOnline = false;

    public Gift() {

    }

    public Gift(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setFromUserId(jsonData.getLong("fromUserId"));
                this.setToUserId(jsonData.getLong("toUserId"));
                this.setGiftId(jsonData.getInt("giftId"));
                this.setFromUserVerified(jsonData.getInt("fromUserVerified"));
                this.setAnonymous(jsonData.getInt("anonymous"));

                this.setFromUserAllowShowOnline(jsonData.getInt("fromUserAllowShowOnline"));
                this.setFromUserOnline(jsonData.getBoolean("fromUserOnline"));

                this.setFromUserUsername(jsonData.getString("fromUserUsername"));
                this.setFromUserFullname(jsonData.getString("fromUserFullname"));
                this.setFromUserPhotoUrl(jsonData.getString("fromUserPhotoUrl"));

                this.setMessage(jsonData.getString("message"));
                this.setImgUrl(jsonData.getString("imgUrl"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
            }

        } catch (Throwable t) {

            Log.e("Gift", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Gift", jsonData.toString());
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromUserId() {

        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {

        this.fromUserId = fromUserId;
    }

    public long getToUserId() {

        return toUserId;
    }

    public void setToUserId(long toUserId) {

        this.toUserId = toUserId;
    }

    public int getAnonymous() {

        return this.anonymous;
    }

    public void setAnonymous(int anonymous) {

        this.anonymous = anonymous;
    }

    public int getGiftId() {

        return giftId;
    }

    public void setGiftId(int giftId) {

        this.giftId = giftId;
    }

    public int getFromUserVerified() {

        return fromUserVerified;
    }

    public void setFromUserVerified(int fromUserVerified) {

        this.fromUserVerified = fromUserVerified;
    }

    public int getCreateAt() {

        return createAt;
    }

    public void setCreateAt(int createAt) {
        this.createAt = createAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getFromUserUsername() {

        return fromUserUsername;
    }

    public void setFromUserUsername(String fromUserUsername) {

        this.fromUserUsername = fromUserUsername;
    }

    public String getFromUserFullname() {

        return fromUserFullname;
    }

    public void setFromUserFullname(String fromUserFullname) {

        this.fromUserFullname = fromUserFullname;
    }

    public String getFromUserPhotoUrl() {

        if (this.fromUserPhotoUrl == null) {

            this.fromUserPhotoUrl = "";
        }

        return this.fromUserPhotoUrl;
    }

    public void setFromUserPhotoUrl(String fromUserPhotoUrl) {

        this.fromUserPhotoUrl = fromUserPhotoUrl;
    }

    public Boolean getFromUserOnline() {

        return fromUserOnline;
    }

    public void setFromUserOnline(Boolean fromUserOnline) {

        this.fromUserOnline = fromUserOnline;
    }

    public int getFromUserAllowShowOnline() {

        return fromUserAllowShowOnline;
    }

    public void setFromUserAllowShowOnline(int fromUserAllowShowOnline) {

        this.fromUserAllowShowOnline = fromUserAllowShowOnline;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.fromUserId);
        dest.writeLong(this.toUserId);
        dest.writeInt(this.createAt);
        dest.writeInt(this.fromUserVerified);
        dest.writeInt(this.anonymous);
        dest.writeInt(this.giftId);
        dest.writeInt(this.fromUserAllowShowOnline);
        dest.writeString(this.timeAgo);
        dest.writeString(this.date);
        dest.writeString(this.message);
        dest.writeString(this.imgUrl);
        dest.writeString(this.fromUserUsername);
        dest.writeString(this.fromUserFullname);
        dest.writeString(this.fromUserPhotoUrl);
        dest.writeValue(this.fromUserOnline);
    }

    protected Gift(Parcel in) {
        this.id = in.readLong();
        this.fromUserId = in.readLong();
        this.toUserId = in.readLong();
        this.createAt = in.readInt();
        this.fromUserVerified = in.readInt();
        this.anonymous = in.readInt();
        this.giftId = in.readInt();
        this.fromUserAllowShowOnline = in.readInt();
        this.timeAgo = in.readString();
        this.date = in.readString();
        this.message = in.readString();
        this.imgUrl = in.readString();
        this.fromUserUsername = in.readString();
        this.fromUserFullname = in.readString();
        this.fromUserPhotoUrl = in.readString();
        this.fromUserOnline = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        @Override
        public Gift createFromParcel(Parcel source) {
            return new Gift(source);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };
}
