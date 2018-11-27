package com.android.auto.p160scan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.auto.p160scan.R;
import com.android.auto.p160scan.ScannerServices;
import com.android.auto.p160scan.utility.ScanLog;

public class ExitActivity extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.exit_dialog);
		WindowManager m = getWindowManager();
		layout = (LinearLayout) findViewById(R.id.exit_layout);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
	}

	public void exitbutton0(View v) {
		this.finish();
		stopService();
		SettingActivity.SettingFragement.instance.getActivity().finish();
		ScanLog.getInstance(this).LOGD(
				"iscanÒÑ±»ÍË³ö" );
	}

	public void stopService() {
		Intent i = new Intent(this, ScannerServices.class);
		stopService(i);
	}

}
