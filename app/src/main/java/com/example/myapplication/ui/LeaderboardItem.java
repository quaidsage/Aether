package com.example.myapplication.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class LeaderboardItem implements Parcelable {

    private String username;
    private Long totalEmission;
    private int rank;

    // Constructor
    public LeaderboardItem(String username, Long totalEmission, int rank) {
        this.username = username;
        this.totalEmission = totalEmission;
        this.rank = rank;
    }

    // Protected constructor for Parcelable
    protected LeaderboardItem(Parcel in) {
        username = in.readString();
        totalEmission = in.readLong();
        rank = in.readInt();
    }

    // Parcelable Creator
    public static final Creator<LeaderboardItem> CREATOR = new Creator<LeaderboardItem>() {
        @Override
        public LeaderboardItem createFromParcel(Parcel in) {
            return new LeaderboardItem(in);
        }

        @Override
        public LeaderboardItem[] newArray(int size) {
            return new LeaderboardItem[size];
        }
    };

    // Getters
    public String getUsername() {
        return username;
    }

    public Long getTotalEmission() {
        return totalEmission;
    }

    public int getRank() {
        return rank;
    }

    // Parcelable interface methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeLong(totalEmission);
        dest.writeInt(rank);
    }
}