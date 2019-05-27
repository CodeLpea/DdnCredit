package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/25.
 */

public class StuAttendancedStatusRequest {
    @SerializedName("student_id")
    private long mStudentId;
    @SerializedName("parent_id")
    private long mParentId;
    @SerializedName("create_time")
    private String mCreateTime;

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

    @Override
    public String toString() {
        return "StuAttendancedStatusRequest{" +
                "mStudentId='" + mStudentId + '\'' +
                ", mParentId='" + mParentId + '\'' +
                ", mCreateTime='" + mCreateTime + '\'' +
                '}';
    }
}
