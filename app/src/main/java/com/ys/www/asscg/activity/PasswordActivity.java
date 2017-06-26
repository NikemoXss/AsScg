package com.ys.www.asscg.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 修改登录密码
 *
 * @author 孙建超
 */
public class PasswordActivity extends BaseActivity implements OnClickListener {
	private EditText mOld, mPassw, mPassw2;
	private String info[];
	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_update_loginpsw);

		initView();
	}

	public void initView() {
		findViewById(R.id.enter).setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.title_right);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.peo_password_1);

		mOld = (EditText) findViewById(R.id.editold);
		mPassw = (EditText) findViewById(R.id.editpassw);
		mPassw2 = (EditText) findViewById(R.id.editpassw2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter:
			info = getInfo();
			if (info == null) {
				return;
			}
			new Thread(){
                @Override
                public void run() {
                    super.run();
                    doHttp();
                }
            }.start();
			break;
		case R.id.title_right:
			finish();
			break;
		}

	}

	public String[] getInfo() {
		String old = mOld.getText().toString();
		String passw = mPassw.getText().toString();
		String passw2 = mPassw2.getText().toString();

		if (old.equals("")) {
			showCustomToast(R.string.toast7);
			return null;
		}
		if (passw.equals("")) {
			showCustomToast(R.string.toast8);
			return null;
		}
		if (passw.length() < 4) {
			showCustomToast(R.string.toast13);

			return null;
		}
		if (passw.length() < 6) {
			showCustomToast(R.string.toast8_2);
			return null;
		}

		if (passw.length() > 16) {
			showCustomToast(R.string.toast8_3);
			return null;
		}
		if (passw2.equals("")) {
			showCustomToast(R.string.toast8_1);
			return null;
		}

		if (passw2.equals(passw) == false) {
			showCustomToast(R.string.toast9);
			return null;
		}

		return new String[] { old, passw, passw2 };
	}

	// public void doHttp()
	// {
	// showLoadingDialogNoCancle(getResources().getString(
	// R.string.toast2));
	//
	// JsonBuilder builder = new JsonBuilder();
	// builder.put("oldpwd", info[0]);
	// builder.put("newpwd", info[1]);
	//
	// new BaseModel(null, Default.changepass, builder)
	// .setConnectionResponseLinstener(new ConnectResponseListener()
	// {
	// public void onConnectResponseCallback(JSONObject json)
	// {
	// dismissLoadingDialog();
	// finish();
	// }
	//
	// @Override
	// public void onFail(JSONObject json)
	// {
	// dismissLoadingDialog();
	// try
	// {
	// Message msg = new Message();
	// Bundle bundel = new Bundle();
	// bundel.putString("info", json.getString("message"));
	// msg.setData(bundel);
	// handler.sendMessage(msg);
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	public void doHttp(){
		String url = Default.ip + Default.changepass;
		try {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("uid",0);
			jsonObject.put("oldpwd", info[0]);
			jsonObject.put("newpwd", info[1]);
			String s = HttpClient.post(url, null);
			if (!"".equals(s)) {
				JSONObject response = str2json(s);
				if (response.getInt("status") == 1) {
					finish();
					showCustomToast("修改登陆密码成功！");
				} else {
					showCustomToast(response.getString("message"));
				}
			} else {
				showCustomToast("获取失败");
			}
			Log.e("HttpClient", s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject str2json(String s) {
		JSONObject json = null;
		try {
			json = new JSONObject(s);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
