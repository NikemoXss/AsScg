package com.ys.www.asscg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ys.www.asscg.R;
import com.ys.www.asscg.activity.AboutUsActivity;
import com.ys.www.asscg.activity.ContactUsActivity;
import com.ys.www.asscg.activity.FeedbackActivity;
import com.ys.www.asscg.activity.LoginActivity;
import com.ys.www.asscg.base.BaseFragment;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

//苏常设置
public class SetFragment_Scg extends BaseFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_set, null);
        mainView.findViewById(R.id.item_feedback).setOnClickListener(this);
        mainView.findViewById(R.id.item_check_update).setOnClickListener(this);
        mainView.findViewById(R.id.item_shareapp).setOnClickListener(this);
        mainView.findViewById(R.id.item_contactus).setOnClickListener(this);
        mainView.findViewById(R.id.item_aboutus).setOnClickListener(this);
        return mainView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    public void stop() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_feedback:
                // 检测用户是否登录
                if (Default.userId != 0) {
                    getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.item_check_update:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        checkNewVersion();
                    }
                }.start();
                break;
            case R.id.item_shareapp:
                //SystenmApi.showShareView(getActivity(), "苏常网APP，随时随地掌握你的财富", "手机移动理财的指尖神器，帮您在“拇指时代”指点钱途，“掌握财富”。",
//                "http://www.czsuchang.com/Member/Common/AppRegister?invite=" + Default.userId);//http://www.czsuchang.com/Member/Common/AppRegister?invite=881
                break;
            case R.id.item_contactus:
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.item_aboutus:
                Intent intent1 = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent1);
                break;

            default:
                break;
        }
    }

    // 提交用户反馈到服务器
    public void checkNewVersion() {
        String url = Default.ip + Default.version;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("version", Default.version);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = response;
                handler.sendMessage(msg);
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject response = (JSONObject) msg.obj;
                    try {
                        // 没有新版本
                        if (response.getInt("status") == 1) {
                            showCustomToast(response.getString("message"));
                            // 获取新版本
                        } else if (response.getInt("status") == 0) {
                            // 另起后台线程 下载新版APP

                        } else {
                            showCustomToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
