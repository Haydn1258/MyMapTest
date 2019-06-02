package com.example.mymaptest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    // NaverMap API 3.0
    private MapView mapView;

    double latitude=37.5670135, longitude=126.9783740;
    TextView tv;
    ImageButton bt;
   




    // FusedLocationSource (Google)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    LocationManager lm;
    Marker marker = new Marker();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        tv = (TextView)findViewById(R.id.textView);
        bt = (ImageButton)findViewById(R.id.button);






        naverMapBasicSettings();
        getAddr();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Context ct = getApplicationContext();
                    GpsTracker gk = new GpsTracker(ct);
                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                0 );
                    }
                    else{
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        naverMapBasicSettings();
                        getAddr();

                }


                }catch (Exception e){

                }


            }

        });



    }
    public void getAddr(){
        final Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;

        try {

            list = geocoder.getFromLocation(latitude,longitude, 10); // 얻어올 값의 개수

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size()==0) {
                tv.setText("해당되는 주소 정보는 없습니다");
            } else {
                String sAddr = list.get(0).getAddressLine(0).toString();
                String sCountry = list.get(0).getCountryName().toString();
                sAddr = sAddr.substring(sAddr.lastIndexOf(sCountry)+sCountry.length()+1);

                tv.setText(sAddr);
            }
        }
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();



        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };




    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);


        //latitude = myLocation.getLatitude();
       // longitude = myLocation.getLongitude();
        //provider = myLocation.getProvider();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        naverMap.getUiSettings().setLocationButtonEnabled(false);

        marker.setMap(null);
        marker.setWidth(80);
        marker.setHeight(140);
        marker.setPosition(new LatLng(latitude, longitude));


        // Location Change Listener을 사용하기 위한 FusedLocationSource 설정
        naverMap.setLocationSource(locationSource);

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude));
        CameraUpdate cuZomm = CameraUpdate.zoomTo(18);
        naverMap.moveCamera(cuZomm);
        naverMap.moveCamera(cameraUpdate);
        marker.setMap(naverMap);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }




}
