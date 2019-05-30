package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lp.ddncredit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {
    private static final String TAG="SetFragment";


    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }
    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "SetFragment onAttach: ");
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "SetFragment onDetach: ");

    }

}
