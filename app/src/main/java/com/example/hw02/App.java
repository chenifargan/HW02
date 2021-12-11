package com.example.hw02;

import android.app.Application;

import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MSPV.initHelper(this);

        String js = MSPV.getMe().getString("MY_DB", "");
        MyDB md = new Gson().fromJson(js, MyDB.class);

        if(md==null){
            MyDB myDB = new MyDB();
            String json = new Gson().toJson(myDB);
            MSPV.getMe().putString("MY_DB", json);
        }



    }
}
