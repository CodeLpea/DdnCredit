package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.expression.ExpressionMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.NoPersionExpression;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpressionFragment extends Fragment {
    private static final String TAG = "ExpressionFragment";
    private GifImageView gifImageView;


    public ExpressionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expression, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        gifImageView = view.findViewById(R.id.giv_express);

        try {
            gifImageView.setImageDrawable((new GifDrawable(getActivity().getAssets(), NoPersionExpression)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理考勤模块发来的表情变化信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ExpressionEvent(ExpressionMessage expressionMessage) {
        Log.i(TAG, "表情界面得到表情变化: ");
        if (expressionMessage.getExpression() != null && !expressionMessage.getExpression().isEmpty()) {
            try {
                gifImageView.setImageDrawable((new GifDrawable(getActivity().getAssets(), expressionMessage.getExpression())));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        EventBus.getDefault().unregister(this);//在pause的时候注销eventbus

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        EventBus.getDefault().register(this);//onResume中注册evnetbus

    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "ExpressionFragment onAttach: ");
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "ExpressionFragment onDetach: ");

    }
}
