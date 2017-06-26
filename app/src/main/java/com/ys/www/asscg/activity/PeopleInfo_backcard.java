package com.ys.www.asscg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.api.SystenmApi;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.http.HttpClient;
import com.ys.www.asscg.util.Default;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 第一次绑定银行卡
 */

public class PeopleInfo_backcard extends BaseActivity implements OnClickListener {
    private LayoutInflater mInflater;

    private Spinner mBank, mProvince, mCity;
    private EditText mOldCardNumber, mCardNumber, mBankName;

    private List<BankItem> mBankList = new ArrayList<BankItem>();
    private List<BankItem> mProvinceList = new ArrayList<BankItem>();
    private List<BankItem> mCityList = new ArrayList<BankItem>();

    private bankAdapter mBankAdapter;
    private provinceAdapter mProvinceAdapter;
    private cityAdapter mCityAdapter;

    private String strOldBankNum, str_sfz, str_dh, str_name, ip;// 卡号

    private String strBank;// 银行卡
    private String strBankNum;// 卡号
    private String strProvince;// 开户省
    private String strCity;// 城市
    private String strBranch;// 开户行
    private EditText ed_cardnumber_sx, et_sfz_sx, et_dh_sx, et_xm_sx;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_info_bind_bankcard);
        mInflater = LayoutInflater.from(this);

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("up", false);

        initView();

        dohttpBank();
    }

    public void initView() {

        ed_cardnumber_sx = (EditText) findViewById(R.id.ed_cardnumber_sx);
        et_sfz_sx = (EditText) findViewById(R.id.ed_sfz_sx);
        et_dh_sx = (EditText) findViewById(R.id.ed_dh_sx);
        et_xm_sx = (EditText) findViewById(R.id.ed_rn_sx);

        findViewById(R.id.title_right).setOnClickListener(this);
        findViewById(R.id.btn_tijiao).setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(isUpdate ? R.string.update_banckcard : R.string.bind_banckcard);

        // TextView text = (TextView) findViewById(R.id.textcard);
        // text.setText(isUpdate ? R.string.new_kahao : R.string.kahao);

        // findViewById(R.id.lay1).setVisibility(isUpdate ? View.VISIBLE :
        // View.GONE);

//		mCardNumber = (EditText) findViewById(R.id.ed_cardnumber);
        mBankName = (EditText) findViewById(R.id.ed_oldbank);
        mOldCardNumber = (EditText) findViewById(R.id.ed_oldcardnumber);

        mBank = (Spinner) findViewById(R.id.sp_chosebank_list);
        mProvince = (Spinner) findViewById(R.id.province_spinner);
        mCity = (Spinner) findViewById(R.id.city_spinner);

        mBankAdapter = new bankAdapter();
        mBank.setAdapter(mBankAdapter);

        mCityAdapter = new cityAdapter();
        mCity.setAdapter(mCityAdapter);

        mProvinceAdapter = new provinceAdapter();
        mProvince.setAdapter(mProvinceAdapter);
        mProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BankItem item = mProvinceList.get(arg2);
                dohttpCity(item.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_tijiao:
                // Boolean b = SystenmApi.isWifiConnected(PeopleInfo_backcard.this);
                // ip = SystenmApi.getIPStr(PeopleInfo_backcard.this, b);// ip
                // ip=SystenmApi。GetHostIp();
                ip = SystenmApi.getLocalIpAddress();
                strBank = "" + mBank.getSelectedItem();// 银行卡
                strBankNum = ed_cardnumber_sx.getText().toString();// 卡号
                strProvince = "" + mProvince.getSelectedItem();// 开户省
                strCity = "" + mCity.getSelectedItem();// 城市
                strBranch = mBankName.getText().toString();// 开户行
                str_sfz = et_sfz_sx.getText().toString();// 身份证
                str_dh = et_dh_sx.getText().toString();// 电话
                str_name = et_xm_sx.getText().toString();// 姓名
                if (!SystenmApi.isMobileNO(str_dh)) {
                    showCustomToast("请输入合法的手机号");
                    return;
                }
                // if (!SystenmApi.personIdValidation(str_sfz)) {
                // showCustomToast("请输入合法的身份证");
                // return;
                // }
                doHttp();
                break;
            case R.id.title_right:
                finish();
                break;
        }
    }

    /**
     * 获取银行列表 获取省份列表
     */
    private void dohttpBank() {
        String url = Default.ip + Default.getBankInfo;
        try {
            String s = HttpClient.post(url, null);
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    getBankInfo(response);
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

    /**
     * 根据省份id获取 城市列表
     *
     * @param id
     */
    private void dohttpCity(String id) {
        String url = Default.ip + Default.getCity;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", 0);
            jsonObject.put("id", id);
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    getCityInfo(response);
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

    /**
     * 解析获得数据
     *
     * @param json
     */
    public void getCityInfo(JSONObject json) {
        try {
            mCityList.clear();
            JSONArray citys = json.getJSONArray("city");

            for (int i = 0; i < citys.length(); i++) {
                JSONObject temp = citys.getJSONObject(i);
                BankItem item = new BankItem(temp);
                mCityList.add(item);
            }

            mCityAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    class BankItem {
        String id;
        String name;

        public BankItem(JSONObject json) {
            try {
                id = json.getString("id");
                name = json.getString("value");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getBankInfo(JSONObject json) {
        try {
            JSONArray banks = json.getJSONArray("bank");
            JSONArray province = json.getJSONArray("province");

            for (int i = 0; i < banks.length(); i++) {
                JSONObject temp = banks.getJSONObject(i);
                BankItem item = new BankItem(temp);
                mBankList.add(item);
            }

            for (int i = 0; i < province.length(); i++) {
                JSONObject temp = province.getJSONObject(i);
                BankItem item = new BankItem(temp);
                mProvinceList.add(item);
            }

            mBankAdapter.notifyDataSetChanged();
            mProvinceAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    class bankAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mBankList.size();
        }

        @Override
        public Object getItem(int position) {
            BankItem item = mBankList.get(position);
            return item.name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            BankItem item = mBankList.get(position);
            name.setText(item.name);
            return convertView;
        }

    }

    class provinceAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mProvinceList.size();
        }

        @Override
        public Object getItem(int position) {
            BankItem item = mProvinceList.get(position);
            return item.name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            BankItem item = mProvinceList.get(position);
            name.setText(item.name);
            return convertView;
        }

    }

    class cityAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mCityList.size();
        }

        @Override
        public Object getItem(int position) {
            BankItem item = mCityList.get(position);
            return item.name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            BankItem item = mCityList.get(position);
            name.setText(item.name);
            return convertView;
        }

    }

    public void doHttp() {
        String url = Default.ip + Default.peoInfobindbankcard_sx;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", 0);
            jsonObject.put("bankname", strBank);// 银行卡
            jsonObject.put("bankcard", strBankNum);// 卡号
            jsonObject.put("province", strProvince);// 开户省
            jsonObject.put("city", strCity);// 城市
            jsonObject.put("bankaddress", strBranch);// 开户行
            jsonObject.put("idcard", str_sfz);// 身份证
            jsonObject.put("mobile", str_dh);// 电话
            jsonObject.put("realname", str_name);// 姓名
            jsonObject.put("ip", ip);// 姓名
            String s = HttpClient.post(url, jsonObject.toString());
            if (!"".equals(s)) {
                JSONObject response = str2json(s);
                if (response.getInt("status") == 1) {
                    showCustomToast("信息绑定成功");
                    finish();
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
}
