package com.android.auto.p160scan.barcodescanner;

public interface ScanCallBack {
	void onScanResults(byte[] data);
	
	void onScanResults(String data);
	int onFail(int code) ;
	

}
