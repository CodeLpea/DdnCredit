package com.example.lp.ddncredit.mainview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.AttendGridView;
import com.example.lp.ddncredit.mainview.view.adapter.attendParentBean;
import com.example.lp.ddncredit.mainview.view.adapter.attendGridAdapter;

public class attendFragment extends BaseFragment {
    private static final String TAG="attendFragment";
    private LinearLayout linearLayoutMain;
    private TextView tv_role,tv_babyname,tv_clazzname,tv_icnumber,tv_attendtime;
    private Button backbotton;
    private int backcountTime;

    private AttendGridView attendGridView;
    private attendGridAdapter attendGridAdapter;

    private attendParentBean attendBean;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attend, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        linearLayoutMain=view.findViewById(R.id.ll_attend);
        setLinerLayoutViewSize(linearLayoutMain);//设置为中心区域显示


        tv_role=view.findViewById(R.id.tv_Role);
        tv_babyname=view.findViewById(R.id.tv_babyname);
        tv_clazzname=view.findViewById(R.id.tv_clazzname);
        tv_icnumber=view.findViewById(R.id.tv_icnumber);
        tv_attendtime=view.findViewById(R.id.tv_attendtime);

        backbotton=view.findViewById(R.id.bt_attendback);
        backcountTime=5;//初始化倒计时时间
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回ExpressionFragment
            }
        });


        attendGridView=view.findViewById(R.id.grid_photoview);
        attendGridView.setNumColumns(6);
        attendGridAdapter=new attendGridAdapter(new attendParentBean(),this.getContext(),attendGridView);
        attendGridView.setAdapter(attendGridAdapter);


    }


}
