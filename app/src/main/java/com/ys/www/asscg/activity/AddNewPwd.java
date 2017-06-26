package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;


public class AddNewPwd extends BaseActivity implements OnClickListener {
	Button conmit_bt;
	EditText newpwd_et;
	TextView title;
	ImageView back_iv;
	String uid, iszf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_addnewpwd);
		Intent intent = getIntent();
		conmit_bt = (Button) findViewById(R.id.subpwd_sx);
		newpwd_et = (EditText) findViewById(R.id.newpwd_sx);
		title = (TextView) findViewById(R.id.title);
		iszf = intent.getStringExtra("iszf");
		title.setText("添加新登录密码");
		if (iszf.equals("0")) {
			title.setText("添加新支付密码");
		}
		back_iv = (ImageView) findViewById(R.id.title_right);
		back_iv.setVisibility(View.VISIBLE);
		conmit_bt.setOnClickListener(this);
		back_iv.setOnClickListener(this);

		uid = intent.getStringExtra("uid");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.subpwd_sx:
			if (newpwd_et.getText().toString() == null || newpwd_et.getText().toString().equals("")) {
				showCustomToast("请输入新的密码");
			} else {
				new Thread(){
					@Override
					public void run() {
						super.run();
						doHttaddnewpwd();
					}
				}.start();

			}
			break;

		case R.id.title_right:
			finish();
			break;

		default:
			break;
		}
	}

	private void doHttaddnewpwd() {
		String url = Default.ip + Default.addloginnewpwdsx;
		try {
			JSONObject jsonObject=new JSONObject();
			if (iszf.equals("0")) {
				url = Default.addzhifunewpwdsx;
				jsonObject.put("uid", Default.userId);
			} else {
				jsonObject.put("uid", uid);
			}
			jsonObject.put("password", newpwd_et.getText().toString());
			String s = HttpClient.post(url, jsonObject.toString());
			if (!"".equals(s)) {
				JSONObject response = str2json(s);
				if (response.getInt("status") == 1) {
					showCustomToast(response.getString("message"));
					// showCustomToast("修改成功,请重新登录");
					ForgotPwdActivity.instance.finish();
					finish();
				} else {
					showCustomToast("修改失败");
				}
			} else {
				showCustomToast("验证码验证错误");
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
