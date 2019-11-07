package com.example.lp.ddncredit.websocket.bean;

import com.example.lp.ddncredit.websocket.WSConstant;
import com.google.gson.annotations.SerializedName;

public class RunningInfo extends AbstractLocatePackage{
    @SerializedName("voice_status")
    private String voiceStatus;
    @SerializedName("camera_Status")
    private String  cameraStatus;
    @SerializedName("credit_status")
    private String creditStatus;

    public RunningInfo() {
        methodName= WSConstant.RemoteLocationConstant.ACREDIT_RUNNING_STATUS;
    }

    public String getVoiceStatus() {
        return voiceStatus;
    }

    public void setVoiceStatus(String voiceStatus) {
        this.voiceStatus = voiceStatus;
    }

    public String getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(String cameraStatus) {
        this.cameraStatus = cameraStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    @Override
    public String toString() {
        return "RunningInfo{" +
                "voiceStatus='" + voiceStatus + '\'' +
                ", cameraStatus='" + cameraStatus + '\'' +
                ", creditStatus='" + creditStatus + '\'' +
                '}';
    }
}
