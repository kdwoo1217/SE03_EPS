package com.software.pld.pld;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MapView.POIItemEventListener {
    private MapView mapView;

    private LocationChecker locationChecker;
    private JSONArray data;
    private ArrayList<ParkingLot> parkingLots = new ArrayList<ParkingLot>();

    public boolean canGetParkingLot = false;

    public void updateParkingLot() {
        new AsyncTask<Object, Object, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Object... params) {
                try {
                    URL url = new URL("http://10.210.97.132:5000/getParkingInfo/?location=1");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONArray(json.toString());

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject temp = data.getJSONObject(i);

                        parkingLots.add(new ParkingLot(temp.getString("name"), new Float(temp.getString("x")), new Float(temp.getString("y")),
                                temp.getInt("totalSpace"), temp.getInt("currentSpace"), temp.getInt("isFree"), temp.getString("note")));
                    }

                } catch (Exception e) {
                    System.out.println("Exception "+ e.getMessage());
                }
                canGetParkingLot = true;
                showPoint();
                return null;
            }

            @Override
            protected void onPostExecute(String Void) {
                if(data!=null){
                    Log.d("my parkingLot received",data.toString());
                }
            }
        }.execute();
    }



    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.e("MapScreenAsyncTask", "onCalloutBalloonOfPOIItemTouched");
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        for(ParkingLot parkingLot : parkingLots)
        {
            if(mapPOIItem.getItemName().equals(parkingLot.getName()))
            {
                // start(lat,lon)
                double lat = parkingLot.getLatitude();
                double lot = parkingLot.getLongtitude();

                Intent intent = new Intent(getApplicationContext() , ParkingInfoActivity.class);
                intent.putExtra("NoWeather", false);
                intent.putExtra("Name", parkingLot.getName());
                intent.putExtra("Latitude", parkingLot.getLatitude());
                intent.putExtra("Longitude", parkingLot.getLongtitude());
                intent.putExtra("Total", parkingLot.getTotalSpace());
                intent.putExtra("Current", parkingLot.getCurrentSpace());
                intent.putExtra("isFree", parkingLot.getIsFree());
                intent.putExtra("Note", parkingLot.getNote());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Log.e("MapScreenAsyncTask", "onCalloutBalloonOfPOIItemTouched");
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        Log.e("MapScreenAsyncTask", "onDraggablePOIItemMoved");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationChecker = new LocationChecker(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("8caa92bb7e375e0dc675d5c2ca3091f6");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        mapView.setPOIItemEventListener(this);

        updateParkingLot();

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        if(grantExternalLocationPermission()) {
            if (checkGpsService()) {
                if (locationChecker.canGetLocation()) {
                    locationChecker.stopUsingGPS();
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(locationChecker.getLatitude(), locationChecker.getLongitude()), true);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        if(grantExternalLocationPermission() && checkGpsService()) {
            locationChecker.updateLocation();
            while(!locationChecker.canGetLocation()) {

            }
            if(locationChecker.canGetLocation()) {
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(locationChecker.getLatitude(), locationChecker.getLongitude()), true);
            }
        }
        super.onResume();
    }

    private boolean grantExternalLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return false;
            }
        }else{
            return true;
        }
    }

    private boolean checkGpsService() {
        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            }).create().show();
            return false;
        } else {
            return true;
        }
    }

    private void showPoint() {
        int i = 0;
        for(ParkingLot temp : parkingLots) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(temp.getName());
            marker.setTag(i);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(temp.getLatitude(), temp.getLongtitude()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);
            i++;
        }
    }
}
