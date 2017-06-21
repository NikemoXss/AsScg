package com.ys.www.asscg.dialog;





import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ys.www.asscg.R;

/**
 * @author zzx
 */

public class LoadingDialog {

	public Dialog mDialog;
//	private AnimationDrawable animationDrawable = null;
//	private TextView text;

	public LoadingDialog(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.loading_view, null);

//		text = (TextView) view.findViewById(R.id.progress_message);
//		text.setText(message);
//		ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
//		loadingImage.setImageResource(R.anim.loading_animation);
//		animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
//		animationDrawable.setOneShot(false);
//		animationDrawable.start();
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
	}

//	public void setText(String message) {
//		if (text != null) {
//			text.setText(message);
//		}
//	}

	public void show() {
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
//			animationDrawable.stop();
		}
	}

	public boolean isShowing() {
		return mDialog.isShowing();
	}

}
