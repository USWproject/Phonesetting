package com.ulan.timetable.GPS;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ulan.timetable.R;

public class ConfirmDialogActivity extends AppCompatActivity implements View.OnClickListener{

    //Geofencing Area에 대한 세부 정보를 전달하는 상수값 설정(위도, 경도, 범위, 장소명, 주소)
    public static final String GEO_LATITUDE = "geo_latitude";
    public static final String GEO_LONGITUDE = "geo_longitude";
    public static final String GEO_RADIUS = "geo_radius";
    public static final String GEO_NAME = "geo_name";
    public static final String GEO_ADDRESS = "geo_address";

    //결과값을 반환할 때 Constant가 activity를 결정
    public static final String FROM_CONFIRM_ACTIVITY = "fromConfirmActivity";

    // 장소명, 위도, 경도, 범위, 주소 사용자가 설정
    EditText etName, etLatitude, etLongitude,etRadius, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_activity_confirm_dialog);

        //취소 버튼
        Button button = findViewById(R.id.buttonCancel);
        button.setOnClickListener(this);
        //확인 버튼
        button = findViewById(R.id.buttonConfirm);
        button.setOnClickListener(this);


        etName = findViewById(R.id.editTextName);
        etLatitude = findViewById(R.id.editTextLatitude);
        etLongitude = findViewById(R.id.editTextLongitude);
        etRadius = findViewById(R.id.editTextRadius);
        etAddress = findViewById(R.id.editTextAddress);


        //DB에서 위도, 경도, 범위값 받아오기.(범위의 초기값: 100m), 장소명과 주소는 사용자가 입력
        Intent intent = getIntent();
        etName.setText(intent.getStringExtra(GEO_NAME));
        etLatitude.setText(String.valueOf(intent.getDoubleExtra(GEO_LATITUDE, 0.0)));
        etLongitude.setText(String.valueOf(intent.getDoubleExtra(GEO_LONGITUDE, 0.0)));
        etRadius.setText(String.valueOf(intent.getFloatExtra(GEO_RADIUS, 100.0f)));
        etAddress.setText(intent.getStringExtra(GEO_ADDRESS));
    }

    //함수로 data를 검증하고 MainActivity로 넘긴다.
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonCancel) {
            //취소 버튼을 클릭하면 아무 것도 할 필요가 없으며 결과만 확인한다.
            setResult(RESULT_CANCELED);
            finish();
        } else if(v.getId() == R.id.buttonConfirm){
            //위도값 설정
            String lat = etLatitude.getText().toString().trim();
            if(lat.equals("")){
                Toast.makeText(this, "위도값을 넣어주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            //경도값 설정
            String lng = etLongitude.getText().toString().trim();
            if (lng.equals("")) {
                Toast.makeText(this, "경도값을 넣어주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            //이름 부여하기
            String name = etName.getText().toString().trim();
            if (name.equals("")) {
                Toast.makeText(this, "이름을 넣어주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            //범위값 부여하기
            String rad = etRadius.getText().toString().trim();
            if (rad.equals("")|| rad.startsWith("-")||Float.parseFloat(rad)>2000) {
                Toast.makeText(this, "범위값을 설정해주세요(0m ~ 2000m)", Toast.LENGTH_SHORT).show();
                return;
            }
            String address = etAddress.getText().toString().trim();
            if (address.equals("")) {
                Toast.makeText(this, "주소는 넣어주는 것이 좋습니다.", Toast.LENGTH_SHORT).show();
            }
            Intent resultData = new Intent();
            resultData.putExtra(GEO_NAME, name)
                    .putExtra(GEO_LATITUDE,Double.parseDouble(lat))
                    .putExtra(GEO_LONGITUDE,Double.parseDouble(lng))
                    .putExtra(GEO_RADIUS,Float.parseFloat(rad))
                    .putExtra(GEO_ADDRESS,address)
                    .putExtra(Gps_MainActivity.ACTIVITY_FROM,FROM_CONFIRM_ACTIVITY);
            //If all data are valid return with the RESULT_OK and the collected data.
            setResult(RESULT_OK,resultData);
            finish();
        }
    }
}
