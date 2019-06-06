package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.bgToast;
import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;
import com.xw.repo.BubbleSeekBar;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolName;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_SPEED_LEVEL;

/**
 * 设置界面碎片
 * lp
 * 2019/5/30
 */
public class SetFragment extends BaseFragment {
    private static final String TAG = "SetFragment";
    private LinearLayout linearLayout;
    private TextView tv_id, tv_schoolname, tv_appversion, tv_voice, tv_speed;
    private BubbleSeekBar voiceSeekBar, speedSeekBar;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        initView(view);
        return view;
    }


    private void initView(View view) {
        linearLayout = view.findViewById(R.id.ll_set);
        setLinerLayoutViewSize(linearLayout);

        tv_id = view.findViewById(R.id.text_dev_id);
        tv_schoolname = view.findViewById(R.id.text_schoolName);
        tv_appversion = view.findViewById(R.id.text_app_ver);
        tv_voice = view.findViewById(R.id.tv_voice);
        tv_speed = view.findViewById(R.id.tv_voice_speed);

        voiceSeekBar = view.findViewById(R.id.BubbleSeekBar_voice);
        voiceSeekBar.setOnProgressChangedListener(new VoiceProgressChangedListener());

        speedSeekBar = view.findViewById(R.id.BubbleSeekBar_voice_speed);
        speedSeekBar.setOnProgressChangedListener(new SpeedProgressChangedListener());


    }

    private void initData() {
        tv_voice.setText(String.valueOf(SPUtil.readInt(SP_NAME, VOICE_LEVEL)));
        tv_speed.setText(String.valueOf(SPUtil.readFloat(SP_NAME, VOICE_SPEED_LEVEL)));
        voiceSeekBar.setProgress(SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        speedSeekBar.setProgress(SPUtil.readFloat(SP_NAME, VOICE_SPEED_LEVEL));
        tv_schoolname.setText(SPUtil.readString(SP_NAME, SchoolName));
        tv_appversion.setText(AppUtils.getAppVersionName(Myapplication.getInstance().getApplicationContext()));
        tv_id.setText(AppUtils.getLocalMacAddressFromWifiInfo(Myapplication.getInstance().getApplicationContext()));

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }

    }

    private class VoiceProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            tv_voice.setText("" + progress);
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnActionUp: " + progress);
            SPUtil.writeInt(SP_NAME, VOICE_LEVEL, progress);
            bgToast.myToast(activity, "设置成功", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("音量" + progress, progress);
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnFinally: " + progress);

        }
    }

    private class SpeedProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            tv_speed.setText("" + progressFloat);
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnActionUp: " + progressFloat);
            TtsSpeek.getInstance().setSpeed(progressFloat);
            SPUtil.writeFloat(SP_NAME, VOICE_SPEED_LEVEL, progressFloat);
            bgToast.myToast(activity, "设置成功", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("语速" + progressFloat, SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnFinally: " + progress);

        }
    }
}
