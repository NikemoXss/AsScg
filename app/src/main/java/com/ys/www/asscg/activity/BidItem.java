package com.ys.www.asscg.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//投资详情
public class BidItem extends BaseActivity implements OnClickListener {
    ImageView imageview;
    TextView title;
    private long itemId;
    private int itemType;
    TextView tzxq_title, item_nhl, item_jkje, item_jkqx, item_syje, item_jd;
    ProgressBar item_progress;
    EditText tzje_rs;
    /**
     * 投标记录的显示
     */
    private TextView info1;
    String jkqx, jkfs, jl;
    Double nhl;
    Long ze;
    RelativeLayout xmmx_re_sx;
    // 密码
    String pwdInputName, pwdInputName_dxb;
    // 是否是定向标 0不是 1是
    String is_direction;
    RelativeLayout item_xxpl, cjjl_rs, choosejxj_scg;

    String borrow_duration, repayment_type;
    String id= "";
    String redid = "";
    TextView nll_tz_jxj, nll_tz_redpkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biditem);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imageview = (ImageView) findViewById(R.id.title_right);
        imageview.setVisibility(View.VISIBLE);
        imageview.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);
        title.setText("项目详情");

        findViewById(R.id.calculator).setOnClickListener(this);
        findViewById(R.id.item_xxpl).setOnClickListener(this);
        findViewById(R.id.cjjl_rs).setOnClickListener(this);
        findViewById(R.id.item_ljtj_rs).setOnClickListener(this);
        findViewById(R.id.chooseredpkg_scg).setOnClickListener(this);
        choosejxj_scg = (RelativeLayout) findViewById(R.id.choosejxj_scg);
        choosejxj_scg.setOnClickListener(this);
        tzje_rs = (EditText) findViewById(R.id.tzje_rs);

        info1 = (TextView) findViewById(R.id.info1);

        nll_tz_jxj = (TextView) findViewById(R.id.nll_tz_jxj);
        nll_tz_redpkg = (TextView) findViewById(R.id.nll_tz_redpkg);

        Intent intent = getIntent();
        itemId = intent.getExtras().getLong("id");
        itemType = intent.getExtras().getInt("type");
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        tzxq_title = (TextView) findViewById(R.id.tzxq_title);
        item_nhl = (TextView) findViewById(R.id.item_nhl);
        item_jkje = (TextView) findViewById(R.id.item_jkje);
        item_jkqx = (TextView) findViewById(R.id.item_jkqx);
        item_syje = (TextView) findViewById(R.id.item_syje);
        item_jd = (TextView) findViewById(R.id.item_jd);
        item_progress = (ProgressBar) findViewById(R.id.item_progress);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.title_right:
                finish();
                break;
            case R.id.calculator:
                if (jl != null) {
                    Intent info = new Intent(BidItem.this, CalculateActivity.class);
                    info.putExtra("lilv", nhl);
                    info.putExtra("qixian", jkqx);
                    info.putExtra("fangshi", jkfs);
                    info.putExtra("jiangli", jl);
                    info.putExtra("guanli", "0");
                    info.putExtra("zonge", ze);
                    startActivity(info);
                } else {
                    showCustomToast(R.string.toast1);
                }
                break;
            case R.id.item_xxpl:
//			Intent intent1 = new Intent(BidItem.this, XxplActivity.class);
//			intent1.putExtra("id", itemId);
//			startActivity(intent1);
                break;
            case R.id.cjjl_rs:
//			Intent intent2 = new Intent(BidItem.this, tzDetailsListActivity.class);
//			intent2.putExtra("id", itemId);
//			intent2.putExtra("type", itemType);
//			startActivity(intent2);
                break;
            case R.id.item_ljtj_rs:
                if (Default.userId == 0) {
                    Intent intent = new Intent(BidItem.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    inputTitleDialog();
                }
                break;
            case R.id.choosejxj_scg:
//			Intent intent = new Intent(BidItem.this, ChooseJxjList.class);
//			intent.putExtra("borrow_duration", borrow_duration);
//			intent.putExtra("repayment_type", repayment_type);
//			startActivityForResult(intent, 0);
                break;
            case R.id.chooseredpkg_scg:
//			Intent i = new Intent(BidItem.this, ChooseRedPacketList.class);
//			i.putExtra("borrow_duration", borrow_duration);
//			i.putExtra("repayment_type", repayment_type);
//			startActivityForResult(i, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 1) {
                redid = data.getStringExtra("return_cbid");
                if (!"00".equals(id)) {
                    String rate = data.getStringExtra("return_cbmoney");
                    nll_tz_redpkg.setText(rate);
                } else {
                    nll_tz_redpkg.setText("");
                    redid = "";
                }
            } else {
                id = data.getStringExtra("return_cbid");
                if (!"00".equals(id)) {
                    String rate = data.getStringExtra("return_cbrate");
                    nll_tz_jxj.setText(rate);
                } else {
                    nll_tz_jxj.setText("");
                    id = "";
                }
            }
        } else {
            if (requestCode == 1) {
                nll_tz_redpkg.setText("");
                redid = "";
            } else {
                nll_tz_jxj.setText("");
                id = "";
            }
        }

    }

    private void inputTitleDialog() {
        LayoutInflater mInflater = null;
        mInflater = LayoutInflater.from(BidItem.this);
        View layout = mInflater.inflate(R.layout.diydialog, null);
        View layouttitle = mInflater.inflate(R.layout.diydialogtitle, null);

        final EditText zfpwd = (EditText) layout.findViewById(R.id.zfpwd);
        final EditText dxb_zfpwd = (EditText) layout.findViewById(R.id.dxb_zfpwd);

        if (is_direction.equals("0")) {
            layout.findViewById(R.id.layout_dxb).setVisibility(View.GONE);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入支付密码").setIcon(R.drawable.scg_logo_small).setView(layout).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (is_direction.equals("0")) {
                    pwdInputName = zfpwd.getText().toString();
                    doHttpCheck(pwdInputName, "");
                } else {
                    pwdInputName_dxb = dxb_zfpwd.getText().toString();
                    pwdInputName = zfpwd.getText().toString();

                    if ("".equals(pwdInputName)) {
                        showCustomToast("定向标密码不能为空！");
                    } else {
                        doHttpCheck(pwdInputName, pwdInputName_dxb);
                    }
                }

            }
        });
        builder.setCustomTitle(layouttitle);
        builder.show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                super.run();
                getPageInfoHttp();
            }
        }.start();

    }

    public void getPageInfoHttp() {
        String url = Default.ip + Default.tzListItem;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("id", itemId);
            jsonObject.put("type", itemType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                Message message = new Message();
                message.what = 1;
                message.obj = str2json(s);
                handler.sendMessage(message);
            } else {
                showCustomToast("获取失败");
            }
            Log.e("HttpClient", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void jxJson(JSONObject json) {
        try {
            nhl = json.getDouble("borrow_interest_rate");
            ze = json.getLong("borrow_money");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        jkqx = json.optString("borrow_duration");
        jkfs = json.optString("huankuan_type");
        jl = json.optString("reward");
        is_direction = json.optString("is_direction");
        try {
            item_jd.setText("进度：" + json.optString("progress") + "%");
            tzxq_title.setText(json.optString("borrow_name"));
            item_nhl.setText(json.getDouble("borrow_interest_rate") + "%");
            if ("1".equals(json.optString("repayment_type"))) {
                item_jkqx.setText(json.optString("borrow_duration") + "天");
            } else {
                item_jkqx.setText(json.optString("borrow_duration") + "个月");
            }
            borrow_duration = json.optString("borrow_duration");
            repayment_type = json.optString("repayment_type");
            item_jkje.setText(json.getLong("borrow_money") + "元");
            item_syje.setText(json.getDouble("need") + "");
            item_progress.setProgress((int) json.getDouble("progress"));
            info1.setText(json.getInt("invest_num") + "人");
            // if ((int) json.getDouble("progress") == 100) {
            //
            // }
            String ticket_status = json.optString("ticket_status");// 0：不使用 1：使用
            if ("1".equals(ticket_status)) {
                choosejxj_scg.setVisibility(View.VISIBLE);
            } else {
                choosejxj_scg.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void doHttpCheck(final String pwd_str, final String pwd_dxb) {
        String url = Default.ip + Default.tzListItem4;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("borrow_id", itemId);
            jsonObject.put("type", itemType);
            jsonObject.put("pin", pwd_str);// 支付密码
            jsonObject.put("money", tzje_rs.getText().toString());
            jsonObject.put("uid", Default.userId);
            jsonObject.put("borrow_pass", pwd_dxb);
            jsonObject.put("ticket_id", id);
            jsonObject.put("redpacket_id", redid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject json=str2json(s);
                try {
                    if (json.getInt("status") == 1) {
                        String messageInfo = json.getString("message");
                        showCustomToast(messageInfo);
                        finish();
                    } else {
                        showCustomToast(json.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showCustomToast("获取失败");
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    jxJson(jsonObject);
                    break;
                default:
                    break;
            }
        }
    };

}
