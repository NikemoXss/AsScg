package com.ys.www.asscg.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseFragment;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;
import com.ys.www.asscg.view.ChuckWaveView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//苏常账户
@SuppressLint("NewApi")
public class AccountFragment_Scg extends BaseFragment implements OnClickListener {

    private ChuckWaveView mWaveView = null;

    private float mCurrentHeight = 0;

    TextView total_rs, freeze_rs, user_name_rs;
    ImageView user_pic;
    int jxj_num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_account, null);
        mainView.findViewById(R.id.item_personalset).setOnClickListener(this);
        mainView.findViewById(R.id.item_tzrecord).setOnClickListener(this);
        mainView.findViewById(R.id.item_jyjl).setOnClickListener(this);
        mainView.findViewById(R.id.cz_rs).setOnClickListener(this);
        mainView.findViewById(R.id.tx_rs).setOnClickListener(this);
        mainView.findViewById(R.id.item_jxj).setOnClickListener(this);
        mainView.findViewById(R.id.item_redpkg).setOnClickListener(this);
        total_rs = (TextView) mainView.findViewById(R.id.total_rs);
        freeze_rs = (TextView) mainView.findViewById(R.id.freeze_rs);
        user_pic = (ImageView) mainView.findViewById(R.id.user_pic);
        user_name_rs = (TextView) mainView.findViewById(R.id.user_name_rs);
        mWaveView = (ChuckWaveView) mainView.findViewById(R.id.main_wave_v1);
        initview();
        return mainView;
    }

    private void initview() {
        // TODO Auto-generated method stub

        loadWaveData(0.30f);
    }

    private void loadWaveData(float height) {
        float a = 0.33f;
        height = a;
        // Toast.makeText(MainActivity.this, height + "", 1000).show();
        final List<Animator> mAnimators = new ArrayList<Animator>();
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(mWaveView, "waveShiftRatio", 0f, 1f); // 水平方向循环
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(2000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        mAnimators.add(waveShiftAnim);

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(mWaveView, "waterLevelRatio", mCurrentHeight, height); // 竖直方向从0%到x%
        waterLevelAnim.setDuration(6000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        mAnimators.add(waterLevelAnim);

        mWaveView.invalidate();
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mAnimators);
        mAnimatorSet.start();

        mCurrentHeight = height;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                super.run();
                doHttpUpdatePeopleInfo();
            }
        }.start();


    }

    public void doHttpUpdatePeopleInfo() {
        String url = Default.ip + Default.peoInfoUpdate;

        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);

                if (response.getInt("status") == 1) {
                    // updateUserInfo(response);
                    total_rs.setText(response.getString("total") + "元");
                    // 冻结
                    freeze_rs.setText(response.getString("freeze") + "元");
                    Picasso.with(getActivity()).load(Default.ip + response.getString("head")).into(user_pic);
                    user_name_rs.setText(response.getString("username"));
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

    ;

    public JSONObject str2json(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_personalset:// 用户设置
//			Intent intent = new Intent(getActivity(), peopleInfoDataActivity.class);
//			startActivity(intent);
                break;
            case R.id.item_tzrecord:// 投资记录
//			Intent intent1 = new Intent(getActivity(), InvestManagerStandardActivity.class);
//			startActivity(intent1);
                break;
            case R.id.item_jyjl:// 交易记录
//			startActivity(new Intent(getActivity(), PeopleInfoJiaoYi_New.class));
                break;
            case R.id.cz_rs:// 充值
//			Intent i = new Intent(getActivity(), ChoosePayType.class);
//			startActivity(i);
                break;
            case R.id.tx_rs:// 提现
                getUserBankCard();
                break;
            case R.id.item_jxj:// 加息劵
//			Intent i1 = new Intent(getActivity(), InterestratesecuritiesActivity.class);
//			startActivity(i1);
                break;
            case R.id.item_redpkg://红包
//			Intent i2 = new Intent(getActivity(), MyBriberyMoney.class);
//			startActivity(i2);
                break;
            default:
                break;

        }
    }

    public void getUserBankCard() {
    }

    ;

    // 解析获取到银行卡信息
//	public void decoidTheUesrCardInfo(JSONObject json) {
//		try {
//			if (json.getString("bank_num") != null && !json.getString("bank_num").equals("")) {
//
//				Intent intent = new Intent(getActivity(), peopleInfoWithdrawalActivity.class);
//
//				intent.putExtra("bank_num", json.getString("bank_num"));
//
//				if (json.getString("bank_name") != null && !json.getString("bank_name").equals("")) {
//
//					intent.putExtra("bank_name", json.getString("bank_name"));
//
//				}
//				if (json.getString("real_name") != null && !json.getString("real_name").equals("")) {
//
//					intent.putExtra("real_name", json.getString("real_name"));
//				}
//				// if (json.getString("all_money") != null
//				// && !json.getString("all_money").equals("")) {
//				intent.putExtra("all_money", json.getString("all_money"));
//				MyLog.e("提现准备传的钱数", json.getString("all_money"));
//				// }
//				if (json.getString("qixian") != null && !json.getString("qixian").equals("")) {
//
//					intent.putExtra("qixian", json.getString("qixian"));
//				}
//
//				startActivity(intent);
//
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
