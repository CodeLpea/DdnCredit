package com.example.lp.ddncredit.datebase;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class testDbEntry extends LitePalSupport {

    private int id;

    //name是唯一的，且默认值为unknown
    @Column(unique = true, defaultValue = "unknown")
    private String name;

    //忽略即是不在数据库中创建该属性对应的字段
    @Column(ignore = true)
    private float price;

    private byte[] cover;
    private int duration;

    //不为空
    @Column(nullable = false)
    private String director;

    private String type;

    //记得添加所有字段的getter和setter方法
}

