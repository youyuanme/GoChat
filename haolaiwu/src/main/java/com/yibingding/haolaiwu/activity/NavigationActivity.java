package com.yibingding.haolaiwu.activity;

import android.content.Intent;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.ybd.app.BaseActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.activity.baidumaplistener.MyLocationListener;
import com.yibingding.haolaiwu.activity.baidumaplistener.MyLocationListener.LocationResuteListener;
import com.yibingding.haolaiwu.tools.MyUtils;

public class NavigationActivity extends BaseActivity implements
		OnGetRoutePlanResultListener {
	private RoutePlanSearch mSearch = null;
	OverlayManager routeOverlay = null;
	RouteLine route = null;
	private MapView mMapView;
	private BaiduMap mBaidumap;

	@Override
	public void onCreateThisActivity() {

		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();

		Intent intent = getIntent();
		String latlon = intent.getStringExtra("latlon");
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		System.out.println("=======latlon=====" + latlon);
		try {
			String[] ll = latlon.split("#");
			enNode = PlanNode.withLocation(new LatLng(
					Double.parseDouble(ll[0]), Double.parseDouble(ll[1])));
			getLocation();
		} catch (Exception e) {
			MyUtils.showToast(this, "无效的地理位置信息。");
			return;
		}

	}

	public LocationClient mLocationClient = null;

	private void getLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	public void initViews() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(NavigationActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient = null;
		}

		super.onDestroy();
	}

	String mycity;
	public BDLocationListener myListener = new MyLocationListener(
			new LocationResuteListener() {
				@Override
				public void onResult(final BDLocation location) {

					LatLng arg0 = new LatLng(location.getLatitude(),
							location.getLongitude());
					PlanNode stNode = PlanNode.withLocation(arg0);
					mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
							stNode).to(enNode));
				}

				@Override
				public void onFault() {
					MyUtils.showToast(getApplicationContext(), "定位失败");
				}
			});
	private PlanNode enNode;

	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

}
