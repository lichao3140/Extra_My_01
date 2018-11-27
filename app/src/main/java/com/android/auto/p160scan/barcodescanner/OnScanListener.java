package com.android.auto.p160scan.barcodescanner;

public interface OnScanListener {
	boolean doOpen();

	boolean doClose();

	void doStart();

	void doStop();

	void setListener(ScanCallBack listener);

	int setParams(int num, int value);

	int getParams(int num);
	
	byte[] GetLastImage();
	
}

