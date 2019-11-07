package com.example.lp.ddncredit.websocket.bean;

import com.example.lp.ddncredit.websocket.WSConstant;
import com.google.gson.annotations.SerializedName;

public class SoftWareVersionsInfo extends AbstractLocatePackage {
    @SerializedName("andriod_version_software")
    private String    software; //APP软件版本
    @SerializedName("andriod_version_system")
    private String    system; //系统版本
    @SerializedName("andriod_start_time")
    private String    time; //启动时间
    public SoftWareVersionsInfo(){
        methodName = WSConstant.RemoteLocationConstant.CREDIT_SOFTWARE_VERIOSN;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String kernel) {
        this.time = kernel;
    }

    @Override
    public String toString() {
        return "SoftWareVersionsInfo{" +
                "software='" + software + '\'' +
                ", system='" + system + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
