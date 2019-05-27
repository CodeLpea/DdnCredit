package com.example.lp.ddncredit.http;

import com.example.lp.ddncredit.http.model.ResponseEntry;
import com.example.lp.ddncredit.http.model.SchoolKeyEntry;
import com.example.lp.ddncredit.http.model.SchoolStaffsInfoEntry;
import com.example.lp.ddncredit.http.model.SchoolStudentsInfoEntry;
import com.example.lp.ddncredit.http.model.ServerTimeEntry;
import com.example.lp.ddncredit.http.model.StuAttendancedStatusResponse;
import com.example.lp.ddncredit.http.model.UpgradePackageVersionInfoEntry;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Long on 2018/7/24.
 */

public interface NetWorkAPIs {
    /**
     * 获取学校Key
     */
    @GET
    Call<ResponseEntry<SchoolKeyEntry>> getSchoolKey(@Url String url);
    /**
     * 获取学生信息列表
     */
    @GET
    Call<ResponseEntry<SchoolStudentsInfoEntry>> getSchoolStudentList(@Url String url);

    /**
     * 获取员工信息列表
     */
    @GET
    Call<ResponseEntry<SchoolStaffsInfoEntry>> getSchoolStaffList(@Url String url);

    /**
     * 获取服务器的时间
     */
    @GET
    Call<ResponseEntry<ServerTimeEntry>> getServerTime(@Url String url);

    /**
     * 上传学生考勤数据
     */
    @POST
    Call<ResponseEntry> uploadStudentAttendancedInfo(@Url String url, @Body RequestBody body);

    /**
     * 上传职工考勤数据
     */
    @POST
    Call<ResponseEntry> uploadStaffAttendancedInfo(@Url String url, @Body RequestBody body);

    /**
     * 接送状态查询
     */
    @POST
    Call<ResponseEntry<StuAttendancedStatusResponse>> queryStudentAttendancedStatus(@Url String url, @Body RequestBody body);

    /**
     * 获取服务器最新升级包版本信息
     */
    @POST
    Call<ResponseEntry<UpgradePackageVersionInfoEntry>> getUpgradePackageVersionInfo(@Url String url, @Body RequestBody body);
}
