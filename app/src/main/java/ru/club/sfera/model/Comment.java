package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.constants.Constants;

public class Comment extends Application implements Constants, Parcelable {

    private long id, itemId, fromUserId, replyToUserId, itemFromUserId;
    private int fromUserState, fromUserVerified, likesCount, createAt, fromUserAllowShowOnline;
    private String comment, fromUserUsername, fromUserFullname, replyToUserUsername, replyToUserFullname, fromUserPhotoUrl, timeAgo, imgUrl;
    private Boolean fromUserOnline = false;

    private int ad = 0;

    private Boolean myLike = false;

    public Comment() {

    }

    public Comment(JSONObject jsonData) {

        try {

            this.setId(jsonData.getLong("id"));
            this.setItemId(jsonData.getLong("itemId"));
            this.setFromUserId(jsonData.getLong("fromUserId"));
            this.setFromUserState(jsonData.getInt("fromUserState"));
            this.setFromUserVerified(jsonData.getInt("fromUserVerified"));
            this.setFromUserOnline(jsonData.getBoolean("fromUserOnline"));
            this.setFromUserAllowShowOnline(jsonData.getInt("fromUserAllowShowOnline"));
            this.setFromUserUsername(jsonData.getString("fromUserUsername"));
            this.setFromUserFullname(jsonData.getString("fromUserFullname"));
            this.setFromUserPhotoUrl(jsonData.getString("fromUserPhotoUrl"));
            this.setReplyToUserId(jsonData.getLong("replyToUserId"));
            this.setReplyToUserUsername(jsonData.getString("replyToUserUsername"));
            this.setReplyToUserFullname(jsonData.getString("replyToFullname"));
            this.setText(jsonData.getString("comment"));
            this.setImgUrl(jsonData.getString("imgUrl"));
            this.setTimeAgo(jsonData.getString("timeAgo"));
            this.setCreateAt(jsonData.getInt("createAt"));
            this.setLikesCount(jsonData.getInt("likesCount"));

            if (jsonData.has("itemFromUserId")) {

                this.setItemFromUserId(jsonData.getLong("itemFromUserId"));
            }

            if (jsonData.has("myLike")) {

                this.setMyLike(jsonData.getBoolean("myLike"));
            }

        } catch (Throwable t) {

            Log.e("Comment", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Comment", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setItemId(long itemId) {

        this.itemId = itemId;
    }

    public long getItemId() {

        return this.itemId;
    }

    public void setFromUserId(long fromUserId) {

        this.fromUserId = fromUserId;
    }

    public long getFromUserId() {

        return this.fromUserId;
    }

    public void setReplyToUserId(long replyToUserId) {

        this.replyToUserId = replyToUserId;
    }

    public long getReplyToUserId() {

        return this.replyToUserId;
    }

    public void setFromUserState(int fromUserState) {

        this.fromUserState = fromUserState;
    }

    public int getFromUserState() {

        return this.fromUserState;
    }

    public void setFromUserVerified(int fromUserVerified) {

        this.fromUserVerified = fromUserVerified;
    }

    public int getFromUserVerified() {

        return this.fromUserVerified;
    }

    public void setText(String comment) {

        this.comment = comment;
    }

    public String getText() {

        return this.comment;
    }

    public void setImgUrl(String imgUrl) {

        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {

        return this.imgUrl;
    }

    public void setTimeAgo(String timeAgo) {

        this.timeAgo = timeAgo;
    }

    public String getTimeAgo() {

        return this.timeAgo;
    }

    public void setFromUserUsername(String fromUserUsername) {

        this.fromUserUsername = fromUserUsername;
    }

    public String getFromUserUsername() {

        return this.fromUserUsername;
    }

    public void setReplyToUserUsername(String replyToUserUsername) {

        this.replyToUserUsername = replyToUserUsername;
    }

    public String getReplyToUserUsername() {

        return this.replyToUserUsername;
    }

    public void setFromUserFullname(String fromUserFullname) {

        this.fromUserFullname = fromUserFullname;
    }

    public String getFromUserFullname() {

        return this.fromUserFullname;
    }

    public void setReplyToUserFullname(String replyToUserFullname) {

        this.replyToUserFullname = replyToUserFullname;
    }

    public String getReplyToUserFullname() {

        return this.replyToUserFullname;
    }

    public void setFromUserPhotoUrl(String fromUserPhotoUrl) {

        this.fromUserPhotoUrl = fromUserPhotoUrl;
    }

    public String getFromUserPhotoUrl() {

        return this.fromUserPhotoUrl;
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

    public void setCreateAt(int createAt) {

        this.createAt = createAt;
    }

    public int getCreateAt() {

        return this.createAt;
    }

    public void setLikesCount(int likesCount) {

        this.likesCount = likesCount;
    }

    public int getLikesCount() {

        return this.likesCount;
    }

    public Boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(Boolean myLike) {

        this.myLike = myLike;
    }

    public int getAd() {
        return this.ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public void setItemFromUserId(long itemFromUserId) {

        this.itemFromUserId = itemFromUserId;
    }

    public long getItemFromUserId() {

        return this.itemFromUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.itemId);
        dest.writeLong(this.fromUserId);
        dest.writeLong(this.replyToUserId);
        dest.writeLong(this.itemFromUserId);
        dest.writeInt(this.fromUserState);
        dest.writeInt(this.fromUserVerified);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.createAt);
        dest.writeInt(this.fromUserAllowShowOnline);
        dest.writeString(this.comment);
        dest.writeString(this.fromUserUsername);
        dest.writeString(this.fromUserFullname);
        dest.writeString(this.replyToUserUsername);
        dest.writeString(this.replyToUserFullname);
        dest.writeString(this.fromUserPhotoUrl);
        dest.writeString(this.timeAgo);
        dest.writeString(this.imgUrl);
        dest.writeValue(this.fromUserOnline);
        dest.writeInt(this.ad);
        dest.writeValue(this.myLike);
    }

    protected Comment(Parcel in) {
        this.id = in.readLong();
        this.itemId = in.readLong();
        this.fromUserId = in.readLong();
        this.replyToUserId = in.readLong();
        this.itemFromUserId = in.readLong();
        this.fromUserState = in.readInt();
        this.fromUserVerified = in.readInt();
        this.likesCount = in.readInt();
        this.createAt = in.readInt();
        this.fromUserAllowShowOnline = in.readInt();
        this.comment = in.readString();
        this.fromUserUsername = in.readString();
        this.fromUserFullname = in.readString();
        this.replyToUserUsername = in.readString();
        this.replyToUserFullname = in.readString();
        this.fromUserPhotoUrl = in.readString();
        this.timeAgo = in.readString();
        this.imgUrl = in.readString();
        this.fromUserOnline = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.ad = in.readInt();
        this.myLike = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
