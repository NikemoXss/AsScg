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
import com.ys.www.asscg.base.MyLog;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class FeedbackActivity extends BaseActivity implements OnClickListener {

    private EditText feedBackEditText = null;
    private String mMessage;
    TextView title;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        findViewById(R.id.title_right).setOnClickListener(this);
        feedBackEditText = (EditText) findViewById(R.id.feedback_content);
        findViewById(R.id.submit_btn).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("意见反馈");
        iv = (ImageView) findViewById(R.id.title_right);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                finish();
                break;

            case R.id.submit_btn:
                String feedBackStr = feedBackEditText.getText().toString();
                MyLog.e("意见反馈信息--->", feedBackStr);
                if (null == feedBackStr) {
                    showCustomToast(R.string.feedback_tip_null);
                } else if (feedBackStr.length() == 0 || feedBackStr.equals("")) {
                    showCustomToast(R.string.feedback_tip_null);
                } else {
                    doHttp();
                }
                break;
            default:
                break;
        }
    }

    // 提交用户反馈到服务器  	jsonBuilder.put("message", feedBackEditText.getText().toString());
    public void doHttp() {
        String url = Default.ip + Default.FEEDBACK;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("message", feedBackEditText.getText().toString());
            // 获取当前手机系统信息
            jsonObject.put("system", Default.PHONE_MODEL + Default.OS_VERSION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject json = str2json(s);
                try {
                    if (json.getInt("status") == 1) {
                        showCustomToast(json.getString("message"));
                    } else {
                        showCustomToast(json.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showCustomToast(R.string.toast1);
            }
            Log.e("HttpClient", s);
        } catch (IOException e) {
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
