package com.example.hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ImageView[][] path;
    private ImageView[] panel_BTN_LeftOrRight;
    private ImageView[] panel_IMG_hearts;
    private ImageView[] players;
    private int[][] values;
    private int playerPos =1;
    private Random rand = new Random();
    private final int MAX_LIVES = 3;
    private int lives = MAX_LIVES;
    private static CallBack_Score callBack_score1;
    private Handler handler;
    private MediaPlayer spongeBob_crying;
    private MediaPlayer spongeBob_laughing;
    private boolean updated = true ;
    private  int score = 0;
    private TextView info;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private int whichGame;

    public GameActivity(){};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        whichGame = getIntent().getExtras().getInt("is sensor");
        whichGame(whichGame);
        findViews();

        runGame();
    }

    private void whichGame(int num) {
        if(num==1){
            panel_BTN_LeftOrRight = new ImageView[]{
                    findViewById(R.id.buttonLeft),
                    findViewById(R.id.buttonRight)
            };

            panel_BTN_LeftOrRight[0].setVisibility(View.VISIBLE);
            panel_BTN_LeftOrRight[0].setImageResource(R.drawable.img_button_left);
            panel_BTN_LeftOrRight[1].setVisibility(View.VISIBLE);
            panel_BTN_LeftOrRight[1].setImageResource(R.drawable.img_button_right);

            buttonsListener();

        }
        else{
            initSensor();

        }

    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void findViews() {
        panel_IMG_hearts = new ImageView[]{
                findViewById(R.id.panel_IMG_heart1),
                findViewById(R.id.panel_IMG_heart2),
                findViewById(R.id.panel_IMG_heart3)
        };
        if (whichGame==1) {


            players = new ImageView[]{
                    findViewById(R.id.imageViewLeft),
                    findViewById(R.id.imageView),
                    findViewById(R.id.imageViewRight)
            };
            players[1].setVisibility(View.VISIBLE);
            path = new ImageView[][]{
                    {findViewById(R.id.demo_IMG_01), findViewById(R.id.demo_IMG_02), findViewById(R.id.demo_IMG_03)},
                    {findViewById(R.id.demo_IMG_11), findViewById(R.id.demo_IMG_12), findViewById(R.id.demo_IMG_13)},
                    {findViewById(R.id.demo_IMG_21), findViewById(R.id.demo_IMG_22), findViewById(R.id.demo_IMG_23)},
                    {findViewById(R.id.demo_IMG_31), findViewById(R.id.demo_IMG_32), findViewById(R.id.demo_IMG_33)}
            };
        }
        else{
            path = new ImageView[][]{
                    {findViewById(R.id.demo_IMG_00),findViewById(R.id.demo_IMG_01), findViewById(R.id.demo_IMG_02), findViewById(R.id.demo_IMG_03),findViewById(R.id.demo_IMG_04)},
                    {findViewById(R.id.demo_IMG_10),findViewById(R.id.demo_IMG_11), findViewById(R.id.demo_IMG_12), findViewById(R.id.demo_IMG_13),findViewById(R.id.demo_IMG_14)},
                    {findViewById(R.id.demo_IMG_20),findViewById(R.id.demo_IMG_21), findViewById(R.id.demo_IMG_22), findViewById(R.id.demo_IMG_23),findViewById(R.id.demo_IMG_24)},
                    {findViewById(R.id.demo_IMG_30),findViewById(R.id.demo_IMG_31), findViewById(R.id.demo_IMG_32), findViewById(R.id.demo_IMG_33),findViewById(R.id.demo_IMG_34)}
            };
            players =new ImageView[]{
                    findViewById(R.id.buttonLeft),
                    findViewById(R.id.imageViewLeft),
                    findViewById(R.id.imageView),
                    findViewById(R.id.imageViewRight),
                    findViewById(R.id.buttonRight)
            };
        }
        spongeBob_crying = MediaPlayer.create(GameActivity.this, R.raw.spongebobsquarepantswawawa);
        spongeBob_laughing =MediaPlayer.create(GameActivity.this,R.raw.spongeboblaughingsoundeffect);
        values = new int[path.length+1][path[0].length];
        info = findViewById(R.id.panel_LBL_score);
        if(callBack_score1!=null){
            callBack_score1.ifChange(false);

        }



    }

public static void SetCallBackScore(CallBack_Score callBack_score){
callBack_score1 = callBack_score;
}


    private void down() {
        for (int i = 0; i <values.length ; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if(values[i][j]!=0){
                    int temp = values[i][j];
                    values[i++][j]=0;
                    if(i<values.length){
                        values[i][j]=temp;

                    }
                    break;
                }

            }
        }


    }


    private void updateUI() {
        for (int i = 0; i < values.length-1; i++) {
            for (int j = 0; j < values[0].length ; j++) {
                ImageView im = path[i][j];
                if(values[i][j]==0){
                    im.setVisibility(View.INVISIBLE);
                }
                else if(values[i][j]==1){
                    im.setImageResource(R.drawable.img_squidward);
                    im.setVisibility(View.VISIBLE);
                }

                else if(values[i][j]==2){

                    im.setImageResource(R.drawable.img_star1);
                    im.setVisibility(View.VISIBLE);


                }
            }
        }
        if(raisingScore()==1){
            score+=100;
            info.setText("" +score);
            soundLaughing();

        }
        else if(crash()==1){
            lives--;
            if(score>=50){
                score -= 50;
                info.setText(""+score);


            }
            soundCrying();
            updateLivesViews();
            vibrate();
        }
        if(lives==0){
         //   Toast.makeText(this, "GAME OVER!!!!!", Toast.LENGTH_SHORT).show();
            if(whichGame==0){
                sensorManager.unregisterListener(accSensorEventListener);

            }
            Intent intent = new Intent(this, Activity_Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            if(callBack_score1!=null){
                callBack_score1.giveScore(score);
                callBack_score1.ifChange(true);
            }
            finish();
        }
    }

    private void soundCrying() {
        spongeBob_crying.start();
    }
    private void soundLaughing() {
        spongeBob_laughing.start();
    }
    private void startTicker() {
        handler = new Handler();
        handler.postDelayed(() -> {
            down();
            if (addObsticle()) {
                addObsticlevoid();
            }
            runGame();
        },1000);


    }

    void runGame(){
        if(lives>0){
            updateUI();
            startTicker();
        }
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            DecimalFormat df = new DecimalFormat("##.##");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(x<-4){
                move(1);
            }
            if(x>4){
                move(0);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };




    @Override
    protected void onResume() {
        super.onResume();
        if(whichGame==0){
            sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(whichGame==0){
            sensorManager.unregisterListener(accSensorEventListener);

        }
    }
    public boolean isSensorExist(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }
    private void buttonsListener(){
        for(int i = 0; i < panel_BTN_LeftOrRight.length; i++){
            final int buttonIndex = i;
            panel_BTN_LeftOrRight[i].setOnClickListener(v -> move(buttonIndex));
        }
    }
    private void move(int buttonIndex){
        if(buttonIndex == 0){
            if(playerPos > 0){
                players[playerPos--].setVisibility(View.INVISIBLE);
                players[playerPos].setVisibility(View.VISIBLE);


            }
        }else{
            if(playerPos < players.length-1){
                players[playerPos++].setVisibility(View.INVISIBLE);
                players[playerPos].setVisibility(View.VISIBLE);


            }
        }
    }
    private void updateLivesViews() {

        panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
    }
    private int crash(){
        if (lives > 0 ) {
            for (int M = 0; M < values[0].length; M++) {
                if (players[M].getVisibility() == View.VISIBLE && values[4][M] == 1) {
                    return 1;
                }
            }
        }

        return 0;
    }

    private int raisingScore(){
        if (lives > 0 ) {
            for (int M = 0; M < values[0].length; M++) {
                if (players[M].getVisibility() == View.VISIBLE && values[4][M] == 2) {
                    return 1;
                }
            }
        }

        return 0;
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void addObsticlevoid(){
        int randomNum = rand.nextInt(values[0].length);
        int randomNumForStarOrSquidward = rand.nextInt(3);
        values[0][randomNum]= randomNumForStarOrSquidward;
    }

    private boolean addObsticle(){
        for (int i = 0; i <2; i++) {
            for (int j = 0; j < values[0].length; j++) {
                if(values[i][j]!=0){
                    return false;
                }
            }

        }
        return true;
    }

    private void stopTicker(){
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    protected void onStart() {
        super.onStart();
        stopTicker();
        runGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }


}