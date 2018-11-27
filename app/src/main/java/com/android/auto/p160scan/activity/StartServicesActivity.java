package com.android.auto.p160scan.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.BarcodeJNI;
import com.android.auto.p160scan.ScannerServices;
import com.android.auto.p160scan.utility.ConstantUtil;

public class StartServicesActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isServiceWork(this, ConstantUtil.ISCAN_SERVER_NAME)) {
			
			Intent intent = new Intent();
			intent.setClass(this, MainViewActivity.class);
			//intent.setClass(this, ScrollViewActivity.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			
			finish();
			return;
		}
		int id = BarcodeJNI.GetPlatform();
		if (id == 0) {
			Intent i = new Intent(this, ScannerServices.class);
			startService(i);
		}
		finish();
	}

	public boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

}
