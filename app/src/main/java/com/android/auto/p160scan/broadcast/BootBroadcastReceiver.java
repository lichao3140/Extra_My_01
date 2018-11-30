package com.android.auto.p160scan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.auto.p160scan.activity.StartServicesActivity;
import com.android.auto.p160scan.utility.ConstantUtil;
import com.android.auto.p160scan.utility.Variable;

public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ConstantUtil.ISCAN_BOOT_ACTION)) {
			if (Variable.getInstance(context).GetAotuStartState()) {
			try {
				Thread.sleep(2000L);
				Intent ootStartIntent = new Intent(context, StartServicesActivity.class);
				ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(ootStartIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
		}
	}
}
