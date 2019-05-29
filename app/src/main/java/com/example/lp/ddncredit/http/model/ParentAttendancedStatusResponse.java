package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/25.
 */

public class ParentAttendancedStatusResponse {
    public transient static final int ONLY_BRUSH_ONCE_OCCASION_UPLOAD = 81;     //刷一次场景，可以上传
    public transient static final int ONLY_BRUSH_ONCE_OCCASION_NOT_UPLOAD = 88; //刷一次场景，不用上传
    public transient static final int MULTI_BRUSH_OCCASION_UPLOAD = 401;  //多次刷场景，可以上传(服务器无记录直接上传，服务器有记录提示被谁接走再上传)
    public transient static final int MULTI_BRUSH_OCCASION_PROMPT_FREQUENT = 402;//间隔时间内频繁刷，提示"请勿频繁刷卡"

    @SerializedName("status")
    private int mStatus;
    @SerializedName("relation_title")
    private String mRelationShip;
    @SerializedName("create_time")
    private String mCreateTime;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getRelationShip() {
        return mRelationShip;
    }

    public void setRelationShip(String relationShip) {
        this.mRelationShip = relationShip;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        this.mCreateTime = createTime;
    }

    @Override
    public String toString() {
        return "ParentAttendancedStatusResponse{" +
                "mStatus='" + mStatus + '\'' +
                ", mRelationShip='" + mRelationShip + '\'' +
                ", mCreateTime='" + mCreateTime + '\'' +
                '}';
    }
}
