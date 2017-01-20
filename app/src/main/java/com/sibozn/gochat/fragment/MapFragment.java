package com.sibozn.gochat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sibozn.gochat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment
        implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LAT_STRING = "latString";
    private static final String LNG_STRING = "lngString";
    private static final String TAG = "MapFragment";

    // TODO: Rename and change types of parameters
    private String latString;
    private String lngString;

    private OnFragmentInteractionListener mListener;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private TextView textView;
    private Context mContext;
    private LatLng lastLocationLatLng;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(LAT_STRING, param1);
        args.putString(LNG_STRING, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        if (getArguments() != null) {
            latString = getArguments().getString(LAT_STRING);
            lngString = getArguments().getString(LNG_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootvView = inflater.inflate(R.layout.fragment_map, container, false);
//        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.my_container, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
        textView = (TextView) rootvView.findViewById(R.id.textview);
        return rootvView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady: ");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
//        mMap.setBuildingsEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        Log.e(TAG, "onMapReady1: " + uiSettings.isZoomControlsEnabled());
        Log.e(TAG, "onMapReady2: " + uiSettings.isZoomGesturesEnabled());
        uiSettings.setZoomControlsEnabled(true);// 缩放比例控件
        uiSettings.setZoomGesturesEnabled(true);//手势
        //uiSettings.setIndoorLevelPickerEnabled();//层级选取器
        uiSettings.setMapToolbarEnabled(false);// 地图工具栏
//        uiSettings.setMyLocationButtonEnabled(false);//完全禁止定位按钮出现
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {// My Location 按钮事件
            @Override
            public boolean onMyLocationButtonClick() {
                Log.e(TAG, "点击mylocaltion");
                return false;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {// 地图标记事件
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e(TAG, "onMarkerClick: ");
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {//信息窗口事件
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e(TAG, "onInfoWindowClick: ");
            }
        });
//        Log.e("22222222222222", uiSettings.toString());
//        Projection projection = mMap.getProjection();
//        Log.e("11111111111111", projection.toString());
//        Location myLocation = mMap.getMyLocation();
////        double latitude = myLocation.getLatitude();
////         IndoorBuilding focusedBuilding = mMap.getFocusedBuilding();
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {//摄像机改变监听
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChange");
                // cameraPosition = mMap.getCameraPosition();
                textView.setText(cameraPosition.toString());
            }
        });
        lastLocationLatLng = new LatLng(Double.parseDouble(latString), Double.parseDouble(lngString));
        CircleOptions circleOptions = new CircleOptions()
                .center(lastLocationLatLng)
                .strokeWidth(2)
                // .strokeColor(getResources().getColor(R.color.colorPrimary))
                .fillColor(getResources().getColor(R.color.gray))
                .radius(2600); // In meters
        Circle circle = mMap.addCircle(circleOptions);
        mMap.addMarker(new MarkerOptions()
                .position(lastLocationLatLng)
                .title("title")
                .alpha(0.7f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                // .draggable(true)
                .snippet("snippet"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, 12.6f));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
