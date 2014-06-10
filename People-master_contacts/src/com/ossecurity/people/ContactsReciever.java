package com.ossecurity.people;

import java.io.IOException;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ContactsReciever extends BroadcastReceiver{
	private static final String TAG = "ContactsReceiver";
	private static final String FILE_EXISTS = "File Exists";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle results = getResultExtras(true);
		
		try {
			DataExporter.exportData(context);
			results.putBoolean(FILE_EXISTS, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "onReceive failed.", e);
			e.printStackTrace();
		}
	}
}
