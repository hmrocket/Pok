package com.hmrocket.magicpoker;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @since 05/Jan/2016 - mhamed
 */
public class MyApp extends Application {

	/**
	 * Verify if this application is running on debug mode or not
	 *
	 * @param context the global context of the app
	 * @return true if FLAG_DEBUGGABLE is set, false otherwise
	 */
	public static boolean isDebugMode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		AnalyticsTrackers.initialize(this);
		// Turn on GA during App startup
		AnalyticsTrackers.getAppTracker();
	}
}