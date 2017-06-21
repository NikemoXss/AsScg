package com.ys.www.asscg.base;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
	public static List<BaseActivity> liveActivityList = new ArrayList<BaseActivity>();
	public static List<BaseActivity> visibleActivityList = new ArrayList<BaseActivity>();
	public static List<BaseActivity> foregroundActivityList = new ArrayList<BaseActivity>();

	public static void addLiveActivity(BaseActivity activity) {
		liveActivityList.add(activity);
		visibleActivityList.add(activity);
		foregroundActivityList.add(activity);
	}

	public static void delLiveActivity(BaseActivity activity) {
		liveActivityList.remove(activity);
		visibleActivityList.remove(activity);
		foregroundActivityList.remove(activity);
	}

	public static void addVisibleActivity(BaseActivity activity) {
		if (!visibleActivityList.contains(activity)) {
			visibleActivityList.add(activity);
		}
	}

	public static void delVisibleActivity(BaseActivity activity) {
		if (visibleActivityList.contains(activity)) {
			visibleActivityList.remove(activity);
		}
	}

	public static void addForegroundActivity(BaseActivity activity) {
		if (!foregroundActivityList.contains(activity)) {
			foregroundActivityList.add(activity);
		}
	}

	public static void delForegroundActivity(BaseActivity activity) {
		if (foregroundActivityList.contains(activity)) {
			foregroundActivityList.remove(activity);
		}
	}

	public static void closeAllActivity() {
		for (int i = 0; i < liveActivityList.size(); i++) {
			if (liveActivityList.get(i) != null) {
				liveActivityList.get(i).finish();
			}
		}
		System.exit(0);// Android的程序只是让Activity finish()掉,而单纯的finish掉,退出并不完全
	}

	public static BaseActivity getCurrentActivit() {
		if (visibleActivityList.size() == 0) {
			return null;
		} else {
			return visibleActivityList.get(0);
		}
	}

	public static void prientActivit() {
		for (int i = 0; i < liveActivityList.size(); i++) {
			MyLog.d("activity", i + "activit live=" + liveActivityList.get(i).getClass().toString());
		}
		for (int i = 0; i < visibleActivityList.size(); i++) {
			MyLog.d("activity", i + "activit visible=" + visibleActivityList.get(i).getClass().toString());
		}
		for (int i = 0; i < foregroundActivityList.size(); i++) {
			MyLog.d("activity", i + "activit foreground=" + foregroundActivityList.get(i).getClass().toString());
		}
	}
}
