package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.constants.Constants;


public class GalleryItem extends Application implements Constants, Parcelable {

    private long id, fromUserId;
    private int createAt, likesCount, viewsCount, commentsCount, accessMode, fromUserVerified, fromUserAllowGalleryComments, fromUserAllowShowOnline;
    private String timeAgo, date, desc, imgUrl, previewImgUrl, originImgUrl, fromUserUsername, fromUserFullname, fromUserPhotoUrl, area, country, city;
    private String videoUrl, previewVideoImgUrl;
    private int itemType;
    private Double lat = 0.000000, lng = 0.000000;
    private Boolean myLike = false;
    private Boolean fromUserOnline = false;

    public GalleryItem() {

    }

    public GalleryItem(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setFromUserId(jsonData.getLong("fromUserId"));
                this.setAccessMode(jsonData.getInt("accessMode"));
                this.setItemType(jsonData.getInt("itemType"));
                this.setFromUserAllowGalleryComments(jsonData.getInt("fromUserAllowGalleryComments"));
                this.setFromUserAllowShowOnline(jsonData.getInt("fromUserAllowShowOnline"));
                this.setFromUserVerified(jsonData.getInt("fromUserVerified"));
                this.setFromUserOnline(jsonData.getBoolean("fromUserOnline"));
                this.setFromUserUsername(jsonData.getString("fromUserUsername"));
                this.setFromUserFullname(jsonData.getString("fromUserFullname"));
                this.setFromUserPhotoUrl(jsonData.getString("fromUserPhoto"));
                this.setDescription(jsonData.getString("desc"));
                this.setVideoUrl(jsonData.getString("videoUrl"));
                this.setPreviewVideoImgUrl(jsonData.getString("previewVideoImgUrl"));
                this.setImgUrl(jsonData.getString("imgUrl"));
                this.setPreviewImgUrl(jsonData.getString("previewImgUrl"));
                this.setOriginImgUrl(jsonData.getString("originImgUrl"));
                this.setArea(jsonData.getString("area"));
                this.setCountry(jsonData.getString("country"));
                this.setCity(jsonData.getString("city"));
                this.setCommentsCount(jsonData.getInt("commentsCount"));
                this.setLikesCount(jsonData.getInt("likesCount"));
                this.setViewsCount(jsonData.getInt("viewsCount"));
                this.setLat(jsonData.getDouble("lat"));
                this.setLng(jsonData.getDouble("lng"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));

                this.setMyLike(jsonData.getBoolean("myLike"));
            }

        } catch (Throwable t) {

            Log.e("Gallery Item", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Gallery Item", jsonData.toString());
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

    public int getAccessMode() {

        return accessMode;
    }

    public void setAccessMode(int accessMode) {

        this.accessMode = accessMode;
    }

    public int getItemType() {

        return itemType;
    }

    public void setItemType(int itemType) {

        this.itemType = itemType;
    }

    public int getFromUserVerified() {

        return fromUserVerified;
    }

    public void setFromUserVerified(int fromUserVerified) {

        this.fromUserVerified = fromUserVerified;
    }

    public Boolean getFromUserOnline() {

        return fromUserOnline;
    }

    public void setFromUserOnline(Boolean fromUserOnline) {

        this.fromUserOnline = fromUserOnline;
    }

    public int getFromUserAllowGalleryComments() {

        return fromUserAllowGalleryComments;
    }

    public void setFromUserAllowGalleryComments(int fromUserAllowGalleryComments) {

        this.fromUserAllowGalleryComments = fromUserAllowGalleryComments;
    }

    public int getFromUserAllowShowOnline() {

        return fromUserAllowShowOnline;
    }

    public void setFromUserAllowShowOnline(int fromUserAllowShowOnline) {

        this.fromUserAllowShowOnline = fromUserAllowShowOnline;
    }

    public int getCommentsCount() {

        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {

        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getViewsCount() {

        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {

        this.viewsCount = viewsCount;
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

    public String getDescription() {
        return this.desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
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

        if (fromUserPhotoUrl == null) {

            fromUserPhotoUrl = "";
        }

        return fromUserPhotoUrl;
    }

    public void setFromUserPhotoUrl(String fromUserPhotoUrl) {

        this.fromUserPhotoUrl = fromUserPhotoUrl;
    }

    public String getVideoUrl() {

        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPreviewVideoImgUrl() {

        return previewVideoImgUrl;
    }

    public void setPreviewVideoImgUrl(String previewVideoImgUrl) {

        this.previewVideoImgUrl = previewVideoImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPreviewImgUrl() {
        return previewImgUrl;
    }

    public void setPreviewImgUrl(String previewImgUrl) {
        this.previewImgUrl = previewImgUrl;
    }

    public String getOriginImgUrl() {
        return originImgUrl;
    }

    public void setOriginImgUrl(String originImgUrl) {
        this.originImgUrl = originImgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {

        if (this.area == null) {

            this.area = "";
        }

        return this.area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public String getCountry() {

        if (this.country == null) {

            this.country = "";
        }

        return this.country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getCity() {

        if (this.city == null) {

            this.city = "";
        }

        return this.city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public Double getLat() {

        return this.lat;
    }

    public void setLat(Double lat) {

        this.lat = lat;
    }

    public Double getLng() {

        return this.lng;
    }

    public void setLng(Double lng) {

        this.lng = lng;
    }

    public Boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(Boolean myLike) {

        this.myLike = myLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.fromUserId);
        dest.writeInt(this.createAt);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.viewsCount);
        dest.writeInt(this.commentsCount);
        dest.writeInt(this.accessMode);
        dest.writeInt(this.fromUserVerified);
        dest.writeInt(this.fromUserAllowGalleryComments);
        dest.writeInt(this.fromUserAllowShowOnline);
        dest.writeString(this.timeAgo);
        dest.writeString(this.date);
        dest.writeString(this.desc);
        dest.writeString(this.imgUrl);
        dest.writeString(this.previewImgUrl);
        dest.writeString(this.originImgUrl);
        dest.writeString(this.fromUserUsername);
        dest.writeString(this.fromUserFullname);
        dest.writeString(this.fromUserPhotoUrl);
        dest.writeString(this.area);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.videoUrl);
        dest.writeString(this.previewVideoImgUrl);
        dest.writeInt(this.itemType);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeValue(this.myLike);
        dest.writeValue(this.fromUserOnline);
    }

    protected GalleryItem(Parcel in) {
        this.id = in.readLong();
        this.fromUserId = in.readLong();
        this.createAt = in.readInt();
        this.likesCount = in.readInt();
        this.viewsCount = in.readInt();
        this.commentsCount = in.readInt();
        this.accessMode = in.readInt();
        this.fromUserVerified = in.readInt();
        this.fromUserAllowGalleryComments = in.readInt();
        this.fromUserAllowShowOnline = in.readInt();
        this.timeAgo = in.readString();
        this.date = in.readString();
        this.desc = in.readString();
        this.imgUrl = in.readString();
        this.previewImgUrl = in.readString();
        this.originImgUrl = in.readString();
        this.fromUserUsername = in.readString();
        this.fromUserFullname = in.readString();
        this.fromUserPhotoUrl = in.readString();
        this.area = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.videoUrl = in.readString();
        this.previewVideoImgUrl = in.readString();
        this.itemType = in.readInt();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.myLike = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.fromUserOnline = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel source) {
            return new GalleryItem(source);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };
}
