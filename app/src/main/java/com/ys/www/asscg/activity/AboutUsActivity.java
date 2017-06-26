package com.ys.www.asscg.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;

public class AboutUsActivity extends BaseActivity implements OnClickListener {
	ImageView title_right;
	TextView title,verson_sx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		title_right = (ImageView) findViewById(R.id.title_right);
		title = (TextView) findViewById(R.id.title);
		title_right.setVisibility(View.VISIBLE);
		title.setVisibility(View.VISIBLE);
		title.setText("关于苏常");
		title_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		default:
			break;
		}
	}

}
