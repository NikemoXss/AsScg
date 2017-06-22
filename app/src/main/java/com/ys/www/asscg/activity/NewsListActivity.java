/**
 *
 */
package com.ys.www.asscg.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.item.newsItem;
import com.ys.www.asscg.util.Default;
import com.ys.www.asscg.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 网站公告
 */
public class NewsListActivity extends BaseActivity implements OnClickListener {

    private MyListView mListView;
    private LayoutInflater mInflater;
    private newsAdapter adapter;

    private int curPage, maxPage;
    private int pageCount = 5;
    public static List NewsList = new ArrayList<newsItem>();
    public static List NoticeList = new ArrayList<newsItem>();

    ImageView title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        title_right= findViewById(R.id.title_right);
        title_right.setVisibility(View.VISIBLE);
        title_right.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("公告");
        if (NewsList == null || NewsList.size() == 0) {
            NewsList.clear();
            final JSONObject jo = new JSONObject();
            try {
                jo.put("limit", pageCount);
                jo.put("page", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    doHttp(jo);
                }
            }.start();

        }

        initView();
        if (Default.showNewsList) {
            handler.sendEmptyMessage(3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                finish(0);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    protected void initView() {
        mListView = (MyListView) findViewById(R.id.listview);
        mInflater = LayoutInflater.from(this);
        adapter = new newsAdapter();
        mListView.setAdapter(adapter);
        mListView.setDividerHeight(0);
        mListView.showFootView(true);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(NewsListActivity.this, itemNewsActivity.class);
                Default.showNewsId = ((newsItem) NewsList.get(arg2 - 1)).getId();
                NewsListActivity.this.startActivity(intent);
            }
        });

        mListView.setOnLoadMoreInfo(new MyListView.LoadMoreInfo() {
            @Override
            public void onRefresh() {
                NewsList.clear();
                final JSONObject jo = new JSONObject();
                try {
                    jo.put("limit", pageCount);
                    jo.put("page", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        doHttp(jo);
                    }
                }.start();


            }

            @Override
            public void onLoadMore() {
                if (curPage + 1 <= maxPage) {
                    curPage++;
                    final JSONObject jo = new JSONObject();
                    try {
                        jo.put("limit", pageCount);
                        jo.put("page", curPage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            doHttp(jo);
                        }
                    }.start();

                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });

    }

    public void initData(JSONObject json) {
        JSONArray list = null;
        try {
            if (json.has("list") && !json.isNull("list")) {
                list = json.getJSONArray("list");
            }
            if (list != null) {
                for (int i = 0; i < list.length(); i++) {
                    JSONObject temp = list.getJSONObject(i);
                    newsItem item = new newsItem(temp);

                    NewsList.add(item);
                }
            }

            maxPage = json.getInt("totalPage");
            curPage = json.getInt("nowPage");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    class newsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (NewsList == null) {
                return 0;
            }
            return NewsList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.adapter_item_news, null);
            }
            {
                TextView type = (TextView) arg1.findViewById(R.id.name);
                TextView info = (TextView) arg1.findViewById(R.id.time);

                newsItem item = (newsItem) NewsList.get(arg0);

                type.setText(item.getName());
                info.setText(item.getTime());
            }
            return arg1;
        }
    }

    public void doHttp(JSONObject jo) {
        String url = Default.ip + Default.news;
        try {
            String s = HttpClient.post(url, jo.toString());
            if (!"".equals(s)) {
                Message message = new Message();
                message.what = 4;
                message.obj = str2json(s);
                handler.sendMessage(message);
            } else {
                showCustomToast("获取失败");
            }
        } catch (Exception e) {

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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mListView.onRefreshComplete();
                mListView.onLoadMoreComplete();
                showCustomToast("无更多数据！");
            }
            if (msg.what == 3) {
                Intent intent = new Intent(NewsListActivity.this, itemNewsActivity.class);
                intent.putExtra("id", Default.showNewsId);
                NewsListActivity.this.startActivity(intent);
                Default.showNewsList = false;
            }
            if (msg.what == 4) {
                mListView.onRefreshComplete();
                mListView.onLoadMoreComplete();
                JSONObject jo = (JSONObject) msg.obj;
                initData(jo);
            }
        }

    };
}
