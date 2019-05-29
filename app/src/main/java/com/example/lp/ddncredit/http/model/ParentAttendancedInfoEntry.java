package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/26.
 */

public class ParentAttendancedInfoEntry {
    /**
     * {
     "student_id": "24844",
     "parent_id": "85593",
     "device_no": "51db4d0d",
     "create_time": "20180723085153",
     "imgUrl" : "detection/20180328/925bd45b9ccc60106a52c80205917c26.jpg"
     }
     */
    @SerializedName("student_id")
    private long mStudentId;
    @SerializedName("parent_id")
    private long mParentId;
    @SerializedName("create_time")
    private String mCreateTime;
    @SerializedName("imgUrl")
    private String mImgUrl;

    public long getStudentId() {
        return mStudentId;
    }

    public void setStudentId(long studentId) {
        this.mStudentId = studentId;
    }

    public long getParentId() {
        return mParentId;
    }

    public void setParentId(long parentId) {
        this.mParentId = parentId;
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
        return "ParentAttendancedInfoEntry{" +
                "mStudentId=" + mStudentId +
                ", mParentId=" + mParentId +
                ", mCreateTime='" + mCreateTime + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                '}';
    }
}
