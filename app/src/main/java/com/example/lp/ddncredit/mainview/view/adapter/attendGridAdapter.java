package com.example.lp.ddncredit.mainview.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class attendGridAdapter extends BaseAdapter {
    private Context context;
    private attendParentBean attendBean;
    private GridView gv;


    public attendGridAdapter(attendParentBean attendBean, Context context, GridView gv) {
        this.attendBean=attendBean;
        this.context = context;
        this.gv = gv;
    }

    @Override
    public int getCount() {
        int count=attendBean.getPhotoShowBean().getRelation().length;//获取总共有多少个家长
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        return null;
    }
}
