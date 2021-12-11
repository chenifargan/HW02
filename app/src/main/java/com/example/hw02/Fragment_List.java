package com.example.hw02;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Fragment_List extends Fragment {
    private MaterialButton[][] listTable;
    private AppCompatActivity activity;
    private CallBack_List callBack_list;
    private MyDB myDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        addNameAndScore();
          initViews();


        return view;
    }

    private void initViews() {
        ArrayList<Player> players = myDB.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Player tempPlayer = players.get(i);
            listTable[i][0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callBack_list.location(tempPlayer.getLatitude(), tempPlayer.getLongitude());
                }
            });
            listTable[i][1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.location(tempPlayer.getLatitude(), tempPlayer.getLongitude());
                }
            });


        }
    }


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }



    public void setCallBackList(CallBack_List callBackList1) {
        this.callBack_list = callBackList1;
    }


    private void findViews(View view) {

        listTable = new MaterialButton[][]{

            { view.findViewById(R.id.tabelName1), view.findViewById(R.id.tabelScore1)},
            { view.findViewById(R.id.tabelName2), view.findViewById(R.id.tabelScore2)},
            { view.findViewById(R.id.tabelName3), view.findViewById(R.id.tabelScore3)},
            { view.findViewById(R.id.tabelName4), view.findViewById(R.id.tabelScore4)},
            { view.findViewById(R.id.tabelName5), view.findViewById(R.id.tabelScore5)},
            { view.findViewById(R.id.tabelName6), view.findViewById(R.id.tabelScore6)},
            { view.findViewById(R.id.tabelName7), view.findViewById(R.id.tabelScore7)},
            { view.findViewById(R.id.tabelName8), view.findViewById(R.id.tabelScore8)},
            { view.findViewById(R.id.tabelName9), view.findViewById(R.id.tabelScore9)},
            { view.findViewById(R.id.tabelName10), view.findViewById(R.id.tabelScore10)}
        };



    }
    public void setMyDB(MyDB myDB1){
        myDB = myDB1;


    }



    public  void addNameAndScore(){
        ArrayList<Player> palPlayers = myDB.getPlayers();
        for (int i = 0; i < palPlayers.size(); i++) {

            listTable[i][0].setText(palPlayers.get(i).getName());
            listTable[i][1].setText(String.valueOf(palPlayers.get(i).getScore()));
        }


        }

}
