package com.ys.www.asscg.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.item.newsItem;
import com.ys.www.asscg.util.Default;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class itemNewsActivity extends BaseActivity implements OnClickListener {
    public static String source; // =
    private TextView name;
    private TextView time;
    private newsItem mItem;
    private WebView webview;
    public static JSONObject noticeListItemJson;
    ImageView title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        name = (TextView) findViewById(R.id.newsName);
        time = (TextView) findViewById(R.id.newsTime);

        title_right= findViewById(R.id.title_right);
        title_right.setVisibility(View.VISIBLE);
        title_right.setOnClickListener(this);

        TextView text = (TextView) findViewById(R.id.title);
        text.setText(R.string.newsTitle);

        webview = (WebView) findViewById(R.id.web);
        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        new Thread() {
            @Override
            public void run() {
                super.run();
                if (Default.IS_SHOW_NESW_OR_NOTICE) {
                    doShowItemhttp(Default.showNoticeId);
                } else {
                    doShowItemhttp(Default.showNewsId);
                }
            }
        }.start();
    }

    private String getFormateHtml(String html) {

        String retStr = "<html><head><style type='text/css'>p{text-align:justify;border-style:" + " none;border-top-width: 2px;border-right-width: 2px;border-bottom-width: 2px;border-left-width: 2px;}" + "img{height:auto;width: auto;width:100%;}</style></head><body>" + html + "</body></html>";

        return retStr;

    }

    private void updateData(JSONObject json) {
        try {

            name.setText(json.getString("title"));
            time.setText(json.getString("art_time"));
            source = json.getString("art_content");

            // doText();

            webview.loadDataWithBaseURL(Default.ip, getFormateHtml(source), "text/html", "utf-8", null);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public void doText() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageGetter getter = new ImageGetter() {

                    @Override
                    public Drawable getDrawable(String source) {
                        Drawable drawable = null;
                        try {
                            URL url = new URL(Default.ip + source);
                            String srcName = source.substring(source.lastIndexOf("/") + 1, source.length());
                            InputStream is = null;
                            try {
                                is = url.openStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            drawable = Drawable.createFromStream(is, srcName);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        return drawable;
                    }
                };
                Spanned spanned = Html.fromHtml(source, getter, null);
                Map map = new HashMap<String, Object>();
                map.put("spanned", spanned);
                map.put("info", source);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = map;
                handler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    public void finish() {
        Default.IS_SHOW_NESW_OR_NOTICE = false;
        super.finish();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    HashMap map = (HashMap) msg.obj;

                    Spanned getter = (Spanned) map.get("spanned");
                    // info.setText(getter);
                    break;
                case 3:
                    updateData(noticeListItemJson);
                    break;
            }
        }

    };

    // private void doShowItemhttp(long id)
    // {
    // showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
    //
    // JsonBuilder builder = new JsonBuilder();
    // builder.put("id", id);
    // new BaseModel(null, Default.newsListItem, builder)
    // .setConnectionResponseLinstener(new ConnectResponseListener()
    // {
    // public void onConnectResponseCallback(JSONObject json)
    // {
    // dismissLoadingDialog();
    // Data.noticeListItemJson = json;
    // handler.sendEmptyMessage(3);
    // }
    //
    // @Override
    // public void onFail(JSONObject json)
    // {
    // dismissLoadingDialog();
    // try
    // {
    // Message msg = new Message();
    // msg.arg1 = 2;
    // Bundle bundel = new Bundle();
    // bundel.putString("info", json.getString("message"));
    // msg.setData(bundel);
    // handler.sendMessage(msg);
    // }
    // catch (JSONException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // });
    //
    // }
    private void doShowItemhttp(long id) {
        String url = Default.ip + (Default.IS_SHOW_NESW_OR_NOTICE ? Default.noticeListItem : Default.newsListItem);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 0);
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject json = str2json(s);
                noticeListItemJson = json;
                handler.sendEmptyMessage(3);
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
}
