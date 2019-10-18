package com.example.lp.ddncredit.websocket.bean;


import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.websocket.LocationDataCacher;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.example.lp.ddncredit.constant.Constants.SOFTWARE_SYSTEM_TYTE;


/**
 * 其他采集数据结构必须继承该抽象类
 * Created by Long on 2018/3/6.
 */

public abstract class AbstractLocatePackage {
    protected transient String methodName; //使用transient修饰，这样gson就不会其当做json字段来解析
    protected String getMethodName() {
        return methodName;
    }
    public void upload(){
        //生成json包
        RemoteLocatePackage locatePackage = new RemoteLocatePackage();
        locatePackage.setMethodName(this.getMethodName());
        locatePackage.setDeviceNo(AppUtils.getLocalMacAddressFromWifiInfo(Myapplication.getInstance()));
        locatePackage.setSystemType(String.valueOf(SOFTWARE_SYSTEM_TYTE));
        //将采集数据对象转化成JsonObject对象
        Gson payload = new Gson();
        JsonObject object = new JsonParser().parse(payload.toJson(this)).getAsJsonObject();
        locatePackage.setPlayload(object);
        //将与服务器通信的远程定位json包转成json字符串，存储到队列中
        String msg = new Gson().toJson(locatePackage);
        LocationDataCacher.getInstance().PushCacher(msg);
    }
}
