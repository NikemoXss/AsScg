package com.ys.www.asscg.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ys.www.asscg.R;
import com.ys.www.asscg.api.SystenmApi;
import com.ys.www.asscg.base.BaseActivity;
import com.ys.www.asscg.util.Default;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ContactUsActivity extends BaseActivity implements OnClickListener, SensorEventListener {
	ImageView title_right;
	TextView title, verson_sx;
	private LocationMode mCurrentMode;
	public MyLocationListenner myListener = new MyLocationListenner();
	LocationClient mLocClient;
	boolean isFirstLoc = true; // 是否首次定位
	private MyLocationData locData;
	private int mCurrentDirection = 0;
	private double mCurrentLat = 0.0;
	private double mCurrentLon = 0.0;
	private float mCurrentAccracy;
	private Double lastX = 0.0;
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 在地图中显示一个信息窗口，可以设置一个View作为该窗口的内容，也可以设置一个 BitmapDescriptor 作为该窗口的内容。
	private InfoWindow mInfoWindow;

	// 这三个就是地图上的小图标背景，是两种颜色同等大小的图片，一红一绿，他们两个交互显示就是闪烁的效果。不用时及时 recycle
	BitmapDescriptor icBig = BitmapDescriptorFactory.fromResource(R.drawable.icon_markab);
	BitmapDescriptor icSmall = BitmapDescriptorFactory.fromResource(R.drawable.icon_markas);
	BitmapDescriptor mBitmapDescriptorMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);

	String[] loca;
	String who = "test";
	Map<String, Integer> map = new HashMap<String, Integer>();

	Timer timer;
	int blinked;
	public static final int DISPALY_TIME = 1200;
	public static final int BLINK_INTERVAL = 300;
	public static final int BLINK_DELAY = 1000;
	public static final int ZOOM_SIZE = 14;

	// 119.985915,31.756729

	double baseLat1 = 31.756729;
	double baseLong1 = 119.985915;
	MarkerOptions marker, marker2;
	Marker mk;

	boolean customerMode = true;
	int zindex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactus);
		title_right = (ImageView) findViewById(R.id.title_right);
		findViewById(R.id.company_site_sx).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title_right.setVisibility(View.VISIBLE);
		title.setVisibility(View.VISIBLE);
		title.setText("联系我们");
		title_right.setOnClickListener(this);
		verson_sx = (TextView) findViewById(R.id.verson_sx);
		verson_sx.setText("版本号" + Default.curVersion);

		findViewById(R.id.bt_back).setOnClickListener(this);
		mCurrentMode = LocationMode.NORMAL;

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);// 设置地图的缩放级别
		mBaiduMap.setMapStatus(msu);//

		// 添加地图加载监听器 ，
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// 地图加载完，刷新小图标baseLong1
				timer = new Timer();
//				 startBlink(getLatLng(baseLat1 + "", baseLong1 + ""));
				setMapCenter(getLatLng(mCurrentLat + "", mCurrentLon + ""));
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());// 新建一个按钮，这个就是点击小图标后显示的view
				button.setBackgroundResource(R.drawable.popup);// 给按钮设置背景
				OnInfoWindowClickListener listener = null;
				String all = marker.getTitle();
				String name = who;
				button.setText(name);// 设置点击后显示的文字
				button.setTextColor(getResources().getColor(R.color.black));
				listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
						LatLng ll = marker.getPosition();
						marker.setPosition(ll);
						// 当前的图标隐藏掉(这个隐藏是全隐藏掉，没有找到只隐藏掉文字的直接方法，通过先隐藏再显示间接隐藏了文字)
						mBaiduMap.hideInfoWindow();
					}
				};
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// mCurrentLat = location.getLatitude();
			// mCurrentLon = location.getLongitude();
			
			mCurrentLat = baseLat1;
			mCurrentLon = baseLong1;
			mCurrentAccracy = location.getRadius();
			locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentDirection).latitude(baseLat1).longitude(baseLong1)
					.build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(baseLat1, baseLong1);
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void startBlink(LatLng mlatlng) {
		marker = new MarkerOptions().position(mlatlng).icon(icBig).zIndex(zindex++);// 设置zindex是为图标设置一个id，为以后获取他
		marker.title(who);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (blinked > DISPALY_TIME) {
					timer.cancel();
					mk.remove();// 小图标移除
				} else {
					if (blinked <= 0) {
						mk = (Marker) mBaiduMap.addOverlay(marker);
					} else {
						if (blinked % 2 == 1) {
							mk.setIcon(icSmall);
						} else {
							mk.setIcon(icBig);
						}
					}
					blinked++;
				}

			}
		}, BLINK_DELAY, BLINK_INTERVAL);

	}

	public LatLng getLatLng(String latitude, String longtitude) {

		LatLng llA = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
		return llA;

	}

	public void freshMap(String lat, String lon, String who) {
		startBlink(getLatLng(lat, lon));
		if (customerMode) {
			setMapCenter(getLatLng(lat, lon));
		}
	}

	public void setMapCenter(LatLng ll) {
		MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(ZOOM_SIZE).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			timer.cancel();
			break;
		case R.id.company_site_sx:
			ActivityInfo activityInfo = SystenmApi.getBrowserApp(getApplicationContext());
			if (activityInfo != null) {
				Uri uri = Uri.parse(Default.yu);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.setClassName(activityInfo.packageName, activityInfo.name);
				startActivity(intent);
			} else {
				showCustomToast("手机没有浏览器，无法访问");
			}

			break;
		case R.id.bt_back:
			mCurrentMode = LocationMode.FOLLOWING;
			mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
			mCurrentMode = LocationMode.NORMAL;
			mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		super.onDestroy();
		mMapView.onDestroy();
		icBig.recycle();
		icSmall.recycle();
		mBitmapDescriptorMarker.recycle();
		timer.cancel();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// TODO Auto-generated method stub

		double x = sensorEvent.values[SensorManager.DATA_X];
		if (Math.abs(x - lastX) > 1.0) {
			mCurrentDirection = (int) x;
			locData = new MyLocationData.Builder().accuracy(mCurrentAccracy)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentDirection).latitude(mCurrentLat).longitude(mCurrentLon).build();
			mBaiduMap.setMyLocationData(locData);
		}
		lastX = x;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
