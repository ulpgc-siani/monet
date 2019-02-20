package org.monet.space.mobile.syncservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

	private static final Object LOCK = new Object();
	private static SyncAdapter adapter = null;

	@Override
	public void onCreate() {
		synchronized (LOCK) {
			if (adapter == null) {
				adapter = new SyncAdapter(getApplicationContext(), true);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return adapter.getSyncAdapterBinder();
	}
}
