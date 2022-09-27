package com.ulan.timetable.GPS;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ulan.timetable.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    public static final String FROM_MAPS_ACTIVITY = "fromMapsActivity";
    private GoogleMap mMap;
    private Marker marker;
    private Button button;
    private EditText editText;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_activity_maps);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주의");
        builder.setMessage("장소를 입력할시에는 정확히 입력해주세요! 어플이 인식을 못 할경우 에러가 발생합니다!\n\n" +
                "실내로 설정할 지역을 클릭하여 마크로 표시한뒤 추가하기를 눌러주세요!\n마크가 없는상태에서 추가하기를 누를시 에러가 발생합니다!");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.mapSelect) {
                if (marker != null) {
                    setResult(RESULT_OK, new Intent().putExtra(ConfirmDialogActivity.GEO_LATITUDE, marker.getPosition().latitude)
                            .putExtra(ConfirmDialogActivity.GEO_LONGITUDE, marker.getPosition().longitude)
                            .putExtra(Gps_MainActivity.ACTIVITY_FROM, FROM_MAPS_ACTIVITY));
                }
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, Log.getStackTraceString(e), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // googlemap 종류 설정 - 위성지도, 일반 구글 지도 등등 있음. Map_Type_****으로 설정하면 된다!
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng DEFAULT_LOCATION = new LatLng(37.21, 126.9754);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        drawAreas(googleMap);

        geocoder = new Geocoder(this);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();
                if(str.equals("")){
                    Toast.makeText(MapsActivity.this, "장소를 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(str,10); // 주소, 최대 검색 결과 개수
                    if(addressList.get(0).toString().equals(null)){
                        Toast.makeText(MapsActivity.this, "장소를 다시 입력하세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "장소를 다시 입력하세요!", Toast.LENGTH_SHORT).show();
                }

                    String[] splitStr = addressList.get(0).toString().split(",");
                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(point).draggable(true));
            }
        });
    }

    private void drawAreas(GoogleMap gMap) {
        LocationDBHelper dbHelper = new LocationDBHelper(this);
        ArrayList<AutoSilenceLocation> locations = dbHelper.getAllData();
        for (int i = 0; i < locations.size(); i++) {
            AutoSilenceLocation location = locations.get(i);
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(location.getLat(), location.getLng()))
                    .clickable(false)
                    .radius(location.getRadius())
                    .fillColor(Color.argb(150, 200, 200, 200))
                    .strokeColor(Color.RED)
                    .strokeWidth(2);
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(location.getName())
                    .draggable(false)
                    .position(new LatLng(location.getLat(), location.getLng()))
                    .snippet(location.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            gMap.addMarker(markerOptions);
            gMap.addCircle(circleOptions);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(this, String.format(Locale.ENGLISH, "Lat=%.4f\nLng=%.4f", marker.getPosition().latitude, marker.getPosition().longitude), Toast.LENGTH_SHORT).show();
    }
}
