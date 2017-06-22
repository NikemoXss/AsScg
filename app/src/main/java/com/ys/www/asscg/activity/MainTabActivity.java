package com.ys.www.asscg.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.www.asscg.R;
import com.ys.www.asscg.fragment.FragmentFactory;
import com.ys.www.asscg.receiver.NetWorkStatusBroadcastReceiver;
import com.ys.www.asscg.util.Default;

/**
 * Created by Administrator on 2017/6/22.
 */

public class MainTabActivity extends FragmentActivity {
    TextView title_view;
    RadioGroup rg_tab;
    RadioButton tab_one, tab_two, tab_three;
    FrameLayout content_frame;
    NetWorkStatusBroadcastReceiver netWorkStatusBroadcastReceiver;
    /**
     * 当前TabBar 选择项
     */
    private int currentIndex = 1;
    /***
     * 当前TabBar 选择项 之前的选择项
     */
    private int lastIndex = 1;

    private boolean changeToHomeFlag = false;

    public static MainTabActivity mainTabActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        mainTabActivity = this;
        init();
    }

    public void init() {
        title_view = findViewById(R.id.title_view);
        rg_tab = findViewById(R.id.rg_tab);
        tab_one = findViewById(R.id.tab_one);
        tab_two = findViewById(R.id.tab_two);
        tab_three = findViewById(R.id.tab_three);
        content_frame = findViewById(R.id.content_frame);
        netWorkStatusBroadcastReceiver = new NetWorkStatusBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStatusBroadcastReceiver, filter);

        Fragment fragment = FragmentFactory.getInstanceByIndex(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        title_view.setText("理财");
        tab_one.setChecked(true);

        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /**
                 * 判断用户是否登陆 ， 借款和个人账户的跳转选择
                 */

                int selectedId = 0;
                switch (checkedId) {
                    case R.id.tab_one:
                        selectedId = 1;
                        break;
                    case R.id.tab_two:
                        selectedId = 2;
                        break;
                    case R.id.tab_three:
                        selectedId = 3;
                        break;

                    default:
                        break;
                }
                lastIndex = currentIndex;
                currentIndex = selectedId;
                changeTitle(selectedId);

                Fragment fragment = FragmentFactory.getInstanceByIndex(selectedId);
//                if (changeToHomeFlag) {
//                    HomeFragment homeFragment = (HomeFragment) fragment;
//                    changeToHomeFlag = false;
//                }

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                        .commitAllowingStateLoss();

            }
        });
    }

    /**
     * 更改标题
     *
     * @param index Tab 选项
     */
    private void changeTitle(int index) {

        switch (index) {
            case 1:
                title_view.setText("理财");
                break;
            case 2:
                if (Default.userId == 0) {
                    Intent userInfoLoginIntent = new Intent();
                    userInfoLoginIntent.setClass(this, LoginActivity.class);
                    startActivity(userInfoLoginIntent);
                } else {
                    title_view.setText("个人账户");
                }
                break;
            case 3:
                title_view.setText("设置");
                break;

            default:
                break;
        }
    }

    /***
     * 跳转到首页
     *
     * @return
     */

    public void IndexView() {

        tab_one.setChecked(true);

    }

    private long lastTime;
    private int closeNum;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkStatusBroadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                closeNum++;
                if (System.currentTimeMillis() - lastTime > 5000) {
                    closeNum = 0;
                }
                lastTime = System.currentTimeMillis();
                if (closeNum == 1) {
                    finish();
                } else {
                    showCustomToast(R.string.exit);
                }
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);

    }

    public void showCustomToast(String text) {
        View toastRoot = LayoutInflater.from(MainTabActivity.this).inflate(R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(MainTabActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    public void showCustomToast(int id) {
        showCustomToast(getResources().getString(id).toString());
    }
}
