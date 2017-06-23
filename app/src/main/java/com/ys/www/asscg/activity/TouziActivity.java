package com.ys.www.asscg.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.www.asscg.R;
import com.ys.www.asscg.adapter.MyReViewAdapter;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/23.
 */

public class TouziActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyReViewAdapter myReViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    int lastVisibleItem = 0;
    private int maxPage, curPage = 1, pageCount = 5;
    private JSONArray jsonArray = null;
    Map<String, Object> map = null;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ImageView imageview;
    TextView title;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject json = (JSONObject) msg.obj;
                    initJsonData(json);
                    break;
                case 2:
                    JSONObject jo = (JSONObject) msg.obj;
                    updateAddInfo(jo);
                    break;
                case 3:
                    showCustomToast("没有更多数据了");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_touzi);

        imageview = (ImageView) findViewById(R.id.title_right);
        imageview.setVisibility(View.VISIBLE);
        imageview.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("投资列表");

        recyclerView = findViewById(R.id.rev);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myReViewAdapter = new MyReViewAdapter(list, TouziActivity.this);
        recyclerView.setAdapter(myReViewAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        doHttp();
                    }
                }.start();
            }
        });


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myReViewAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        JSONObject jo = new JSONObject();
                                        if (curPage + 1 <= maxPage) {
                                            curPage += 1;
                                            jo.put("type", 11);
                                            jo.put("page", curPage);
                                            Log.e("builder", curPage + "");
                                            jo.put("limit", pageCount);
                                            doHttp(jo);
                                        } else {
                                            handler.sendEmptyMessage(3);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    }, 1000);
                    Toast.makeText(TouziActivity.this, "Load Finished!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    //每次上拉加载的时候，给RecyclerView的后面添加了10条数据数据
    private void loadMoreData() {
        Map<String, Object> map;
        List<Map<String, Object>> listsun = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, Object>();
            map.put("aa", "cc00");
            listsun.add(map);
        }
        list.addAll(listsun);
        new Thread() {
            @Override
            public void run() {
                super.run();
                myReViewAdapter.notifyItemRangeChanged(0, list.size());
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                doHttp();
            }
        }.start();

    }

    public void doHttp(JSONObject jo) {
        String url = Default.ip + Default.tzAllList;
        try {
            String s = HttpClient.post(url, jo.toString());
            if (!"".equals(s)) {
                Message message = new Message();
                message.what = 2;
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


    public void doHttp() {
        String url = Default.ip + Default.tzAllList;
        JSONObject jsonObject = new JSONObject();
        try {
            curPage = 1;
            jsonObject.put("uid", 0);
            jsonObject.put("type", 11);
            jsonObject.put("page", curPage);
            jsonObject.put("limit", pageCount);
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

    public JSONObject str2json(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void initJsonData(JSONObject json) {
        list.clear();
        try {
            // 散标数据初始化
            List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
            maxPage = json.getInt("totalPage");
            curPage = json.getInt("nowPage");
            if (!json.isNull("list")) {
                jsonArray = json.getJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject templist = jsonArray.getJSONObject(i);
                        map = new HashMap<String, Object>();
                        map.put("id", templist.getLong("id"));
                        map.put("type", templist.getInt("type"));

                        Double all = templist.getDouble("borrow_money");
                        Double has = templist.getDouble("has_borrow");

                        map.put("has_borrow_sx", getSubtract(all, has));

                        map.put("progress_tz_sx", templist.getDouble("progress"));
                        map.put("item_tzbt_sx", templist.getString("borrow_name"));
                        map.put("item_jkje_sx", templist.getString("borrow_money"));
                        map.put("item_tzqx_sx", templist.getString("borrow_duration"));
                        map.put("item_nhl_sx", templist.getString("borrow_interest_rate"));
                        map.put("item_time_sx", templist.getString("addtime"));
                        map.put("repayment_type", templist.getString("repayment_type"));
                        list2.add(map);
                    }
                }
            }
            list.clear();
            list.addAll(list2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        myReViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(TouziActivity.this, "Refresh Finished!", Toast.LENGTH_SHORT).show();
    }

    protected void updateAddInfo(JSONObject json) {
        // TODO Auto-generated method stub

        try {
            maxPage = json.getInt("totalPage");
            curPage = json.getInt("nowPage");
            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            if (!json.isNull("list")) {
                jsonArray = json.getJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject templist = jsonArray.getJSONObject(i);
                        map = new HashMap<String, Object>();
                        map.put("id", templist.getLong("id"));
                        map.put("type", templist.getInt("type"));

                        Double all = templist.getDouble("borrow_money");
                        Double has = templist.getDouble("has_borrow");

                        map.put("has_borrow_sx", getSubtract(all, has));

                        map.put("progress_tz_sx", templist.getDouble("progress"));
                        map.put("item_tzbt_sx", templist.getString("borrow_name"));
                        map.put("item_jkje_sx", templist.getString("borrow_money"));
                        map.put("item_tzqx_sx", templist.getString("borrow_duration"));
                        map.put("item_nhl_sx", templist.getString("borrow_interest_rate"));
                        map.put("item_time_sx", templist.getString("addtime"));
                        map.put("repayment_type", templist.getString("repayment_type"));
                        list1.add(map);
                    }

                }
            }
            list.addAll(list1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        myReViewAdapter.notifyItemRangeChanged(0, list.size());
    }

    private String getSubtract(Double all, Double has) {
        BigDecimal b1 = new BigDecimal(all.toString());
        BigDecimal b2 = new BigDecimal(has.toString());
        Double isuse = b1.subtract(b2).doubleValue();
        return isuse + "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                finish();
                break;

            default:
                break;
        }
    }
}
