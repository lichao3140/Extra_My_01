package com.android.auto.p160scan.barcodescanner;

import android.content.Context;

public abstract class BaseScan implements OnScanListener {

	public ScanCallBack mListener = null;
	protected static Context mContext = null;

	public BaseScan(Context context) {
		mContext = context;
	}
	@Override
	public void setListener(ScanCallBack listener) {
		// TODO Auto-generated method stub
		this.mListener = listener;
	}

	
}
