package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import ru.club.sfera.constants.Constants;


public class Item extends Application implements Constants, Parcelable {

    private long id, repostId, fromUserId, toUserId;
    private int createAt, likesCount, viewsCount, repostsCount, commentsCount, accessMode, fromUserVerified, fromAccountType, fromAccountState, fromUserAllowItemsComments, fromUserAllowShowOnline;
    private String timeAgo, date, desc, imgUrl, previewImgUrl, fromUserUsername, fromUserFullname, fromUserPhotoUrl, area, country, city;
    private String videoUrl, previewVideoImgUrl;
    private int itemType;
    private Double lat = 0.000000, lng = 0.000000;
    private Boolean myLike = false;
    private Boolean myRepost = false;
    private Boolean fromUserOnline = false;

    private String urlPreviewTitle, urlPreviewImage, urlPreviewLink, urlPreviewDescription;
    private String youTubeVideoImg, youTubeVideoCode, youTubeVideoUrl;

    private String reTimeAgo, reDate, reDesc, reImgUrl, reFromUserUsername, reFromUserFullname, reFromUserPhotoUrl, reVideoUrl;
    private int reFromUserVerified, reRemoveAt, reFromAccountType, reFromAccountState;
    private long reFromUserId, reToUserId;
    private String reYouTubeVideoImg, reYouTubeVideoCode, reYouTubeVideoUrl;
    private String reUrlPreviewTitle, reUrlPreviewImage, reUrlPreviewLink, reUrlPreviewDescription;

    private int ad = 0;
    private int spotlight = 0;

    public Item() {

    }

    public Item(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setRepostId(jsonData.getLong("repostId"));
                this.setToUserId(jsonData.getLong("toUserId"));
                this.setFromUserId(jsonData.getLong("fromUserId"));
                this.setAccessMode(jsonData.getInt("accessMode"));
                this.setItemType(jsonData.getInt("itemType"));
                this.setFromAccountType(jsonData.getInt("fromAccountType"));
                this.setFromAccountState(jsonData.getInt("fromAccountState"));
                this.setFromUserAllowItemsComments(jsonData.getInt("fromUserAllowItemsComments"));
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
                this.setArea(jsonData.getString("area"));
                this.setCountry(jsonData.getString("country"));
                this.setCity(jsonData.getString("city"));
                this.setRepostsCount(jsonData.getInt("repostsCount"));
                this.setCommentsCount(jsonData.getInt("commentsCount"));
                this.setLikesCount(jsonData.getInt("likesCount"));
                this.setViewsCount(jsonData.getInt("viewsCount"));
                this.setLat(jsonData.getDouble("lat"));
                this.setLng(jsonData.getDouble("lng"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));

                this.setMyLike(jsonData.getBoolean("myLike"));
                this.setMyRepost(jsonData.getBoolean("myRepost"));

                if (jsonData.has("YouTubeVideoImg")) this.setYouTubeVideoImg(jsonData.getString("YouTubeVideoImg"));
                if (jsonData.has("YouTubeVideoCode")) this.setYouTubeVideoCode(jsonData.getString("YouTubeVideoCode"));
                if (jsonData.has("YouTubeVideoUrl")) this.setYouTubeVideoUrl(jsonData.getString("YouTubeVideoUrl"));

                if (jsonData.has("urlPreviewTitle")) this.setUrlPreviewTitle(jsonData.getString("urlPreviewTitle"));
                if (jsonData.has("urlPreviewImage")) this.setUrlPreviewImage(jsonData.getString("urlPreviewImage"));
                if (jsonData.has("urlPreviewLink")) this.setUrlPreviewLink(jsonData.getString("urlPreviewLink"));
                if (jsonData.has("urlPreviewDescription")) this.setUrlPreviewDescription(jsonData.getString("urlPreviewDescription"));

                if (this.getRepostId() != 0 && jsonData.has("rePost")) {

                    JSONArray rePostArray = jsonData.getJSONArray("rePost");

                    if (rePostArray.length() > 0) {

                        JSONObject rePostObj = (JSONObject) rePostArray.get(0);

                        this.setReFromAccountType(rePostObj.getInt("fromAccountType"));
                        this.setReFromAccountState(rePostObj.getInt("fromAccountState"));
                        this.setReFromUserVerified(rePostObj.getInt("fromUserVerified"));
                        this.setReFromUserFullname(rePostObj.getString("fromUserFullname"));
                        this.setReFromUserUsername(rePostObj.getString("fromUserUsername"));
                        this.setReFromUserPhotoUrl(rePostObj.getString("fromUserPhoto"));
                        this.setReDescription(rePostObj.getString("desc"));
                        this.setReImgUrl(rePostObj.getString("imgUrl"));
                        this.setReVideoUrl(rePostObj.getString("videoUrl"));
                        this.setReTimeAgo(rePostObj.getString("timeAgo"));
                        this.setReDate(rePostObj.getString("date"));
                        this.setReFromUserId(rePostObj.getLong("fromUserId"));
                        this.setReToUserId(rePostObj.getLong("toUserId"));
                        this.setReRemoveAt(rePostObj.getInt("removeAt"));

                        if (rePostObj.has("YouTubeVideoImg")) this.setReYouTubeVideoImg(rePostObj.getString("YouTubeVideoImg"));
                        if (rePostObj.has("YouTubeVideoCode")) this.setReYouTubeVideoCode(rePostObj.getString("YouTubeVideoCode"));
                        if (rePostObj.has("YouTubeVideoUrl")) this.setReYouTubeVideoUrl(rePostObj.getString("YouTubeVideoUrl"));

                        if (rePostObj.has("urlPreviewTitle")) this.setReUrlPreviewTitle(rePostObj.getString("urlPreviewTitle"));
                        if (rePostObj.has("urlPreviewImage")) this.setReUrlPreviewImage(rePostObj.getString("urlPreviewImage"));
                        if (rePostObj.has("urlPreviewLink")) this.setReUrlPreviewLink(rePostObj.getString("urlPreviewLink"));
                        if (rePostObj.has("urlPreviewDescription")) this.setReUrlPreviewDescription(rePostObj.getString("urlPreviewDescription"));
                    }

                }
            }

        } catch (Throwable t) {

            Log.e("Item", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Item", jsonData.toString());
        }
    }

    public int getAd() {
        return this.ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public int getSpotlight() {
        return this.spotlight;
    }

    public void setSpotlight(int spotlight) {
        this.spotlight = spotlight;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRepostId() {

        return repostId;
    }

    public void setRepostId(long repostId) {

        this.repostId = repostId;
    }

    public long getToUserId() {

        return toUserId;
    }

    public void setToUserId(long toUserId) {

        this.toUserId = toUserId;
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

    public int getFromAccountType() {

        return fromAccountType;
    }

    public void setFromAccountType(int fromAccountType) {

        this.fromAccountType = fromAccountType;
    }

    public int getFromAccountState() {

        return fromAccountState;
    }

    public void setFromAccountState(int fromAccountState) {

        this.fromAccountState = fromAccountState;
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

    public int getFromUserAllowItemsComments() {

        return fromUserAllowItemsComments;
    }

    public void setFromUserAllowItemsComments(int fromUserAllowItemsComments) {

        this.fromUserAllowItemsComments = fromUserAllowItemsComments;
    }

    public int getFromUserAllowShowOnline() {

        return fromUserAllowShowOnline;
    }

    public void setFromUserAllowShowOnline(int fromUserAllowShowOnline) {

        this.fromUserAllowShowOnline = fromUserAllowShowOnline;
    }

    public int getRepostsCount() {

        return this.repostsCount;
    }

    public void setRepostsCount(int repostsCount) {

        this.repostsCount = repostsCount;
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

    public Boolean isMyRepost() {

        return myRepost;
    }

    public void setMyRepost(Boolean myRepost) {

        this.myRepost = myRepost;
    }

    public String getYouTubeVideoImg() {

        return youTubeVideoImg;
    }

    public void setYouTubeVideoImg(String youTubeVideoImg) {

        this.youTubeVideoImg = youTubeVideoImg;
    }

    public String getYouTubeVideoCode() {

        return youTubeVideoCode;
    }

    public void setYouTubeVideoCode(String youTubeVideoCode) {

        this.youTubeVideoCode = youTubeVideoCode;
    }

    public String getYouTubeVideoUrl() {

        return youTubeVideoUrl;
    }

    public void setYouTubeVideoUrl(String youTubeVideoUrl) {

        this.youTubeVideoUrl = youTubeVideoUrl;
    }

    public String getUrlPreviewTitle() {

        return urlPreviewTitle;
    }

    public void setUrlPreviewTitle(String urlPreviewTitle) {

        this.urlPreviewTitle = urlPreviewTitle;
    }

    public String getUrlPreviewImage() {

        return urlPreviewImage;
    }

    public void setUrlPreviewImage(String urlPreviewImage) {

        this.urlPreviewImage = urlPreviewImage;
    }

    public String getUrlPreviewLink() {

        return urlPreviewLink;
    }

    public void setUrlPreviewLink(String urlPreviewLink) {

        this.urlPreviewLink = urlPreviewLink;
    }

    public String getUrlPreviewDescription() {

        return urlPreviewDescription;
    }

    public void setUrlPreviewDescription(String urlPreviewDescription) {

        this.urlPreviewDescription = urlPreviewDescription;
    }




    // Repost

    public int getReFromAccountType() {

        return reFromAccountType;
    }

    public void setReFromAccountType(int reFromAccountType) {

        this.reFromAccountType = reFromAccountType;
    }

    public int getReFromAccountState() {

        return reFromAccountState;
    }

    public void setReFromAccountState(int reFromAccountState) {

        this.reFromAccountState = reFromAccountState;
    }

    public int getReFromUserVerified() {

        return reFromUserVerified;
    }

    public void setReFromUserVerified(int reFromUserVerified) {

        this.reFromUserVerified = reFromUserVerified;
    }

    public long getReToUserId() {

        return reToUserId;
    }

    public void setReToUserId(long reToUserId) {

        this.reToUserId = reToUserId;
    }

    public long getReFromUserId() {

        return reFromUserId;
    }

    public void setReFromUserId(long reFromUserId) {

        this.reFromUserId = reFromUserId;
    }

    public String getReTimeAgo() {

        return this.reTimeAgo;
    }

    public void setReTimeAgo(String reTimeAgo) {

        this.reTimeAgo = reTimeAgo;
    }

    public String getReDescription() {

        return this.reDesc;
    }

    public void setReDescription(String reDesc) {

        this.reDesc = reDesc;
    }


    public String getReFromUserUsername() {

        return reFromUserUsername;
    }

    public void setReFromUserUsername(String reFromUserUsername) {

        this.reFromUserUsername = reFromUserUsername;
    }

    public String getReFromUserFullname() {

        return reFromUserFullname;
    }

    public void setReFromUserFullname(String reFromUserFullname) {

        this.reFromUserFullname = reFromUserFullname;
    }

    public String getReFromUserPhotoUrl() {

        if (reFromUserPhotoUrl == null) {

            reFromUserPhotoUrl = "";
        }

        return reFromUserPhotoUrl;
    }

    public void setReFromUserPhotoUrl(String reFromUserPhotoUrl) {

        this.reFromUserPhotoUrl = reFromUserPhotoUrl;
    }

    public String getReImgUrl() {

        return reImgUrl;
    }

    public void setReImgUrl(String reImgUrl) {

        this.reImgUrl = reImgUrl;
    }

    public String getReDate() {

        return reDate;
    }

    public void setReDate(String reDate) {

        this.reDate = reDate;
    }

    public int getReRemoveAt() {

        return reRemoveAt;
    }

    public void setReRemoveAt(int reRemoveAt) {

        this.reRemoveAt = reRemoveAt;
    }

    public String getReYouTubeVideoImg() {

        return reYouTubeVideoImg;
    }

    public void setReYouTubeVideoImg(String reYouTubeVideoImg) {

        this.reYouTubeVideoImg = reYouTubeVideoImg;
    }

    public String getReYouTubeVideoCode() {

        return reYouTubeVideoCode;
    }

    public void setReYouTubeVideoCode(String reYouTubeVideoCode) {

        this.reYouTubeVideoCode = reYouTubeVideoCode;
    }

    public String getReYouTubeVideoUrl() {

        return reYouTubeVideoUrl;
    }

    public void setReYouTubeVideoUrl(String reYouTubeVideoUrl) {

        this.reYouTubeVideoUrl = reYouTubeVideoUrl;
    }

    public String getReVideoUrl() {

        return reVideoUrl;
    }

    public void setReVideoUrl(String reVideoUrl) {

        this.reVideoUrl = reVideoUrl;
    }

    public String getReUrlPreviewTitle() {

        return reUrlPreviewTitle;
    }

    public void setReUrlPreviewTitle(String reUrlPreviewTitle) {

        this.reUrlPreviewTitle = reUrlPreviewTitle;
    }

    public String getReUrlPreviewImage() {

        return reUrlPreviewImage;
    }

    public void setReUrlPreviewImage(String reUrlPreviewImage) {

        this.reUrlPreviewImage = reUrlPreviewImage;
    }

    public String getReUrlPreviewLink() {

        return reUrlPreviewLink;
    }

    public void setReUrlPreviewLink(String reUrlPreviewLink) {

        this.reUrlPreviewLink = reUrlPreviewLink;
    }

    public String getReUrlPreviewDescription() {

        return reUrlPreviewDescription;
    }

    public void setReUrlPreviewDescription(String reUrlPreviewDescription) {

        this.reUrlPreviewDescription = reUrlPreviewDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.repostId);
        dest.writeLong(this.fromUserId);
        dest.writeLong(this.toUserId);
        dest.writeInt(this.createAt);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.viewsCount);
        dest.writeInt(this.repostsCount);
        dest.writeInt(this.commentsCount);
        dest.writeInt(this.accessMode);
        dest.writeInt(this.fromUserVerified);
        dest.writeInt(this.fromAccountType);
        dest.writeInt(this.fromAccountState);
        dest.writeInt(this.fromUserAllowItemsComments);
        dest.writeInt(this.fromUserAllowShowOnline);
        dest.writeString(this.timeAgo);
        dest.writeString(this.date);
        dest.writeString(this.desc);
        dest.writeString(this.imgUrl);
        dest.writeString(this.previewImgUrl);
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
        dest.writeValue(this.myRepost);
        dest.writeValue(this.fromUserOnline);
        dest.writeString(this.urlPreviewTitle);
        dest.writeString(this.urlPreviewImage);
        dest.writeString(this.urlPreviewLink);
        dest.writeString(this.urlPreviewDescription);
        dest.writeString(this.youTubeVideoImg);
        dest.writeString(this.youTubeVideoCode);
        dest.writeString(this.youTubeVideoUrl);
        dest.writeString(this.reTimeAgo);
        dest.writeString(this.reDate);
        dest.writeString(this.reDesc);
        dest.writeString(this.reImgUrl);
        dest.writeString(this.reFromUserUsername);
        dest.writeString(this.reFromUserFullname);
        dest.writeString(this.reFromUserPhotoUrl);
        dest.writeString(this.reVideoUrl);
        dest.writeInt(this.reFromUserVerified);
        dest.writeInt(this.reRemoveAt);
        dest.writeInt(this.reFromAccountType);
        dest.writeInt(this.reFromAccountState);
        dest.writeLong(this.reFromUserId);
        dest.writeLong(this.reToUserId);
        dest.writeString(this.reYouTubeVideoImg);
        dest.writeString(this.reYouTubeVideoCode);
        dest.writeString(this.reYouTubeVideoUrl);
        dest.writeString(this.reUrlPreviewTitle);
        dest.writeString(this.reUrlPreviewImage);
        dest.writeString(this.reUrlPreviewLink);
        dest.writeString(this.reUrlPreviewDescription);
        dest.writeInt(this.ad);
        dest.writeInt(this.spotlight);
    }

    protected Item(Parcel in) {
        this.id = in.readLong();
        this.repostId = in.readLong();
        this.fromUserId = in.readLong();
        this.toUserId = in.readLong();
        this.createAt = in.readInt();
        this.likesCount = in.readInt();
        this.viewsCount = in.readInt();
        this.repostsCount = in.readInt();
        this.commentsCount = in.readInt();
        this.accessMode = in.readInt();
        this.fromUserVerified = in.readInt();
        this.fromAccountType = in.readInt();
        this.fromAccountState = in.readInt();
        this.fromUserAllowItemsComments = in.readInt();
        this.fromUserAllowShowOnline = in.readInt();
        this.timeAgo = in.readString();
        this.date = in.readString();
        this.desc = in.readString();
        this.imgUrl = in.readString();
        this.previewImgUrl = in.readString();
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
        this.myRepost = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.fromUserOnline = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.urlPreviewTitle = in.readString();
        this.urlPreviewImage = in.readString();
        this.urlPreviewLink = in.readString();
        this.urlPreviewDescription = in.readString();
        this.youTubeVideoImg = in.readString();
        this.youTubeVideoCode = in.readString();
        this.youTubeVideoUrl = in.readString();
        this.reTimeAgo = in.readString();
        this.reDate = in.readString();
        this.reDesc = in.readString();
        this.reImgUrl = in.readString();
        this.reFromUserUsername = in.readString();
        this.reFromUserFullname = in.readString();
        this.reFromUserPhotoUrl = in.readString();
        this.reVideoUrl = in.readString();
        this.reFromUserVerified = in.readInt();
        this.reRemoveAt = in.readInt();
        this.reFromAccountType = in.readInt();
        this.reFromAccountState = in.readInt();
        this.reFromUserId = in.readLong();
        this.reToUserId = in.readLong();
        this.reYouTubeVideoImg = in.readString();
        this.reYouTubeVideoCode = in.readString();
        this.reYouTubeVideoUrl = in.readString();
        this.reUrlPreviewTitle = in.readString();
        this.reUrlPreviewImage = in.readString();
        this.reUrlPreviewLink = in.readString();
        this.reUrlPreviewDescription = in.readString();
        this.ad = in.readInt();
        this.spotlight = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
