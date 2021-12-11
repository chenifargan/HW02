package com.example.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMap extends AppCompatActivity {
    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;

    private static CallBack_MYDB callBack_mydb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);

        fragmentList.setMyDB(callBack_mydb1.getMyDB());

        fragmentList.setCallBackList(callBack_list);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();


       fragmentMap = new Fragment_Map();
       fragmentMap.setActivity(this);

   getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();

    }

   CallBack_List callBack_list = new CallBack_List() {
       @Override
       public void location(double lat, double lon) {
            zoomOnMap(lat,lon);
       }


   };
    public static void setCallBack_myDB(CallBack_MYDB myDB){
        callBack_mydb1 = myDB;
    }

    private void zoomOnMap(double lat, double lon) {
        GoogleMap gm = fragmentMap.getGoogleMap();
        LatLng point = new LatLng(lat, lon);
        gm.addMarker(new MarkerOptions()
                .position(point)
                .title("* Crash Site * | Pilot Name: " ));
        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13.0f));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();
    }



}