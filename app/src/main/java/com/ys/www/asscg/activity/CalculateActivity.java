package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.api.SystenmApi;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.base.MyLog;


/**
 * 计算器界面
 *
 * @author zzx
 */
public class CalculateActivity extends BaseActivity implements OnClickListener {
	private Spinner spinner;
	private ArrayAdapter adapter;

	private String str_zonge;

	private EditText mEditText[];
	private RadioGroup mRadio_qixian, mRadio_jiangli;
	private TextView mText_ntbjl, mText_jl;
	private RelativeLayout layout;
	private TextView mText_info[];

	private float bili, guanli, qixian;
	private ImageView title_right;
	private int
	// 1日利率 0年利率
	type_lilv = 0,
			// 0比例 1固定
			type_jiangli,
			// 0月1日
			type_qixian,
			// 0 按天到期还款 1每月还息到期还本，2按月分期还款 3一次性还款
			type_fangshi;
	private double
	// 投资总额
	amount,
			// 利率
			rate,
			// 投资
			invest_money,
			// 奖金
			reward_money,
			// 偿还金
			repayment_money,
			// 利息
			interest, month_apr, day_apr, year_apr, yuelilv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate);

		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.js);

		spinner = (Spinner) findViewById(R.id.spinner);
		// 将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this, R.array.js_16, android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				type_fangshi = position;
				getInfo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

		mEditText = new EditText[6];
		mEditText[0] = (EditText) findViewById(R.id.edit1);
		mEditText[1] = (EditText) findViewById(R.id.edit2);
		mEditText[2] = (EditText) findViewById(R.id.edit3);
		mEditText[3] = (EditText) findViewById(R.id.edit4);
		mEditText[4] = (EditText) findViewById(R.id.edit5);
		mEditText[5] = (EditText) findViewById(R.id.edit6);

		mRadio_jiangli = (RadioGroup) findViewById(R.id.group_2);
		mRadio_qixian = (RadioGroup) findViewById(R.id.group_3);

		findViewById(R.id.jisuan).setOnClickListener(this);

		findViewById(R.id.group_2_1).setOnClickListener(this);
		findViewById(R.id.group_2_2).setOnClickListener(this);

		findViewById(R.id.group_3_1).setOnClickListener(this);
		findViewById(R.id.group_3_2).setOnClickListener(this);
		findViewById(R.id.title_right).setVisibility(View.VISIBLE);
		findViewById(R.id.title_right).setOnClickListener(this);

		mText_ntbjl = (TextView) findViewById(R.id.ntbjl);
		mText_jl = (TextView) findViewById(R.id.jl);
		layout = (RelativeLayout) findViewById(R.id.layout);

		mText_info = new TextView[5];
		mText_info[0] = (TextView) findViewById(R.id.text1);
		mText_info[1] = (TextView) findViewById(R.id.text2);
		mText_info[2] = (TextView) findViewById(R.id.text3);
		mText_info[3] = (TextView) findViewById(R.id.text4);
		mText_info[4] = (TextView) findViewById(R.id.text5);

		getIntentInfo();
	}

	public void getIntentInfo() {
		Intent intent = getIntent();
		String str_lilv = intent.getExtras().getDouble("lilv") + "";
		String str_qixian = intent.getExtras().getString("qixian");
		String str_fangshi = intent.getExtras().getString("fangshi");
		String str_jiangli = intent.getExtras().getString("jiangli");
		String str_guanli = intent.getExtras().getString("guanli");
		str_zonge = intent.getExtras().getLong("zonge") + "";

		mEditText[0].setText(str_zonge);// "1000"
		mEditText[1].setText(str_lilv);
		if (str_jiangli.endsWith("%")) {
			mEditText[2].setText(str_jiangli.substring(0, str_jiangli.lastIndexOf("%")));
		} else if (str_jiangli.equals("0")) {
			mEditText[2].setText(str_jiangli);
		} else {
			type_jiangli = 1;
			layout.setVisibility(View.VISIBLE);
			mEditText[2].setText(str_zonge);
			mEditText[5].setText(str_jiangli);
			mText_jl.setText(R.string.js_21);
			mRadio_jiangli.check(R.id.group_2_2);
		}

		mEditText[3].setText(str_guanli);
		if (str_qixian.endsWith("天")) {
			mRadio_qixian.check(R.id.group_3_2);
			mEditText[4].setText(str_qixian.substring(0, str_qixian.lastIndexOf("天")));
			// type_lilv = 1;
			type_qixian = 1;
		} else if (str_qixian.endsWith("个月")) {
			mRadio_qixian.check(R.id.group_3_1);
			mEditText[4].setText(str_qixian.substring(0, str_qixian.lastIndexOf("个")));
		}

		if (str_fangshi.equals("按天到期还款")) {
			spinner.setSelection(0);
		} else if (str_fangshi.equals("每月还息到期还本")) {
			spinner.setSelection(1);
		} else if (str_fangshi.equals("按月分期还款")) {
			spinner.setSelection(2);
		} else if (str_fangshi.equals("一次性还款") || str_fangshi.equals("按季分期还款")) {
			spinner.setSelection(3);
		}

		// mHandler.sendEmptyMessage(0);
		getInfo();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		case R.id.jisuan:
			getInfo();
			break;
		case R.id.group_2_1:
			type_jiangli = 0;
			mText_ntbjl.setText(R.string.js_3);
			mText_jl.setText(R.string.js_26);
			layout.setVisibility(View.GONE);
			break;
		case R.id.group_2_2:
			type_jiangli = 1;
			mText_ntbjl.setText(R.string.js_25);
			mText_jl.setText(R.string.js_21);
			layout.setVisibility(View.VISIBLE);
			mEditText[2].setText(str_zonge);
			break;
		case R.id.group_3_1:
			changeInfo(0);
			break;
		case R.id.group_3_2:
			changeInfo(1);
			break;
		}
	};

	public void changeInfo(int type) {
		switch (type) {
		case 0:
			type_qixian = 0;

			// type_lilv = 0;
			spinner.setEnabled(true);
			mRadio_qixian.check(R.id.group_3_1);

			break;
		case 1:
			type_qixian = 1;

			// type_lilv = 1;
			spinner.setEnabled(false);
			spinner.setSelection(0);
			mRadio_qixian.check(R.id.group_3_2);

			break;
		}
	}

	public void getInfo() {
		// 投标金额
		amount = Double.parseDouble(mEditText[0].getText().toString());
		// 利率
		rate = Double.parseDouble(mEditText[1].getText().toString());
		bili = Float.parseFloat(mEditText[2].getText().toString());
		guanli = Float.parseFloat(mEditText[3].getText().toString());
		qixian = Float.parseFloat(mEditText[4].getText().toString());

		type_fangshi = spinner.getSelectedItemPosition();

		// lilv 1日利率 0年利率
		// type_qixian 0日 1月
		// 年利率
		if (type_fangshi != 0 && type_lilv == 1)// 不是日还款，期限是日利率
		{
			rate = rate * 365;
		}
		if (type_fangshi == 0 && type_lilv == 0)// 是日还款，期限是年利率
		{
			rate = rate / 365;
		}
		// 奖励
		if (type_jiangli == 1) {
			Double num1 = Double.parseDouble(mEditText[5].getText().toString());
			Double num2 = Double.parseDouble(mEditText[2].getText().toString());
			bili = (float) (num1 * 100 / num2);
		}
		// 期限
		if (type_lilv == 1 && type_qixian == 0) {
			qixian = qixian * 365 / 12;
		}

		reward_money = amount * bili / 100;
		invest_money = (long) (amount - reward_money);

		if (type_fangshi == 0)// 按天还款
		{
			repayment_money = amount * (rate * qixian * (100 - guanli) / 100 + 100) / 100;
			interest = repayment_money - amount;
			day_apr = (repayment_money - invest_money) * 100 / (invest_money * qixian);
			month_apr = day_apr * 365 / 12;
			year_apr = day_apr * 365;

		} else if (type_fangshi == 1)// 月还利息 到期还本
		{

			repayment_money = amount * (rate * qixian * (100 - guanli) / 100 / 12 + 100) / 100;
			interest = repayment_money - amount;
			month_apr = (repayment_money - invest_money) * 100 / (invest_money * qixian);
			day_apr = month_apr * 12 / 365;
			year_apr = month_apr * 12;

		} else if (type_fangshi == 2222)// 先息后本
		{

			interest = amount * (rate / 12 / 100) * qixian * ((100 - guanli) / 100);
			invest_money -= interest;
			repayment_money = amount;

			month_apr = (repayment_money - invest_money) * 100 / (invest_money * qixian);
			day_apr = month_apr * 12 / 365;
			year_apr = month_apr * 12;
		} else if (type_fangshi == 3)// 到期还款
		{

			repayment_money = amount + amount * (qixian * rate / 12 / 100) * (100 - guanli) / 100;
			interest = repayment_money - amount;
			// repayment_money = amount;

			month_apr = (repayment_money - invest_money) * 100 / (invest_money * qixian);
			day_apr = month_apr * 12 / 365;
			year_apr = month_apr * 12;
		} else if (type_fangshi == 2)// 按月还款
		{
			double month_apr1 = rate / (12 * 100);
			double li = Math.pow(1 + month_apr1, qixian);

			double repayment = li == 1 ? amount / qixian : amount * (month_apr1 * li) / (li - 1);

			repayment_money = (repayment * qixian - amount) * (100 - guanli) / 100 + amount;
			interest = repayment_money - amount;

			double month_apr2 = (repayment_money - invest_money) / (invest_money * qixian);
			double rek = 0.001;
			for (int i = 0; i < 100; i++) {
				double li2 = Math.pow(1 + month_apr2, qixian);
				double repay = invest_money * qixian * (month_apr2 * li2) / (li2 - 1);
				if (repay < repayment_money * 0.99999) {
					month_apr2 += rek;
				} else if (repay > repayment_money * 1.00001) {
					month_apr2 -= rek * 0.9;
					rek *= 0.1;
				}
			}

			month_apr = month_apr2 * 100;
			day_apr = month_apr * 12 / 365;
			year_apr = month_apr * 12;
		}

		MyLog.d("zzx",
				"偿还 " + repayment_money + "利息 " + interest + "年利率 " + year_apr + "月利率 " + month_apr + "日利率" + day_apr);
		mText_info[0].setText(SystenmApi.getRounding(year_apr) + "%");
		mText_info[1].setText(SystenmApi.getRounding(month_apr) + "%");

		mText_info[2].setText(SystenmApi.getRounding(interest + reward_money) + "");
		mText_info[3].setText(SystenmApi.getRounding(interest) + "");
		mText_info[4].setText(SystenmApi.getRounding(reward_money) + "");

	}

}
