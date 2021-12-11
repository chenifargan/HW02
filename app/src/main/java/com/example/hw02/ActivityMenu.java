package com.example.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Collections;

public class ActivityMenu extends AppCompatActivity {

    private MaterialButton menu_with_BTN;
    private MaterialButton menu_without_BTN;
    private MaterialButton menuRecordTableAndMap;
    private ImageView imageView;
    private MyDB myDBGame =new MyDB();
    private int scoreGame = 0;
    private TextInputLayout info;
    private Boolean ifItChange=false;
    private double latitude;
    private  double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initViews();



        menu_with_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(1);
            }
        });

        menu_without_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(0);
            }
        });

        menuRecordTableAndMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(2);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(callBack_score!=null){
            if(ifItChange){
                ifItChange=false;
                getScoreForGame();
            }
        }
    }

    private void getScoreForGame() {
        LocationServis  locationServis = new LocationServis(ActivityMenu.this);
        if(locationServis.canGetLocation){
            latitude = locationServis.getLatitude();
            longitude = locationServis.getLongitude();
            Toast.makeText(getApplicationContext(), "THE PLAYER SAVED", Toast.LENGTH_LONG).show();
        }
        else {
            locationServis.showSettingsAlert();
            latitude = locationServis.getLatitude();
            longitude = locationServis.getLongitude();

        }

        String md= MSPV.getMe().getString("MY_DB","");
        myDBGame = new Gson().fromJson(md,MyDB.class);
        String name = info.getEditText().getText().toString();


        myDBGame.getPlayers().add(new Player().setName(name).setScore(scoreGame).setLatitude(latitude).setLongitude(longitude));
        Collections.sort(myDBGame.getPlayers(),new SortedByScore());
        String json = new Gson().toJson(myDBGame);
        MSPV.getMe().putString("MY_DB", json);

    }


    private boolean isNameExist(){
        if(info.getEditText().getText().toString().equals("")){
            Toast.makeText(this, "ENTER NAME!", Toast.LENGTH_SHORT).show();
        return  false;

        }
        return true;
    }
    private void startGame(int num) {
        Intent myIntent = null;

        if (num == 1 || num == 0) {
            if(isNameExist()==true){
                myIntent = new Intent(this, GameActivity.class);
                GameActivity.SetCallBackScore(callBack_score);
                Bundle bundle = new Bundle();
                bundle.putInt("is sensor", num);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }

        }

        else if (num == 2) {
            myIntent = new Intent(this, ActivityMap.class);
            ActivityMap.setCallBack_myDB(callBack_mydb);
            startActivity(myIntent);
        }

    }
CallBack_Score callBack_score = new CallBack_Score() {
    @Override
    public void giveScore(int score) {
        scoreGame = score;
    }

    @Override
    public void ifChange(Boolean ifChange) {
        ifItChange = ifChange;
    }
};


CallBack_MYDB callBack_mydb = new CallBack_MYDB() {
    @Override
    public void setMyDB(MyDB myDB) {

        myDBGame = myDB;
    }

    @Override
    public MyDB getMyDB() {
        String md= MSPV.getMe().getString("MY_DB","");
        myDBGame = new Gson().fromJson(md,MyDB.class);
        return myDBGame;
    }
};

private void initViews(){
    menu_with_BTN = findViewById(R.id.menu_with_BTN);
    menu_without_BTN = findViewById(R.id.menu_without_BTN);
    menuRecordTableAndMap = findViewById(R.id.menuRecordTableAndMap);
    imageView = findViewById(R.id.bobspong);
    imageView.setVisibility(View.VISIBLE);
    info = findViewById(R.id.name);
    (new LocationServis(ActivityMenu.this)).showSettingsAlert();


}

}