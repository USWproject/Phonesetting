package com.ulan.timetable.GPS;

public class AutoSilenceLocation {
    //Geofence Location의 고유 ID
    private int id;
    //Geofence Location 이름(사용자 설정, 주소)
    private String name, address;
    //Geofence의 경도(Latitude) 위도(Longitude)
    private double lat;
    private double lng;
    //지정한 Geofence Location의 무음 설정 범위
    private float radius;
    //RequestID for the Geofences.
    private String requestId;

    AutoSilenceLocation(int id, String requestId, String name, double lat, double lng, float radius, String address) {
        this.name = name;
        this.requestId = requestId;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.radius = radius;
    }

    //사용자 설정 이름 받아오기
    public String getName() {
        return name;
    }

    //주소 받아오기
    public String getAddress() {
        return address;
    }

    //위도 받아오기
    public double getLat() {
        return lat;
    }

    //경도 받아오기
    public double getLng() {
        return lng;
    }

    //설정한 범위 받아오기
    public float getRadius() {
        return radius;
    }

    //Geofence Location 고유 ID 받아오기
    public int getId() {
        return id;
    }

    //RequestID for the Geofences 받아오기
    public String getRequestId() {
        return requestId;
    }
}
