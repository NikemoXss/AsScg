package com.ys.www.asscg.base;

import java.util.List;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.www.asscg.R;
import com.ys.www.asscg.dialog.LoadingDialog;
import com.ys.www.asscg.util.Default;

import static android.view.Gravity.*;

public class BaseFragment extends Fragment {

	LoadingDialog loadingDialog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		mLoadingDialog = new ProgressDialog(getActivity(), "请求提交中");
		loadingDialog = new LoadingDialog(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//

		if (!Default.isActive) {
			// app 从后台唤醒，进入前台
			if (!Default.isgestures) {

//				showThePasswordView();
			} else {
				Default.isgestures = false;
			}
			Default.isActive = true;
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!isAppOnForeground()) {
			// app 进入后台
			// showThePasswordView();
			// 全局变量isActive = false 记录当前已经进入后台
			Default.isActive = false;

		}
	}

//	public void showThePasswordView() {
//
//		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lmq", 0);
//
//		if (sharedPreferences.getBoolean("sl", false)) {
//
//			Intent intent = new Intent(getActivity().getApplicationContext(), UnlockGesturePasswordActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			getActivity().getApplicationContext().startActivity(intent);
//
//		}
//
//	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		android.app.ActivityManager activityManager = (android.app.ActivityManager) getActivity()
				.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getActivity().getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	// -------------------------------------------------------

	// --------------------------------------------------------
	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	protected void showShortToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	protected void showLongToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}

	/** 显示自定义Toast提示(来自res) **/
	// protected void showCustomToast(int resId) {
	// View toastRoot = LayoutInflater.from(BaseActivity.getActivity()).inflate(
	// R.layout.common_toast, null);
	// ((HandyTextView) toastRoot.findViewById(R.id.toast_text))
	// .setText(getString(resId));
	// Toast toast = new Toast(BaseActivity.getActivity());
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// toast.setDuration(Toast.LENGTH_SHORT);
	// toast.setView(toastRoot);
	// toast.show();
	// }

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(getActivity()).inflate(R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(getActivity());
		toast.setGravity(CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	protected void showCustomToast(int id) {

		if (Default.hdf_show_error_info) {
			showCustomToast(getResources().getString(id).toString());
		}
	}

	// ---------------------------------------------------------------
//	protected void showLoadingDialog(String text) {
//		if (text != null) {
//			mLoadingDialog.setText(text);
//			// mLoadingDialog.setCancelable(false);
//		}
//		mLoadingDialog.show();
//	}

//	protected void showLoadingDialogNoCancle(String text) {
//		if (mLoadingDialog == null) {
//			return;
//		}
//		if (text != null) {
//			mLoadingDialog.setText(text);
//			// mLoadingDialog.setCancelable(false);
//		}
//
//		mLoadingDialog.show();
//	}
//
//	protected void dismissLoadingDialog() {
//		if (mLoadingDialog != null) {
//			if (mLoadingDialog.isShowing()) {
//				mLoadingDialog.dismiss();
//			}
//		}
//	}

	protected void showLoadingDialogNoCancle(String text) {
		if (loadingDialog == null) {
			return;
		}
		loadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (loadingDialog != null) {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

}
