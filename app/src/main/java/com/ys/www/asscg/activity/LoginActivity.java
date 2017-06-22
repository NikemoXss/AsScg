package com.ys.www.asscg.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.base.MyLog;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;
import com.ys.www.asscg.util.Utils;
import com.ys.www.asscg.view.EditTextWithDel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity implements OnClickListener {

    EditTextWithDel login_zh_sx, login_mm_sx;
    Button login_sx;
    String login_zh_sx_str, login_mm_sx_str;
    TextView title, register_sx, forget_pwd_sx;
    ImageView iv, title_left1;
    CheckBox cb_login_sx;
    LinearLayout et_dh, login_zh_ll, login_pwd_ll, remember_ll, login_ed_ll;
    Button login_sx_bt;
    static LoginActivity mLoginActivity_Scg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_scg);
        LoginActivity.mLoginActivity_Scg = this;
        login_sx_bt = findViewById(R.id.login_sx_bt);
        login_sx_bt.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("登录");
        login_zh_sx = findViewById(R.id.login_zh_sx);
        login_mm_sx = findViewById(R.id.login_mm_sx);
        login_zh_sx.setPic(R.drawable.icon_03);
        login_mm_sx.setPic(R.drawable.icon_05);

        iv = findViewById(R.id.title_right);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(this);

        title_left1 = findViewById(R.id.title_left1);
        title_left1.setOnClickListener(this);

        findViewById(R.id.forget_pwd_sx).setOnClickListener(this);
        findViewById(R.id.register_scg).setOnClickListener(this);
        cb_login_sx = findViewById(R.id.cb_login_sx);
        cb_login_sx.setChecked(true);

        et_dh = findViewById(R.id.et_dh);
        login_zh_ll = findViewById(R.id.login_zh_ll);
        login_pwd_ll = findViewById(R.id.login_pwd_ll);
        remember_ll = findViewById(R.id.remember_ll);
        login_ed_ll = findViewById(R.id.login_ed_ll);

        // ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
        // animator.setTarget(et_dh);
        // animator.setDuration(1000).start();
        // animator.addUpdateListener(new AnimatorUpdateListener() {
        // @Override
        // public void onAnimationUpdate(ValueAnimator animation) {
        // et_dh.setTranslationY((Float) animation.getAnimatedValue());
        // }
        // });

        ZxAnimator(login_sx_bt, 100);
        ZxAnimator(remember_ll, 300);
        ZxAnimator(login_ed_ll, 400);
        // ZxAnimator(login_pwd_ll, 800);
        // ZxAnimator(login_zh_ll, 1000);

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (isHasSh()) {
            title_left1.setVisibility(View.VISIBLE);
        }
    }

    public void ZxAnimator(final View view, long time) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                zxxxx(view);
            }
        }, time);

    }

    public void zxxxx(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
        animator.setTarget(view);
        animator.setDuration(800).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_sx_bt:// 登录
                login_zh_sx_str = login_zh_sx.getText().toString();
                login_mm_sx_str = login_mm_sx.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        doHttp2();
                    }
                }.start();
                break;
            case R.id.title_right:
                Intent intent = getIntent();
                setResult(Default.LOGIN_TYPE_2, intent);
                finish();
                break;
            case R.id.forget_pwd_sx:
                break;
            case R.id.register_scg:
                break;
            case R.id.title_left1:
//			Intent i1 = new Intent(LoginActivity_Scg.this, GestureLockViewActivity.class);
//			i1.putExtra("isRu", 0);
//			i1.putExtra("username", login_zh_sx.getText().toString());
//			i1.putExtra("pwd", login_mm_sx.getText().toString());
//			startActivity(i1);
                break;
            default:
                break;
        }
    }

    public boolean isHasSh() {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("scenelist",
                Context.MODE_PRIVATE);
        // String liststr = sharedPreferences.getString(Utils.SCENE_LIST, "");
        String user = sharedPreferences.getString(Utils.SCENE_USER, "");
        String pwd = sharedPreferences.getString(Utils.SCENE_PWD, "");
        String mUser = login_zh_sx.getText().toString();
        String mPwd = login_mm_sx.getText().toString();
        Log.e("isHasShisHasSh", user + ";" + pwd + ";" + mUser + ";" + mPwd);
        if ((!"".equals(user)) && (user.equals(mUser) && (pwd.equals(mPwd)))) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && data != null) {
            login_zh_sx.setText(data.getExtras().getString("name"));
            login_mm_sx.setText(data.getExtras().getString("password"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                finish();
//				MainTabActivit_Scg.mainTabActivity.IndexView();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void doHttp2() {
        String url = "http://120.25.58.247/member/Mobilecommon/actlogin";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("sUserName", "l5322776");
            jsonObject.put("sPassword", "liu13776831775");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if(!"".equals(s)){
                str2json(s);
            }else {
                showCustomToast("登录失败");
            }
            Log.e("HttpClient", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void str2json(String s) {
        try {
            JSONObject json = new JSONObject(s);
            if (json.has("status")) {
                if (json.getInt("status") == 1) {
                    MyLog.e("123", "登陆后的数据:" + json.toString());
                    String username = json.optString("username");
                    Long uid = json.getLong("uid");
                    Default.userId = uid;
                    String credits = json.optString("credits");
                    if (cb_login_sx.isChecked()) {
                        saveNP_SX(credits, username);
                        Intent intent=new Intent(LoginActivity.this,MainTabActivity.class);
                        startActivity(intent);
                    } else {
                        clearNP();
                    }
                    finish();
                } else {
                    showCustomToast(json.getString("message"));
                }
            } else {
                showCustomToast(json.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储用户信息
     */
    public void saveNP_SX(String credits, String username) {

        /**
         * 加密存储用户信息
         */
        SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Default.userName, login_zh_sx_str);
        edit.putString(Default.userPassword, login_mm_sx_str);
        edit.putString("credits", credits);
        edit.putString("username", username);
        edit.commit();

    }

    public void getNP() {
        SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
        if (!sp.getString(Default.userName, "").equals("")) {
            login_zh_sx.setText(sp.getString(Default.userName, ""));
            login_mm_sx.setText(sp.getString(Default.userPassword, ""));
            // mName = sp.getString(Default.userName, "");
            // mPassword = sp.getString(Default.userPassword, "");
        }

    }

    public void clearNP() {
        SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Default.userName, "");
        edit.putString(Default.userPassword, "");
        edit.commit();
    }

}
