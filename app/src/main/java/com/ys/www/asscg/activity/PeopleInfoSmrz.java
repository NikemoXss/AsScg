package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;


/**
 * 实名验证显示审核信息
 */
public class PeopleInfoSmrz extends BaseActivity implements OnClickListener {
	private TextView info[];

	private int real_status;
	private String real_name;
	private String real_id_num;
	private String real_id_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_shimingyz);
		// 获取Intent传递过来的参数
		Intent intent = getIntent();
		if (intent != null) {
			real_status = intent.getIntExtra("real_status", 0);
			if (real_status == 1) {
				real_name = intent.getStringExtra("realName");
				real_id_num = intent.getStringExtra("realId");
				real_id_status = "已验证";
			} else if (real_status == 0) {
				real_name = real_id_num = real_id_status = "未验证";
			} else {
				real_name = real_id_num = real_id_status = "审核中";
			}
		}

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.shimingyanzheng);

		findViewById(R.id.rl_realname).setOnClickListener(this);
		findViewById(R.id.rl_idcard).setOnClickListener(this);
		findViewById(R.id.rl_zjsh).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);

		info = new TextView[3];
		info[0] = (TextView) findViewById(R.id.tv_shsb);
		info[1] = (TextView) findViewById(R.id.tv_idcard);
		info[2] = (TextView) findViewById(R.id.tv_zjsh);

		info[0].setText(real_name);
		info[1].setText(real_id_num);
		info[2].setText(real_id_status);

		TextView text = (TextView) findViewById(R.id.textView2);
		// text.setText(real_status == 1?"您已通过实名验证":"您的实名验证未通过，请重新填写");
		if (real_status == 1) {
			text.setText("您已通过实名验证");

		} else if (real_status == 0) {
			text.setText("您的实名验证未通过，请重新填写");

		} else {
			text.setText("您的实名验证正在审核");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_realname:
		case R.id.rl_idcard:
		case R.id.rl_zjsh:
			if (real_status == 0) {
				Intent intent3 = new Intent(PeopleInfoSmrz.this, PeopleInfoIdcard.class);
				startActivity(intent3);
				finish();
			} else if (real_status == 3) {
				showCustomToast("您的实名认证正在审核中！");
			} else {
				showCustomToast("您已通过实名认证！");
			}
			break;
		case R.id.title_right:
			finish();
			break;
		}

	}

}
