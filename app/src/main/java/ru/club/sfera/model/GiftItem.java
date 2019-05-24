package ru.club.sfera.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.constants.Constants;


public class GiftItem extends Application implements Constants, Parcelable {

    private long id;
    private int createAt, cost, category;
    private String timeAgo, date, imgUrl;

    public GiftItem() {

    }

    public GiftItem(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setCost(jsonData.getInt("cost"));
                this.setCategory(jsonData.getInt("category"));
                this.setImgUrl(jsonData.getString("imgUrl"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
            }

        } catch (Throwable t) {

            Log.e("GiftItem", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("GiftItem", jsonData.toString());
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCost() {

        return this.cost;
    }

    public void setCost(int cost) {

        this.cost = cost;
    }

    public int getCategory() {

        return this.category;
    }

    public void setCategory(int category) {

        this.category = category;
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
        dest.writeInt(this.createAt);
        dest.writeInt(this.cost);
        dest.writeInt(this.category);
        dest.writeString(this.timeAgo);
        dest.writeString(this.date);
        dest.writeString(this.imgUrl);
    }

    protected GiftItem(Parcel in) {
        this.id = in.readLong();
        this.createAt = in.readInt();
        this.cost = in.readInt();
        this.category = in.readInt();
        this.timeAgo = in.readString();
        this.date = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Creator<GiftItem> CREATOR = new Creator<GiftItem>() {
        @Override
        public GiftItem createFromParcel(Parcel source) {
            return new GiftItem(source);
        }

        @Override
        public GiftItem[] newArray(int size) {
            return new GiftItem[size];
        }
    };
}
