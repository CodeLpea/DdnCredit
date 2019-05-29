package com.example.lp.ddncredit.http;


import android.util.Log;

import com.didanuo.robot.libconfig.InvisibleConfig;
import com.didanuo.robot.libconfig.InvisibleConfigKey;
import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.http.model.ResponseEntry;
import com.example.lp.ddncredit.http.model.SchoolKeyEntry;
import com.example.lp.ddncredit.http.model.SchoolStaffsInfoEntry;
import com.example.lp.ddncredit.http.model.SchoolStudentsInfoEntry;
import com.example.lp.ddncredit.http.model.ServerTimeEntry;
import com.example.lp.ddncredit.http.model.StaffAttendancedInfoEntry;
import com.example.lp.ddncredit.http.model.StuAttendancedInfoEntry;
import com.example.lp.ddncredit.http.model.StuAttendancedStatusRequest;
import com.example.lp.ddncredit.http.model.StuAttendancedStatusResponse;
import com.example.lp.ddncredit.http.model.UpgradePackageVersionInfoEntry;
import com.example.lp.ddncredit.http.model.UpgradePackageVersionInfoRequest;
import com.google.gson.Gson;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lp.ddncredit.constant.Constants.SOFTWARE_SYSTEM_TYTE;


/**
 * 提供与服务器通信的接口
 * Created by Long on 2018/7/24.
 */

public class CloudClient {
    private static final String TAG = "CloudClient";
    //创建retrofit对象
    private static Retrofit mRetrofit = null;
    private static NetWorkAPIs mAPIs = null;
    private static Gson mGson = null;
    private CloudClient(){
        init();
    }

    private void init(){
        //创建OkHttp对象
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(headerInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        //创建retrofit对象
        mRetrofit = new Retrofit.Builder()
               // .baseUrl("http://www.didano.cn")
                .baseUrl(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SERVER_DOMAIN))
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建接口对象
        mAPIs = mRetrofit.create(NetWorkAPIs.class);

        mGson = new Gson();
    }

    private static class CloudClientInner{
        private static CloudClient mInstance = new CloudClient();
    }

    public static CloudClient getInstance(){
        return CloudClientInner.mInstance;
    }

    private static Interceptor headerInterceptor = new Interceptor(){
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().
                    addHeader("device_no", AppUtils.getLocalMacAddressFromWifiInfo(Myapplication.getInstance()))
                    .build();
            Log.i(TAG, "request body : " + request.toString() + " headers : " + request.header("device_no"));
            return chain.proceed(request);
        }
    };

    /**
     * 以下是实现的网络请求接口
     */

    /**
     * 获取绑定学校的Key值
     * @return 失败返回null
     */
    public SchoolKeyEntry getSchoolKeyStr(){
        SchoolKeyEntry key = null;
        Call<ResponseEntry<SchoolKeyEntry>> call = mAPIs.getSchoolKey(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SCHOOL_KEY_URL));
        ResponseEntry<SchoolKeyEntry> response;
        try {
            /**
             * 阻塞请求
             */
            response = call.execute().body();
            //{"code":"1","message":"Access deny","data_len":"1234567890","success":"false"}
            if(response != null && response.getCode() == ResponseEntry.SUCCESS) {
                key = response.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return key;
    }

    /**
     * 获取学校的学生信息
     * @return 成功返回学生信息，失败返回null
     */
    public SchoolStudentsInfoEntry getSchoolStudentList(){
        SchoolStudentsInfoEntry studentsInfoEntry = null;
        Call<ResponseEntry<SchoolStudentsInfoEntry>> call = mAPIs.getSchoolStudentList(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SCHOOL_STUDENTS_LIST_URL));
        ResponseEntry<SchoolStudentsInfoEntry> response;
        try{
            response = call.execute().body();
            if(response != null && response.getCode() == ResponseEntry.SUCCESS){
                studentsInfoEntry = response.getData();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return studentsInfoEntry;
    }

    /**
     * 获取学校职工信息
     * @return 成功返回职工信息，失败返回null
     */
    public SchoolStaffsInfoEntry getSchoolStaffList(){
        SchoolStaffsInfoEntry staffsInfoEntry = null;
        Call<ResponseEntry<SchoolStaffsInfoEntry>> call = mAPIs.getSchoolStaffList(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SCHOOL_STAFFS_LIST_URL));
        ResponseEntry<SchoolStaffsInfoEntry> response;
        try{
            response = call.execute().body();
            if(response != null && response.getCode() == ResponseEntry.SUCCESS){
                staffsInfoEntry = response.getData();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return staffsInfoEntry;
    }

    /**
     * 获取服务器时间
     * @return 成功返回服务器时间，失败返回null
     */
    public ServerTimeEntry getServerTime(){
        ServerTimeEntry time = null;
        Call<ResponseEntry<ServerTimeEntry>> call = mAPIs.getServerTime(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SERVER_TIME_URL));
        ResponseEntry<ServerTimeEntry> response;
        try{
            response = call.execute().body();
            if(response != null && response.getCode() == ResponseEntry.SUCCESS){
                Log.i(TAG, "getServerTime : code : " + response.getCode() + " message : " + response.getMessage()
                + " data_len : " + response.getData_len() + " success : " + response.isSuccess());
                time = response.getData();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 在上传接送记录之前，先检查服务器上当前刷卡人的状态
     * @param request 查询请求
     * @return 成功返回StuAttendancedStatusResponse对象，失败返回null
     */
    public StuAttendancedStatusResponse queryStudentAttendancedStatus(StuAttendancedStatusRequest request){
        StuAttendancedStatusResponse stuAttendancedStatusResponse = null;
        String requestStr = mGson.toJson(request);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestStr);
        Call<ResponseEntry<StuAttendancedStatusResponse>> call = mAPIs.queryStudentAttendancedStatus(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_VALID_BRUSH_URL), body);
        ResponseEntry<StuAttendancedStatusResponse> response;
        long start = System.currentTimeMillis();
        try{
            response = call.execute().body();
            long end = System.currentTimeMillis();
            Log.i(TAG, call.request().url().uri().getPath() + " time : " + (end - start) + " ms");
            if(response != null ){
                int code = response.getCode();
                Log.i(TAG, "response : " + mGson.toJson(response));
                if(code == ResponseEntry.SUCCESS) {
                    stuAttendancedStatusResponse = response.getData();
                }
            }
        }catch (SocketTimeoutException e){
            e.printStackTrace();
            long end = System.currentTimeMillis();
            Log.i(TAG, call.request().url().uri().getPath() + " timeout : " + (end - start) + " ms");
        } catch(Exception e){
            e.printStackTrace();
        }

        return stuAttendancedStatusResponse;
    }

    /**
     * 上传学生考勤信息
     * @param attendancedInfoEntry
     */
    public boolean uploadStudentAttendancedInfo(StuAttendancedInfoEntry attendancedInfoEntry){
        boolean result = false;
        String requestStr = mGson.toJson(attendancedInfoEntry);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestStr);
        Call<ResponseEntry> call = mAPIs.uploadStudentAttendancedInfo(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SUBMIT_TAKE_AWAY_URL), body);
        ResponseEntry response;
        try{
            response = call.execute().body();
            if(response != null){
                if(response.getCode() == ResponseEntry.SUCCESS){
                    Log.i(TAG, mGson.toJson(response));
                    result = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 上传员工考勤信息
     * @param attendancedInfoEntry
     */
    public boolean uploadStaffAttendancedInfo(StaffAttendancedInfoEntry attendancedInfoEntry){
        boolean result = false;
        String requestStr = mGson.toJson(attendancedInfoEntry);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestStr);
        Call<ResponseEntry> call = mAPIs.uploadStaffAttendancedInfo(InvisibleConfig.getConfig(InvisibleConfigKey.KINDERGARTEN_SUBMIT_STAFF_SIGN_URL), body);
        ResponseEntry response;
        try{
            response = call.execute().body();
            if(response != null && response.getCode() == ResponseEntry.SUCCESS){
                Log.i(TAG, "uploadStaffAttendancedInfo--> " + mGson.toJson(response));
                result = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取当前服务器上最新升级包版本信息
     * @return 成功返回 true, 失败返回false
     */
    public UpgradePackageVersionInfoEntry getUpgradePackageVersionInfo(String appVersion){
        UpgradePackageVersionInfoEntry versionInfo = null;
        UpgradePackageVersionInfoRequest upgradePackageVersionInfoRequest = new UpgradePackageVersionInfoRequest();
        upgradePackageVersionInfoRequest.setHardWareDeviceType("NuoShua");
        upgradePackageVersionInfoRequest.setHardWareDeviceNumber(AppUtils.getLocalMacAddressFromWifiInfo(Myapplication.getInstance()));
        upgradePackageVersionInfoRequest.setCurrentApkVersion(appVersion);
        upgradePackageVersionInfoRequest.setSoftWareType(SOFTWARE_SYSTEM_TYTE);
        String requestStr = mGson.toJson(upgradePackageVersionInfoRequest);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestStr);
        Call<ResponseEntry<UpgradePackageVersionInfoEntry>> call = mAPIs.getUpgradePackageVersionInfo(InvisibleConfig.getConfig(InvisibleConfigKey.GET_APP_VERSION_URL), body);
        ResponseEntry<UpgradePackageVersionInfoEntry> response;
        try{
            response = call.execute().body();
            if(response != null) {
                int code = response.getCode();
                Log.i(TAG, "response : " + mGson.toJson(response));
                if (code == ResponseEntry.SUCCESS) {
                    versionInfo = response.getData();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return versionInfo;
    }
}
