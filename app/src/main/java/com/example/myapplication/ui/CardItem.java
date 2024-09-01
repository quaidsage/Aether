package com.example.myapplication.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class CardItem implements Parcelable {

    private String title;
    private String description;
    private int imageResId;

    // Constructor
    public CardItem(String title, String description, int imageResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    // Protected constructor for Parcelable
    protected CardItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageResId = in.readInt();
    }

    // Parcelable Creator
    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Parcelable interface methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(imageResId);
    }
}