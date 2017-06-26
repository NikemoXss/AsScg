package com.ys.www.asscg.activity;

import android.content.Intent;
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


public class ForgotPwdActivity extends BaseActivity implements OnClickListener {

	/**
	 * 用户手机号码
	 */
	private EditText phoneEditor;
	private EditText usernameEditor, user_phone_sx, ver_pwd_sx;

	/**
	 * 用户手机号码
	 */
	private String phoneNum;
	private String username;
	TextView title;
	ImageView iv;
	String uid;
	String iszfpwd;
	public static ForgotPwdActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pwd);
		instance = this;
		title = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		iszfpwd = intent.getStringExtra("iszfpwd");
//		showCustomToast(iszfpwd);
		title.setText("忘记登录密码");
		if(iszfpwd.equals("0")){
			title.setText("忘记支付密码");
		}
		// findViewById(R.id.forget_pwd).setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		// phoneEditor = (EditText) findViewById(R.id.user_phone);
		// usernameEditor = (EditText) findViewById(R.id.user_name);
		findViewById(R.id.edittext_sx).setOnClickListener(this);
		user_phone_sx = (EditText) findViewById(R.id.user_phone_sx);
		findViewById(R.id.forget_pwd_sx).setOnClickListener(this);
		ver_pwd_sx = (EditText) findViewById(R.id.ver_pwd_sx);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		case R.id.forget_pwd_sx:// 校验验证码
			if (user_phone_sx.equals("")) {
				showCustomToast("请输入您的手机号！");
				return;
			}
			if (ver_pwd_sx.equals("")) {
				showCustomToast("请输入验证码！");
				return;
			}
			doHttpyzmyz();
//			Intent intent = new Intent(ForgotPwdActivity.this, AddNewPwd.class);
//			intent.putExtra("uid", 349);
//			startActivity(intent);
			break;
		case R.id.edittext_sx:// 获取验证码
			doHttpyzm();
			break;
		default:
			break;
		}

	}

	private void doHttpyzm() {
		{
			String url = Default.ip + Default.getyzm;
			try {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("uid", 0);
				jsonObject.put("phone", user_phone_sx.getText().toString());
				String s = HttpClient.post(url, jsonObject.toString());
				if (!"".equals(s)) {
					JSONObject response = str2json(s);
					if (response.getInt("status") == 1) {
						uid = response.getString("uid");
						showCustomToast("验证码发送成功");
					} else {
						showCustomToast("验证码发送失败");
					}
				} else {
					showCustomToast("获取失败");
				}
				Log.e("HttpClient", s);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	private void doHttpyzmyz() {
			String url = Default.ip + Default.yzmyzsx;
			try {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("uid", 0);
				jsonObject.put("code", ver_pwd_sx.getText().toString());
				String s = HttpClient.post(url, jsonObject.toString());
				if (!"".equals(s)) {
					JSONObject response = str2json(s);
					if (response.getInt("status") == 1) {
						Intent intent = new Intent(ForgotPwdActivity.this, AddNewPwd.class);
						intent.putExtra("uid", uid);
						intent.putExtra("iszf", iszfpwd);
						startActivity(intent);
					} else {
						showCustomToast("验证码发送失败");
					}
				} else {
					showCustomToast("验证码验证错误");
				}
				Log.e("HttpClient", s);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
