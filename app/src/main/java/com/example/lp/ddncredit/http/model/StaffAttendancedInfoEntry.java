package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/26.
 */

public class StaffAttendancedInfoEntry {
    /**
     * {
     "id": "3130",
     "device_no":"195744c7",
     "create_time": "20180412215216",
     "imgUrl" : "detection/20180328/925bd45b9ccc60106a52c80205917c26.jpg"
     }
     */
    @SerializedName("id")
    private long mId;
    @SerializedName("create_time")
    private String mCreateTime;
    @SerializedName("imgUrl")
    private String mImgUrl;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }


    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        this.mCreateTime = createTime;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.mImgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "StaffAttendancedInfoEntry{" +
                "mId=" + mId +
                ", mCreateTime='" + mCreateTime + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                '}';
    }
}
