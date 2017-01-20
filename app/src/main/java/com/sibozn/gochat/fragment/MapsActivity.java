//package com.sibozn.gochat.view.frgment;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.Projection;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.UiSettings;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.sibozn.gochat.R;
//
//public class MapsActivity extends AppCompatActivity
//        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private static final int REQUEST_LOCATION = 2;
//    private static final int MY_LOCATION_REQUEST_CODE = 10;
//    private static final String TAG = "MapsActivity";
//    private SupportMapFragment mapFragment;
//    private GoogleMap mMap;
//    private GoogleApiClient mGoogleApiClient;
//    private Location lastLocation;
//    private LatLng lastLocationLatLng;
//    private boolean isCircle;
//    private TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.e(TAG, "onCreate: ");
//        setContentView(R.layout.activity_maps);
////         Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        textView = (TextView) findViewById(R.id.textview);
////        mapFragment.newInstance(new GoogleMapOptions()
////                .compassEnabled(true)
////                .zoomControlsEnabled(true)
////                .zoomGesturesEnabled(true));
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        mGoogleApiClient.connect();
//        super.onResume();
//    }
//
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Log.e(TAG, "onMapReady: ");
//        mMap = googleMap;
////        // Add a marker in Sydney and move the camera
//        // latLng = new LatLng(-33.867, 151.206);
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
////                == PackageManager.PERMISSION_GRANTED) {
////            mMap.setMyLocationEnabled(true);
////        } else {
////            // Show rationale and request permission.
////            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_REQUEST_CODE);
////        }
//        mMap.setMyLocationEnabled(true);
////        mMap.setBuildingsEnabled(true);
//        UiSettings uiSettings = mMap.getUiSettings();
//        Log.e(TAG, "onMapReady1: " + uiSettings.isZoomControlsEnabled());
//        Log.e(TAG, "onMapReady2: " + uiSettings.isZoomGesturesEnabled());
//        uiSettings.setZoomControlsEnabled(true);// 缩放比例控件
//        uiSettings.setZoomGesturesEnabled(true);//手势
//        //uiSettings.setIndoorLevelPickerEnabled();//层级选取器
//        uiSettings.setMapToolbarEnabled(false);// 地图工具栏
////        uiSettings.setMyLocationButtonEnabled(false);//完全禁止定位按钮出现
//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {// My Location 按钮事件
//            @Override
//            public boolean onMyLocationButtonClick() {
//                Log.e(TAG, "点击mylocaltion");
//                return false;
//            }
//        });
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {// 地图标记事件
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Log.e(TAG, "onMarkerClick: ");
//                return false;
//            }
//        });
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {//信息窗口事件
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Log.e(TAG, "onInfoWindowClick: ");
//            }
//        });
////        Log.e("22222222222222", uiSettings.toString());
////        Projection projection = mMap.getProjection();
////        Log.e("11111111111111", projection.toString());
////        Location myLocation = mMap.getMyLocation();
//////        double latitude = myLocation.getLatitude();
//////         IndoorBuilding focusedBuilding = mMap.getFocusedBuilding();
//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {//摄像机改变监听
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                Log.e(TAG, "onCameraChange");
//                // cameraPosition = mMap.getCameraPosition();
//                textView.setText(cameraPosition.toString());
//            }
//        });
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle connectionHint) {
//        Log.e(TAG, "----------------------------------------onConnected");
//        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (lastLocation != null) {
//            String latString = String.valueOf(lastLocation.getLatitude());
//            String lngString = String.valueOf(lastLocation.getLongitude());
//            Log.e(TAG, "lat/lng:" + latString + lngString);
//            lastLocationLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//        } else {
//            lastLocationLatLng = new LatLng(-33.867, 151.206);
//        }
//        if (!isCircle) {
//            CircleOptions circleOptions = new CircleOptions()
//                    .center(lastLocationLatLng)
//                    .strokeWidth(2)
//                    // .strokeColor(getResources().getColor(R.color.colorPrimary))
//                    .fillColor(getResources().getColor(R.color.gray))
//                    .radius(2600); // In meters
//            Circle circle = mMap.addCircle(circleOptions);
//            mMap.addMarker(new MarkerOptions()
//                    .position(lastLocationLatLng)
//                    .title("title")
//                    .alpha(0.7f)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                    // .draggable(true)
//                    .snippet("snippet"));
//            isCircle = true;
//        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, 12.6f));
//        //mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocationLatLng));
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, 18));
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.e(TAG, "onConnectionSuspended");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.e(TAG, "onConnectionFailed");
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//            } else {
//                // Permission was denied. Display an error message.
//            }
//        }
//    }
//}
