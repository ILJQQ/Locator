package com.jikexueyuan.locator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    public LocationClient locationClient;
    boolean ifFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initLocation();
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        List<String> list = locationManager.getAllProviders();
//        String provider;
//        if (list.contains(LocationManager.NETWORK_PROVIDER)) {
//            provider = LocationManager.NETWORK_PROVIDER;
//
//        }else if (list.contains(LocationManager.GPS_PROVIDER)){
//            provider = LocationManager.GPS_PROVIDER;
//        } else {
//            Toast.makeText(this, "当前不能提供位置信息", Toast.LENGTH_LONG).show();
//            return;
//        }
////        检测provider的值
//        Toast.makeText(this, "您正在使用 " + provider +" 提供定位服务", Toast.LENGTH_LONG).show();
//        final Location location = locationManager.getLastKnownLocation(provider);
////        检测坐标准确性
//        Toast.makeText(this, "您的坐标是: "+ String.valueOf(location.getLatitude()) +", "
//                + String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
//        if (location != null) {
//            navigateTo(location);
//        }
//
//        locationManager.requestLocationUpdates(provider, 5000, 1,
//                locationListener);
    }

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locationData);

            if (ifFirst) {
                ifFirst=false;
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll,18.0f);
                baiduMap.animateMapStatus(mapStatusUpdate);
            }
        }
    };


    private void initLocation(){

        mapView = (MapView) findViewById(R.id.mvMap);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        baiduMap.setMyLocationEnabled(true);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
        locationClient.start();
    }

//    private void navigateTo(Location location) {
//        // 按照经纬度确定地图位置
//        if (ifFirst) {
//            LatLng ll = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            // 移动到某经纬度
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomBy(5f);
//            // 放大
//            baiduMap.animateMapStatus(update);
//
//            ifFirst = false;
//        }
//        // 显示个人位置图标
//        MyLocationData.Builder builder = new MyLocationData.Builder();
//        builder.latitude(location.getLatitude());
//        builder.longitude(location.getLongitude());
//        MyLocationData data = builder.build();
//        baiduMap.setMyLocationData(data);
//    }
//
//    LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onProviderEnabled(String arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onProviderDisabled(String arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onLocationChanged(Location arg0) {
//            // TODO Auto-generated method stub
//            // 位置改变则重新定位并显示地图
//            navigateTo(arg0);
//        }
//    };
//


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        super.onDestroy();
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }
}
