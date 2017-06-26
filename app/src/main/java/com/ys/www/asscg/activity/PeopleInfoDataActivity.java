/**
 *
 */
package com.ys.www.asscg.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.base.MyLog;
import com.ys.www.asscg.dialog.BaseDialog;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户设置rs
 */

public class PeopleInfoDataActivity extends BaseActivity implements OnClickListener {

    private TextView info[];
    private ImageView mmImageView;
    /**
     * 实名认证 状态 姓名 身份证号
     */
    private int real_status;
    private String realName;
    private String realId;
    /**
     * 手机认证状态 手机号
     */
    private int phone_status;
    private String phone;
    /**
     * 邮箱 认证状态 邮箱
     */
    private int email_status;
    private String email;
    /**
     * 银行卡 认证状态 银行卡
     */
    private int card_status;
    private String card;

    private TextView vip_tv, title, exit;
    ImageView iv_back;
    private int vip_status;

    private DisplayImageOptions options;

    RelativeLayout gesture_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_info_zhanghu);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doHttp();
    }

    public void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText("用户设置");
        iv_back = (ImageView) findViewById(R.id.title_right);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        findViewById(R.id.rl_smrz).setOnClickListener(this);
        findViewById(R.id.rl_yhkh).setOnClickListener(this);
        findViewById(R.id.rl_dlmm).setOnClickListener(this);
        findViewById(R.id.rl_jymm).setOnClickListener(this);
        findViewById(R.id.rl_vip).setOnClickListener(this);
        findViewById(R.id.gesture_login).setOnClickListener(this);

        vip_tv = (TextView) findViewById(R.id.tv_vip);
        findViewById(R.id.exit).setOnClickListener(this);

        // TextView text = (TextView) findViewById(R.id.title);
        // text.setText(R.string.peo_info2);

        info = new TextView[4];
        info[0] = (TextView) findViewById(R.id.tv_smrz);
        info[3] = (TextView) findViewById(R.id.tv_yhk);

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.p_jk)
                .showImageOnFail(R.drawable.p_jk).resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        findViewById(R.id.rl_vip).setOnClickListener(this);

        // vip_tv = (TextView) findViewById(R.id.tv_vip);

    }

    // 接受之前界面返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getStatus(int status) {
        switch (status) {
            case 0:
                return "未验证";
            case 3:
                return "审核中";
            default:
                return "未验证";
        }
    }

    // 0 待审核 1 通过 2 未通过 3 处理中 4 未申请

    private String getVIPStatus(int vipStatus) {

        switch (vipStatus) {
            case 0:
                return "待审核";
            case 1:
                return "通过 ";
            case 2:
                return "未通过";
            case 3:
                return "处理中";
            case 4:
                return "未申请";

            default:
                return "未申请";
        }

    }

    public void updateInfo(JSONObject json) {

        /**
         * 用户头像
         */
        // mavatar = json.getString("avatar");
        /***
         * 实名认证
         */
        real_status = json.optInt("real_status", 0);
        realName = getStatus(real_status);
        if (real_status == 1) {
            realName = json.optString("real");
            realId = json.optString("real_id");
        }
        /**
         * 验证手机号
         */
        phone_status = json.optInt("phone_status", 0);
        phone = getStatus(phone_status);
        if (phone_status == 1) {
            phone = json.optString("phone");
            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            Editor editor = preferences.edit();
            editor.putString("tel", phone);
            editor.commit();
        }
        /**
         * 电子邮箱
         */
        email_status = json.optInt("email_status", 0);
        email = json.optString("email");
        /**
         * 银行卡
         */
        card_status = json.optInt("card_status", 0);
        card = getStatus(card_status);
        if (card_status == 1) {
            card = json.optString("card");
        }

        vip_status = json.optInt("vip_status", 4);

        vip_tv.setText(getVIPStatus(vip_status));

        // mmm_status = json.optInt("escrow_account", 0);
        // sq1_status = json.optInt("invest_auth", 0);
        // sq2_status = json.optInt("repayment", 0);

        info[0].setText(realName);
        // info[1].setText(phone);
        // info[2].setText(email_status == 1 ? email : getStatus(email_status));
        info[3].setText(card);
        ImageLoader.getInstance().displayImage(Default.ip + Default.user_photo_path, mmImageView, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "Input/Output error";
                                break;
                            case DECODING_ERROR:
                                message = "解析错误";
                                break;
                            case NETWORK_DENIED:
                                message = "网络错误";
                                break;
                            case OUT_OF_MEMORY:
                                message = "内存错误";
                                break;
                            case UNKNOWN:
                                message = "位置错误";
                                break;
                        }
                        // Toast.makeText(getBaseContext(), message,
                        // Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_vip:
                // 0 待审核 1 通过 2 未通过 3 处理中 4 未申请
                if (vip_status == 4 || vip_status == 2) {
                    startActivity(new Intent(PeopleInfoDataActivity.this, VIPActivity.class));
                } else if (vip_status == 1) {
                    showCustomToast("VIP认证已通过");
                } else if (vip_status == 0) {
                    showCustomToast("VIP待审核");
                } else {
                    showCustomToast("VIP认证正在处理中");
                }

                break;

            case R.id.title_right:
                finish();
                break;
            // 实名认证
            case R.id.rl_smrz:
                // TODO
                Intent intent = new Intent();
                if (real_status > 0) {
                    intent.setClass(PeopleInfoDataActivity.this, PeopleInfoSmrz.class);
                    intent.putExtra("real_status", real_status);
                    intent.putExtra("realName", realName);
                    intent.putExtra("realId", realId);
                    // intent.setClass(peopleInfoDataActivity.this,
                    // PeopleInfoIdcard.class);
                } else {
                    intent.setClass(PeopleInfoDataActivity.this, PeopleInfoIdcard.class);
                }
                startActivity(intent);
                break;

            // 银行卡
            case R.id.rl_yhkh:
                // Intent intent2 = new Intent(peopleInfoDataActivity.this,
                // PeopleInfo_XS_backcard.class);
                // intent2.putExtra("card_status", card_status);
                // startActivity(intent2);
                if (real_status == 1) {
                    Intent intent2 = new Intent(PeopleInfoDataActivity.this, PeopleInfo_XS_backcard.class);
                    intent2.putExtra("card_status", card_status);
                    startActivity(intent2);
                } else {
                    showCustomToast("请先通过实名认证");
                }
                break;
            // 登陆密码
            case R.id.rl_dlmm:
                Intent intent5 = new Intent(PeopleInfoDataActivity.this, PasswordActivity.class);
                startActivity(intent5);
                break;
            // 交易密码
            case R.id.rl_jymm:
                Intent intent6 = new Intent(PeopleInfoDataActivity.this, PasswordJaoYiActivity.class);
                startActivity(intent6);
                break;
            case R.id.exit:
                showMyDialog();
                break;
            case R.id.gesture_login:
//                Intent intent7 = new Intent(PeopleInfoDataActivity.this, GestureLockViewActivity.class);
//                intent7.putExtra("isRu", 1);
//                startActivity(intent7);
                break;
            default:
                break;

        }

    }

    public void showDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("退出");
        builder.setMessage("是否退出该用户");
        builder.setIcon(R.drawable.scg_logo_small);

        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                doHttpExit();
                dialog.dismiss();
                // MainTabActivity_Sx.mainTabActivity.IndexView();

            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showMyDialog() {
        BaseDialog dialog = BaseDialog.getDialog(this, "退出", "确认退出么！", "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        doHttpExit();
                    }
                }.start();
                dialog.dismiss();
            }
        }, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void doHttpExit() {
        String url = Default.ip + Default.exit;
        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    MyLog.e("zzx", "exit成功");
                    Default.userId = 0;
                    finish();
                    MainTabActivity.mainTabActivity.IndexView();
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

    public void doHttp() {
        String url = Default.ip + Default.peoInfoSafe;
        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    updateInfo(response);
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
