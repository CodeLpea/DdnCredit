package com.example.lp.ddncredit.mainview.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lp.ddncredit.R;

public class bgToast {

    public static void myToast(Activity activity, String str, int xlocation, int ylocation)

    {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.bg_toast, (ViewGroup)activity.findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast =new Toast(activity);
        toast.setGravity(Gravity.CENTER, xlocation, ylocation);

        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setView(view);

        toast.show();

    }

}
