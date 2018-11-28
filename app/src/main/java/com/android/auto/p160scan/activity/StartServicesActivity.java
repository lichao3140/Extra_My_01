package com.android.auto.p160scan.activity;

import java.util.List;
import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.android.BarcodeJNI;
import com.android.auto.p160scan.ScannerServices;
import com.android.auto.p160scan.utility.ConstantUtil;

public class StartServicesActivity extends Activity {

	private Toast toast = null;
	private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
	private static final String[] NEEDED_PERMISSIONS = new String[]{
			Manifest.permission.CAMERA,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

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
		if (!checkPermissions(NEEDED_PERMISSIONS)) {
			ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
		} else {
			startService();
		}
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == ACTION_REQUEST_PERMISSIONS) {
			boolean isAllGranted = true;
			for (int grantResult : grantResults) {
				isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
			}
			if (isAllGranted) {
				startService();
			} else {
				showToast("权限获取失败");
			}
		}
	}

	private void startService() {
		int id = BarcodeJNI.GetPlatform();
		if (id == 0) {
			Intent i = new Intent(this, ScannerServices.class);
			startService(i);
		}
		finish();
	}

	private boolean checkPermissions(String[] neededPermissions) {
		if (neededPermissions == null || neededPermissions.length == 0) {
			return true;
		}
		boolean allGranted = true;
		for (String neededPermission : neededPermissions) {
			allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
		}
		return allGranted;
	}

	private void showToast(String s) {
		if (toast == null) {
			toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
			toast.show();
		} else {
			toast.setText(s);
			toast.show();
		}
	}
}
