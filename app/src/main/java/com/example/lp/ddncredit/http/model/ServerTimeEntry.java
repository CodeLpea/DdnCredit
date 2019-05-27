package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/25.
 */

public class ServerTimeEntry {
    @SerializedName("time")
    private String mTime; //格式YYYYMMDDhhmmss
    @SerializedName("cacheTime")
    private boolean mIsCacheTime;

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public boolean isCacheTime() {
        return mIsCacheTime;
    }

    public void setCacheTime(boolean isCacheTime) {
        this.mIsCacheTime = isCacheTime;
    }

    @Override
    public String toString() {
        return "ServerTimeEntry{" +
                "mTime='" + mTime + '\'' +
                ", mIsCacheTime=" + mIsCacheTime +
                '}';
    }
}
