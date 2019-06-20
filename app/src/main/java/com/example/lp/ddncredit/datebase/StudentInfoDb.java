package com.example.lp.ddncredit.datebase;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生信息表，一个学生id对应多个家长，与家长表形成多对一关系
 *lp
 * 2019/05/29
 * */
public class StudentInfoDb extends LitePalSupport {
    private int id;
    private String name;
    private long stuId;
    private String clazztitle;
    private List<ParentInfoDb> parentInfoDbListList = new ArrayList<ParentInfoDb>();//多对一

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStuId() {
        return stuId;
    }

    public void setStuId(long stuId) {
        this.stuId = stuId;
    }

    public List<ParentInfoDb> getParentInfoDbListList() {
        return parentInfoDbListList;
    }

    public void setParentInfoDbListList(List<ParentInfoDb> parentInfoDbListList) {
        this.parentInfoDbListList = parentInfoDbListList;
    }

    public String getClazztitle() {
        return clazztitle;
    }

    public void setClazztitle(String clazztitle) {
        this.clazztitle = clazztitle;
    }

    @Override
    public String toString() {
        return "StudentInfoDb{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stuId=" + stuId +
                ", clazztitle='" + clazztitle + '\'' +
                ", parentInfoDbListList=" + parentInfoDbListList +
                '}';
    }
}
