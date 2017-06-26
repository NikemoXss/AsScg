package com.ys.www.asscg.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ys.www.asscg.R;
import com.ys.www.asscg.activity.BidItem;
import com.ys.www.asscg.activity.LoginActivity;
import com.ys.www.asscg.activity.NewsListActivity;
import com.ys.www.asscg.activity.TouziActivity;
import com.ys.www.asscg.base.BaseFragment;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;
import com.ys.www.asscg.view.AutoVerticalScrollTextView;
import com.ys.www.asscg.view.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ys.www.asscg.activity.MainTabActivity.mainTabActivity;

//苏常主页
public class IndexFragment_Scg extends BaseFragment implements OnClickListener, OnPageChangeListener {

    private int curPage = 1, pageCount = 1;
    private JSONArray list = null;
    private long id_tt, id_yy;
    private int type_tt, type_yy;

    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private ArrayList<View> footPointViews;
    private List<String> bannerItemspic;
    private LinearLayout footpointview;
    private static final int[] pics = {R.drawable.slide};
    private ViewPager flipper;
    private int index;
    private boolean change;
    private int number = 0;
    private boolean isRunning = true;

    LinearLayout item_tt, item_yy;

    private AutoVerticalScrollTextView autoVerticalScrollTextView;

    TextView tt_borrow_name, tt_borrow_money, tt_has_borrow, tt_borrow_duration, tt_borrow_interest_rate, tt_jindu;
    ProgressBar tt_progress;
    TextView yy_borrow_name, yy_borrow_money, yy_has_borrow, yy_borrow_duration, yy_borrow_interest_rate, yy_jindu;
    ProgressBar yy_progress;

    List<String> list_str;
    List<String> mStr;


    private String[] strings1 = {"我的剑，就是你的剑!", "俺也是从石头里蹦出来得!", "我用双手成就你的梦想!", "人在塔在!", "犯我德邦者，虽远必诛!", "我会让你看看什么叫残忍!",
            "我的大刀早已饥渴难耐了!"};
    private Handler mHandler = new Handler();
    private Runnable runable = new Runnable() {

        @Override
        public void run() {
            changePic();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_index, null);
        mainView.findViewById(R.id.gg_loadmore).setOnClickListener(this);
        footpointview = (LinearLayout) mainView.findViewById(R.id.footer_point);
        autoVerticalScrollTextView = (AutoVerticalScrollTextView) mainView
                .findViewById(R.id.autoVerticalScrollTextView);
        // autoVerticalScrollTextView.setText(strings[0]);

        autoVerticalScrollTextView.setOnClickListener(this);

        mainView.findViewById(R.id.item_yy).setOnClickListener(this);
        mainView.findViewById(R.id.item_tt).setOnClickListener(this);

        initView(mainView);
        initTtview(mainView);
        initYyview(mainView);
        mHandler.postDelayed(runable, 5000);
        return mainView;
    }

    public void initBannerData(JSONObject json) {

        if (json != null) {
            views.clear();
            // flipper.removeAllViews();
            flipper.setBackgroundDrawable(null);
            footPointViews.clear();
            footpointview.removeAllViews();
            bannerItemspic = new ArrayList<String>();
            try {
                JSONArray array = json.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    bannerItemspic.add(jsonObject.get("pic").toString());
                    // bannerItemspic.add(array.getString(i));

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for (int i = 0; i < bannerItemspic.size(); i++) {
                ImageView footView = new ImageView(mainTabActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                footView.setLayoutParams(params);

                if (i == 0) {
                    footView.setImageResource(R.drawable.b_dx1);
                } else {
                    footView.setImageResource(R.drawable.b_dx2);
                }
                footpointview.addView(footView);
                footPointViews.add(footView);

                final ImageView iv = new ImageView(mainTabActivity);
                iv.setScaleType(ScaleType.FIT_XY);
                iv.setTag(i);

                DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(pics[0])
                        .showImageOnFail(pics[0]).resetViewBeforeLoading(true).cacheOnDisk(true)
                        .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                        .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
                Log.e("url", bannerItemspic.get(i));
                ImageLoader.getInstance().displayImage(Default.ip + bannerItemspic.get(i), iv, options,
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
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage != null) {
                                    iv.setImageBitmap(loadedImage);
                                }

                            }
                        });
                views.add(iv);
            }

        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                super.run();
                doHttp();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                indexdoHttp();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                ggdoHttp();
            }
        }.start();

    }

    /**
     * 首页推荐标
     */
    public void indexdoHttp() {
        String url = Default.ip + Default.tzList;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("is_tuijian", 1);
            jsonObject.put("type", 11);
            jsonObject.put("page", curPage);
            jsonObject.put("limit", pageCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                updateAddInfo(str2json(s));
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

    public void ggdoHttp() {
        String url = Default.ip + Default.sygg;
        try {
            String s = HttpClient.get(url);
            if (!"".equals(s)) {
                JSONObject json = str2json(s);
                if (json.getInt("status") == 1) {
                    String string = json.getString("msg");
                    // int count = string.length();
                    JSONArray jsonArray = json.getJSONArray("msg");
                    list_str = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list_str.add(jsonObject.optString("title"));
                    }
                    Message message = new Message();
                    message.obj = list_str;
                    message.what = 111;
                    handler.sendMessage(message);
                } else {
                    showCustomToast(json.getString("message"));
                }
            } else {
                showCustomToast("获取失败");
            }
            Log.e("HttpClient", s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateAddInfo(JSONObject json) {
        try {

            if (!json.isNull("list")) {
                list = json.getJSONArray("list");
                Message message = new Message();
                message.what = 100;
                message.obj = list;
                handler.sendMessage(message);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取幻灯片
     */
    public void doHttp() {
        String url = Default.ip + Default.bannerPic;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                Message message = new Message();
                message.what = 666;
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

    public void stop() {
        try {
            mHandler.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changePic() {
        if (getActivity() == null) {
            return;
        }
        if (!change) {

            if (index < views.size() - 1) {
                index++;
                setImage(index);
                flipper.setCurrentItem(index);
                if (index == views.size() - 1) {
                    change = true;
                }
            }

        } else {
            if (index > 0) {
                index--;
                setImage(index);
                flipper.setCurrentItem(index);
                if (index == 0) {
                    change = false;
                }
            }
        }
        mHandler.postDelayed(runable, 5000);
    }

    void setImage(int i) {
        for (int j = 0; j < footPointViews.size(); j++) {
            ImageView iv = (ImageView) footPointViews.get(j);
            if (j != i) {
                iv.setImageResource(R.drawable.b_dx2);
            } else {
                iv.setImageResource(R.drawable.b_dx1);
            }
        }
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void initView(View view) {

        views = new ArrayList<View>();
        footPointViews = new ArrayList<View>();
        flipper = (ViewPager) view.findViewById(R.id.flipper);
        ImageView footView = new ImageView(getActivity());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        footView.setLayoutParams(params);

        footView.setImageResource(R.drawable.b_dx1);

        footPointViews.add(footView);
        footpointview.addView(footView);

        flipper = (ViewPager) view.findViewById(R.id.flipper);

        vpAdapter = new ViewPagerAdapter(views);

        flipper.setAdapter(vpAdapter);
        // 绑定回调
        flipper.setOnPageChangeListener(this);

        // new Thread() {
        // @Override
        // public void run() {
        // while (isRunning) {
        // SystemClock.sleep(3000);
        // handler.sendEmptyMessage(199);
        // }
        // }
        // }.start();

    }

    private void initTtview(View mainView) {
        // TODO Auto-generated method stub
        tt_borrow_name = (TextView) mainView.findViewById(R.id.tt_borrow_name);
        tt_borrow_money = (TextView) mainView.findViewById(R.id.tt_borrow_money);
        tt_has_borrow = (TextView) mainView.findViewById(R.id.tt_has_borrow);
        tt_borrow_duration = (TextView) mainView.findViewById(R.id.tt_borrow_duration);
        tt_borrow_interest_rate = (TextView) mainView.findViewById(R.id.tt_borrow_interest_rate);
        tt_jindu = (TextView) mainView.findViewById(R.id.tt_jindu);
        tt_progress = (ProgressBar) mainView.findViewById(R.id.tt_progress);
    }

    private void initYyview(View mainView) {
        // TODO Auto-generated method stub
        yy_borrow_name = (TextView) mainView.findViewById(R.id.yy_borrow_name);
        yy_borrow_money = (TextView) mainView.findViewById(R.id.yy_borrow_money);
        yy_has_borrow = (TextView) mainView.findViewById(R.id.yy_has_borrow);
        yy_borrow_duration = (TextView) mainView.findViewById(R.id.yy_borrow_duration);
        yy_borrow_interest_rate = (TextView) mainView.findViewById(R.id.yy_borrow_interest_rate);
        yy_jindu = (TextView) mainView.findViewById(R.id.yy_jindu);
        yy_progress = (ProgressBar) mainView.findViewById(R.id.yy_progress);
    }

    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                autoVerticalScrollTextView.next();
                number++;
                // autoVerticalScrollTextView.setText(strings[number %
                // strings.length]);
                autoVerticalScrollTextView.setText(mStr.get(number % mStr.size()));
            } else if (msg.what == 100) {
                try {
                    JSONArray ja = (JSONArray) msg.obj;
                    JSONObject jo = null;
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if (i == 0) {
                            id_tt = jo.getLong("id");
                            type_tt = jo.getInt("borrow_type");
                            tt_borrow_name.setText(jo.getString("borrow_name"));
                            tt_borrow_money.setText(jo.getString("borrow_money"));

                            if ("1".equals(jo.getString("repayment_type"))) {
                                tt_borrow_duration.setText(jo.getString("borrow_duration") + "天");
                            } else {
                                tt_borrow_duration.setText(jo.getString("borrow_duration") + "月");
                            }
                            tt_borrow_interest_rate.setText(jo.getString("borrow_interest_rate") + "%");
                            Double all = jo.getDouble("borrow_money");// getSubtract
                            Double has = jo.getDouble("has_borrow");
                            tt_has_borrow.setText(getSubtract(all, has));
                            int progress = getProgress(all, has);
                            tt_progress.setProgress(progress);
                            tt_jindu.setText("进度:" + progress + "%");
                        } else if (i == 1) {
                            id_yy = jo.getLong("id");
                            type_yy = jo.getInt("borrow_type");
                            yy_borrow_name.setText(jo.getString("borrow_name"));
                            yy_borrow_money.setText(jo.getString("borrow_money"));
                            yy_has_borrow.setText(jo.getString("has_borrow"));
                            if ("1".equals(jo.getString("repayment_type"))) {
                                yy_borrow_duration.setText(jo.getString("borrow_duration") + "天");
                            } else {
                                yy_borrow_duration.setText(jo.getString("borrow_duration") + "月");
                            }
                            yy_borrow_interest_rate.setText(jo.getString("borrow_interest_rate") + "%");
                            Double all = jo.getDouble("borrow_money");
                            Double has = jo.getDouble("has_borrow");
                            yy_has_borrow.setText(getSubtract(all, has));
                            int progress = getProgress(all, has);
                            yy_progress.setProgress(progress);
                            yy_jindu.setText("进度:" + progress + "%");

                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (msg.what == 111) {
                mStr = (List<String>) msg.obj;
                if (mStr.size() > 0) {
                    autoVerticalScrollTextView.setText(mStr.get(0));
                    new Thread() {
                        @Override
                        public void run() {
                            while (isRunning) {
                                SystemClock.sleep(3000);
                                handler.sendEmptyMessage(199);
                            }
                        }
                    }.start();
                }

            } else if (msg.what == 666) {
                JSONObject json = (JSONObject) msg.obj;
                initBannerData(json);
                vpAdapter.notifyDataSetChanged();
            }
        }
    };

    private int getProgress(Double all, Double has) {
        BigDecimal b1 = new BigDecimal(all.toString());
        BigDecimal b2 = new BigDecimal(has.toString());
        Double isuse = b1.subtract(b2).doubleValue();
        // BigDecimal isuse1 = new BigDecimal(isuse.toString());
        Double res = b2.divide(b1, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        int i = (int) (Double.parseDouble(res + "") * 100);
        return i;
    }

    private String getSubtract(Double all, Double has) {
        BigDecimal b1 = new BigDecimal(all.toString());
        BigDecimal b2 = new BigDecimal(has.toString());
        Double isuse = b1.subtract(b2).doubleValue();
        return isuse + "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.gg_loadmore:
                Intent intent = new Intent(getActivity(), TouziActivity.class);
                startActivity(intent);
                break;
            case R.id.item_tt:
                if (Default.userId == 0) {
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getActivity(), BidItem.class);
                    intent1.putExtra("id", id_tt);
                    intent1.putExtra("type", shabiphp(type_tt));
                    startActivity(intent1);
                }

                break;
            case R.id.item_yy:
                if (Default.userId == 0) {
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(getActivity(), BidItem.class);
                    intent2.putExtra("id", id_yy);
                    intent2.putExtra("type", shabiphp(type_yy));
                    startActivity(intent2);
                }
                break;
            case R.id.autoVerticalScrollTextView:
                getActivity().startActivity(new Intent(getActivity(), NewsListActivity.class));
                break;
            default:

                break;

        }

    }

    public int shabiphp(int i) {
        int j = 0;
        switch (i) {
            case 1:
                j = 3;
                break;
            case 2:
                j = 4;
                break;
            case 3:
                j = 5;
                break;
            case 4:
                j = 6;
                break;
            case 5:
                j = 7;
                break;

            default:
                break;
        }

        return j;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        index = arg0;
        setImage(arg0);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isRunning = false;
    }

}
