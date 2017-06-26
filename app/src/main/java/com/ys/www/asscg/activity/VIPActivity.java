package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.www.asscg.R;
import com.ys.www.asscg.api.SystenmApi;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.dialog.PopDialog;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VIPActivity extends BaseActivity implements OnClickListener {

    private TextView vip_free_textView;
    private EditText vip_info_editText;
    private JSONArray kf_list;
    private String kf_id;
    private PopDialog kf_popDialog;
    private TextView user_select_kfName, title;
    ImageView imageView;
    private boolean isClickItem = false;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip);

        initView();

    }

    public void initView() {
        imageView = (ImageView) findViewById(R.id.title_right);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("VIP申请");
        findViewById(R.id.vip_button).setOnClickListener(this);
        vip_free_textView = (TextView) findViewById(R.id.vip_fee);

        kf_popDialog = new PopDialog(VIPActivity.this);
        kf_popDialog.setDialogTitle("选择客服");
        user_select_kfName = (TextView) findViewById(R.id.show_selecet_name);

        findViewById(R.id.vip_kf).setOnClickListener(this);
        kf_popDialog.setonClickListener(this);
        kf_popDialog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                isClickItem = true;

                kf_popDialog.setDefaultSelect(position);

                index = position;

            }
        });

        vip_info_editText = (EditText) findViewById(R.id.vip_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                finish(0);
                break;
            case R.id.vip_kf:

                if (kf_list != null) {

                    SystenmApi.hiddenKeyBoard(VIPActivity.this);

                    kf_popDialog.setDefaultSelect(0);
                    try {
                        kf_id = kf_list.getJSONObject(0).getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    kf_popDialog.showAsDropDown(v);

                }
                break;
            case R.id.cButton:
                kf_popDialog.dismiss();
                break;
            case R.id.sButton:

                if (isClickItem) {
                    try {
                        kf_id = kf_list.getJSONObject(index).getString("id");

                        user_select_kfName
                                .setText(kf_list.getJSONObject(kf_popDialog.getDefaultSelect()).getString("user_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    kf_popDialog.setDefaultSelect(0);
                    try {
                        user_select_kfName.setText(kf_list.getJSONObject(0).getString("user_name"));

                        kf_id = kf_list.getJSONObject(0).getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                kf_popDialog.dismiss();
                break;
            case R.id.vip_button:

                if (kf_id == null) {
                    showCustomToast("请选择客服");

                    return;
                } else if (vip_info_editText.getText().toString().equals("")) {
                    showCustomToast("请输入申请说明");
                    return;
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            vipActiondohttp();
                        }
                    }.start();
                }
                break;
        }
    }

    private void initInfo(JSONObject json) {
        try {
            vip_free_textView.setText(json.getString("fee_vip") + "元/年");
            kf_list = json.getJSONArray("list");
            for (int i = 0; i < kf_list.length(); i++) {
                JSONObject obj = kf_list.getJSONObject(i);
                kf_popDialog.addItem(obj.getString("user_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                super.run();
                doHttp();
            }
        }.start();
    }

    private void doHttp() {
        String url = Default.ip + Default.vip_kf_inf;
        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.has("status")) {
                    try {
                        if (response.getInt("status") == 1) {
                            initInfo(response);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                showCustomToast("获取失败");
            }
            Log.e("HttpClient", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vipActiondohttp() {
        String url = Default.ip + Default.hf_vip;
        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.has("status")) {
                    try {
                        if (response.getInt("status") == 1) {
                            showCustomToast("提交申请成功，请等待管理员审核");
                        } else {
                            showCustomToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int code = data.getIntExtra("code", -1);
        String message = data.getStringExtra("message");
        int code1 = data.getIntExtra("code1", -1);
        String message1 = data.getStringExtra("message1");
        int code2 = data.getIntExtra("code2", -1);
        String message2 = data.getStringExtra("message2");
        if (code == 88) {
            Toast.makeText(this, "恭喜您，支付成功！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, code + ":" + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

}
