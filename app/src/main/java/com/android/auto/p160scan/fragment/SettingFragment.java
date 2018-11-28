package com.android.auto.p160scan.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.TextView;

import com.android.auto.p160scan.R;


public class SettingFragment extends PreferenceFragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private TextView textView;

    public static SettingFragment newInstance(String param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        addPreferencesFromResource(R.xml.advancesetting);
    }

}
