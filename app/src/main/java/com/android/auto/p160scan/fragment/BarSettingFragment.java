package com.android.auto.p160scan.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.auto.p160scan.R;
import com.android.auto.p160scan.ScannerServices;
import com.android.auto.p160scan.activity.AdvancedSettingActivity;
import com.android.auto.p160scan.utility.ConstantUtil;
import com.android.auto.p160scan.utility.ScanLog;
import com.android.auto.p160scan.utility.Variable;


public class BarSettingFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener{
    private static String TAG = "BarSettingFragment";
    private Context mContext = null;
    private SwitchPreference m_key_barcode;
    private CheckBoxPreference m_key_beep;
    private CheckBoxPreference m_key_vibrate;
    private CheckBoxPreference m_key_light;
    private ListPreference key_terminator;

    private CheckBoxPreference m_key_auto;
    private CheckBoxPreference m_key_power;

    private PreferenceScreen m_key_about;
    private PreferenceScreen m_key_exit;
    public static BarSettingFragment instance = null;

    public BarSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mybasicsetting);
        mContext = getActivity();
        instance = this;

        m_key_barcode = ((SwitchPreference) findPreference(ConstantUtil.key_barcode));
        m_key_beep = ((CheckBoxPreference) findPreference(ConstantUtil.key_beep));
        m_key_vibrate = ((CheckBoxPreference) findPreference(ConstantUtil.key_vibrate));

        m_key_auto = ((CheckBoxPreference) findPreference(ConstantUtil.key_auto));
        m_key_power = ((CheckBoxPreference) findPreference(ConstantUtil.key_power));

        m_key_light = ((CheckBoxPreference) findPreference(ConstantUtil.key_light));
        m_key_about = ((PreferenceScreen) findPreference(ConstantUtil.key_about));
        m_key_exit = ((PreferenceScreen) findPreference(ConstantUtil.key_exit));
        m_key_barcode.setOnPreferenceChangeListener(this);
        m_key_beep.setOnPreferenceChangeListener(this);
        m_key_vibrate.setOnPreferenceChangeListener(this);

        m_key_auto.setOnPreferenceChangeListener(this);
        m_key_power.setOnPreferenceChangeListener(this);

        m_key_light.setOnPreferenceChangeListener(this);
        m_key_about.setOnPreferenceChangeListener(this);
        m_key_exit.setOnPreferenceChangeListener(this);
        key_terminator = ((ListPreference) findPreference(ConstantUtil.key_terminator));
        key_terminator.setOnPreferenceChangeListener(this);
        key_terminator.setSummary("        " + key_terminator.getEntry());
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        final String key = preference.getKey();
        if (ConstantUtil.key_about.equals(key)) {
            showAboutDialog();
        } else if (ConstantUtil.key_exit.equals(key)) {
            showExitDialog();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    /**
     * 输入密码对话框
     * @param key_title
     */
    public void showPassswordDialog(final String key_title) {
        LinearLayout inputLayout;
        inputLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.input2, null);
        final EditText mCurrentFormat = inputLayout.findViewById(R.id.xET);
        new AlertDialog.Builder(getActivity())
                .setTitle(key_title)
                .setView(inputLayout)
                .setPositiveButton(
                        getString(R.string.iscan_password_ok),
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCurrentFormat.getText().toString().equals(Variable.getInstance(getActivity()).GetAdvancePassword())) {
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, AdvancedSettingActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), getActivity().getString(R.string.iscan_password_error), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        })

                .setNegativeButton(
                        getString(R.string.iscan_password_cancel),
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        if (ConstantUtil.key_terminator.equals(key)
                || ConstantUtil.key_charset.equals(key)) {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            int index = listPreference.findIndexOfValue((String) newValue);
            listPreference.setSummary("        " + entries[index]);
            ScanLog.getInstance(mContext).LOGD("Additional function:"+entries[index]);
        }

        startService();

        if (ConstantUtil.key_barcode.equals(key)) {
            if (!m_key_barcode.isChecked())
                ScanLog.getInstance(mContext).LOGD(" manual:Open Scanner");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Close Scanner");
            }
        }

        else if (ConstantUtil.key_beep.equals(key)) {
            if (!m_key_beep.isChecked())
                ScanLog.getInstance(mContext).LOGD("manual:Enable Beeper");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Disable Beeper");
            }
        }

        else if (ConstantUtil.key_vibrate.equals(key)) {
            if (!m_key_vibrate.isChecked())
                ScanLog.getInstance(mContext).LOGD("manual:Enable Vibrate");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Disable Vibrate");
            }
        }

        else if (ConstantUtil.key_light.equals(key)) {
            if (!m_key_light.isChecked())
                ScanLog.getInstance(mContext).LOGD("manual:Enable Light");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Disable Light");
            }
        }

        else if (ConstantUtil.key_auto.equals(key)) {
            if (!m_key_auto.isChecked())
                ScanLog.getInstance(mContext).LOGD("manual:Autorun on");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Autorun off");
            }
        }

        else if (ConstantUtil.key_power.equals(key)) {
            if (!m_key_power.isChecked())
                ScanLog.getInstance(mContext).LOGD("manual:Power on");
            else {
                ScanLog.getInstance(mContext).LOGD("manual:Power off");
            }
        }
        return true;
    }

    /**
     * 退出
     */
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.iscan);
        builder.setTitle("退出应用无法后台扫描");
        builder.setMessage("确定退出应用吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService();
                getActivity().onBackPressed();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 关于
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.iscan);
        builder.setTitle("软件版本");
        builder.setMessage(getVersionName(mContext));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 获取apk版本信息
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取apk的versionCode
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return versionCode;
    }


    public void startService() {
        Intent i = new Intent(mContext, ScannerServices.class);
        mContext.startService(i);
    }

    public void stopService() {
        Intent i = new Intent(mContext, ScannerServices.class);
        mContext.stopService(i);
    }

}
