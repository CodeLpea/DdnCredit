package com.example.lp.ddncredit.mainview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.lp.ddncredit.R;

public class AttendGridAdapter extends BaseAdapter {
    private Context context;
    private AttendShowBean attendBean;
    private GridView gv;


    public AttendGridAdapter(AttendShowBean attendBean, Context context, GridView gv) {
        this.attendBean=attendBean;
        this.context = context;
        this.gv = gv;
    }

    @Override
    public int getCount() {
     return attendBean.getReletions().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_attendgridphoto, null);

        RelativeLayout relativeLayout =  convertView.findViewById(R.id.rel_photo);//获取每个页面内容的包裹布局，用来设置宽度高度
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        ImageView imageView =  convertView.findViewById(R.id.iv_photo);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
        if (attendBean.getReletions().size()>3){
            gv.setPadding(0,10,10,10);
            params.height = 380;
            params.width = 285;
        }else if(attendBean.getReletions().size()==3) {
            gv.setPadding(0,180,10,0);
            gv.setHorizontalSpacing(10);
            params.height = 380;
            params.width = 285;
        }else if(attendBean.getReletions().size()<3) {
            gv.setPadding(0,80,0,0);
            gv.setHorizontalSpacing(10);
            params.height = 640;
            params.width = 480;
        }
        relativeLayout.setLayoutParams(params);
        Glide.with(context)
                .load(attendBean.getUrls().get(position))
                .placeholder(R.drawable.attenddefault)//图片加载出来前，显示的图片
                .error(R.drawable.attenddefault)//图片加载失败后，显示的图片
                .override(params.width, params.height)//指定图片大小
                .into(imageView);

        Button buttonName=convertView.findViewById(R.id.ib_photoname);
        buttonName.setText(attendBean.getReletions().get(position));
        return convertView;
    }

    public void notifiData(AttendShowBean attendBean){
        this.attendBean=attendBean;
        this.notifyDataSetChanged();
    }
}
