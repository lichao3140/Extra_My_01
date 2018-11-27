package com.android.auto.p160scan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.auto.p160scan.R;


public class BarSettingFragment extends PreferenceFragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private View myView;
    private Context mContext = null;
    private SwitchPreference m_key_barcode;
    private CheckBoxPreference m_key_beep;
    private CheckBoxPreference m_key_vibrate;
    private CheckBoxPreference m_key_light;
    private ListPreference key_terminator;

    private CheckBoxPreference m_key_auto;
    private CheckBoxPreference m_key_power;

    private PreferenceScreen m_key_setting;
    private PreferenceScreen m_key_about;
    private PreferenceScreen m_key_exit;
    public static BarSettingFragment instance = null;
    private TextView tvTitle;
    private Button btnBack;
    private PreferenceScreen root;

    public static BarSettingFragment newInstance(String param1) {
        BarSettingFragment fragment = new BarSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    public BarSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        addPreferencesFromResource(R.xml.basicsetting);
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        myView = inflater.inflate(R.layout.fragment_bar_setting, container, false);
//        return myView;
//    }
}
