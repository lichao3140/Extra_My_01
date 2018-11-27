package com.android.auto.p160scan.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.auto.p160scan.R;
import com.android.auto.p160scan.utility.AScanLog;
import com.android.auto.p160scan.utility.ScanSettingUtil;

import java.lang.reflect.Method;
import java.util.Calendar;


public class ScanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private EditText siteCodeEdit;
    private TextView total_num;
    private TextView scan_len;
    private Button btStart;
    private View myView;
    private TextView decode_time;
    private IntentFilter mFilter;
    private BroadcastReceiver mReceiver;
    private Context mContext;
    long totaltime = 0;

    public static ScanFragment newInstance(String param1) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    public ScanFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    decode_time.setText(String.valueOf(msg.arg1));
                    siteCodeEdit.setText(msg.obj + "");
                    scan_len.setText(String.valueOf(msg.arg2));
                    decode_time.invalidate();
                    total_num.setText(String.valueOf(totaltime));
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_scan, container, false);

        this.mContext = getActivity();

        btStart = myView.findViewById(R.id.start_btn);
        btStart.setOnClickListener(btStart_listener);
        iniHolder();

        siteCodeEdit = myView.findViewById(R.id.scanresult);
        decode_time = myView.findViewById(R.id.decode_time);
        scan_len = myView.findViewById(R.id.decode_len);
        total_num = myView.findViewById(R.id.decode_total);

        mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 此处获取扫描结果信息
                final String scanResult = intent.getStringExtra("value");
                int decodetime = intent.getIntExtra("decodetime", 0);
                AScanLog.getInstance(context).LOGD(scanResult);

                if (scanResult.startsWith("%")) {
                    String result = scanResult.toString().trim();
                    enterSetting(result);
                }

                if (scanResult.length() > 0) {
                    Message msg = Message.obtain();
                    msg.what = 100;
                    msg.obj = scanResult;
                    msg.arg1 = decodetime;
                    msg.arg2 = scanResult.length();
                    handler.sendMessage(msg);
                    totaltime++;
                }
            }
        };
        return myView;
    }

    private void enterSetting(String code) {
        isTime();
        if (code.equals(ScanSettingUtil.SETTING_ENTER)) { // 进入设置
            Toast.makeText(mContext, code, Toast.LENGTH_LONG).show();
        } else if (code.equals(ScanSettingUtil.SETTING_EXIT)) { // 退出设置
            Toast.makeText(mContext, code, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTime() {
        long previous = 0L;
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        if(now - previous <= 1000 * 10) {
            previous = now ;
            return true;
        }
        return false;
    }


    private View.OnClickListener btStart_listener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            decode_time.setText("");
            scan_len.setText("");
            total_num.setText("");
            ScanFragment.Holder.tv_null.setText("");
            totaltime = 0;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        handler.post(ScrollRunnable);
        mContext.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onPause() {
        // 注销获取扫描结果的广播
        mContext.unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReceiver = null;
        mFilter = null;
//        AScanLog.getInstance(mContext).disInstance();
        handler.removeCallbacks(ScrollRunnable);
    }

    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int   line=getCurrentCursorLine(ScanFragment.Holder.tv_null);
            //	Log.d("012","line:::"+line);
            if (line > 50) {
                int index = ScanFragment.Holder.tv_null.getSelectionStart();
                Editable editable = ScanFragment.Holder.tv_null.getText();
                editable.delete(0, index/2);
            }
            handler.postDelayed(this, 10);
        }
    };

    private int getCurrentCursorLine(EditText editText) {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();

        if (selectionStart != -1&&layout!=null) {
            return layout.getLineForOffset(selectionStart) + 1;
        }
        return -1;
    }

    private void iniHolder() {
        //Holder.scroll = (ScrollView) findViewById(R.id.sv_scroll);
        ScanFragment.Holder.mlayout = myView.findViewById(R.id.l_test);
        ScanFragment.Holder.tv_null = myView.findViewById(R.id.scanresult);
        //Holder.tv_null.setHeight(getWindowManager().getDefaultDisplay()
        //		.getHeight()/2
        //);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setSoftInputShownOnFocus;
            setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setSoftInputShownOnFocus.setAccessible(true);
            setSoftInputShownOnFocus.invoke(ScanFragment.Holder.tv_null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Holder {
        //static ScrollView scroll;
        static LinearLayout mlayout;
        static EditText tv_null;
    }

}
