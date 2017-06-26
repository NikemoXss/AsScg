package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * 修改交易密码
 */
public class PasswordJaoYiActivity extends BaseActivity implements OnClickListener {
	private EditText mOldd, mPassww, mPassww2;
	private String info[];
	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_update_jiaoyipsw);
		initView();
	}

	public void initView() {
		iv_back = (ImageView) findViewById(R.id.title_right);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		// findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_jiayoyi).setOnClickListener(this);
		findViewById(R.id.getPw).setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.updatezfpsd_title);

		mOldd = (EditText) findViewById(R.id.ed_itold);
		mPassww = (EditText) findViewById(R.id.ed_itpassw);
		mPassww2 = (EditText) findViewById(R.id.ed_itpassw2);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_jiayoyi:
			info = getInfo();
			if (info == null) {
				return;
			}
			doHttp();
			break;
		case R.id.title_right:
			finish();
			break;
		case R.id.getPw:
			Intent intent = new Intent(PasswordJaoYiActivity.this, ForgotPwdActivity.class);
			intent.putExtra("iszfpwd", "0");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	public String[] getInfo() {
		String old = mOldd.getText().toString();
		String passw = mPassww.getText().toString();
		String passw2 = mPassww2.getText().toString();

		if (old.equals("")) {
			showCustomToast(R.string.toast7);
			return null;
		}
		if (passw.equals("")) {
			showCustomToast(R.string.toast8);
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

	public void doHttp() {
		String url = Default.ip + Default.peoInfoxsjiaoyipsw;
		try {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("uid", 0);
			jsonObject.put("oldpwd", info[0]);
			jsonObject.put("newpwd", info[1]);
			String s = HttpClient.post(url, jsonObject.toString());
			if (!"".equals(s)) {
				JSONObject response = str2json(s);
				if (response.getInt("status") == 1) {
					finish();
					showCustomToast("修改支付密码成功！");
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

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			showCustomToast(msg.getData().getString("info"));
		}

	};

}
