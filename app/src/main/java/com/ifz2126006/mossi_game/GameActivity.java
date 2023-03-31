package com.ifz2126006.mossi_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Runnable{
    FrameLayout gamefield;
    TextView lblGameOver;
    TextView lblPoints;
    TextView lblRounds;
    Handler handler;
    Random randomgenerator;
    private float ruler;
    private int quantityMossis;
    private int goalMossis;
    private int round;

    private int points;

    private Date roundStartTime;
    private final static int timedround = 20;
    
    private long timeleft;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gamefield = findViewById(R.id.gamefield);
        ruler = getResources().getDisplayMetrics().density;
        randomgenerator = new Random();

        lblPoints = findViewById(R.id.Points);
        lblRounds = findViewById(R.id.Rounds);
        lblGameOver = findViewById(R.id.lblGameOver);
        lblGameOver.setVisibility(View.INVISIBLE);
        handler = new Handler();
        startRound();
        handler.postDelayed(this, 1000);

    }

    /**
     * Round Increments, quantity Mossis reset,
     */
    private void startRound(){
        round++;
        quantityMossis = 0;
        goalMossis = round * 10;
        roundStartTime= new Date();
        lblRounds.setText("Round:" + round);
    }

    public void decrementTime(){
        timeleft = timedround- (new Date().getTime() - roundStartTime.getTime())/1000;
        if(!gamefinished()){
            if (!roundfinished()){
                createMossi(null);
            } else{
                startRound();
            }
            handler.postDelayed(this, 1000);
        }else{
            ShowGameOver();
        }
    }

    /*
    true, if enough Mossis were caught in the round
    @return
     */
    private boolean roundfinished()
    {

        return quantityMossis >= goalMossis;
    }
/*
Will return true when the game time has ended and the quantity of Mossis is smaller than the goal Mossis
@return
 */

    private boolean gamefinished() {
        return timeleft <= 0 && quantityMossis < goalMossis;
    }

    public void createMossi(View v){
        ImageView mossi = new ImageView(this);
        mossi.setOnClickListener(view -> entferneMossi(view));
        mossi.setBackgroundResource(R.drawable.mosquito);
        int width = gamefield.getWidth();
        int height = gamefield.getHeight();
        int mosquito_width = Math.round(ruler * 50);
        int mosquito_height = Math.round(ruler * 42);
        int left = randomgenerator.nextInt(width - mosquito_width);
        int top = randomgenerator.nextInt(height - mosquito_height);

        FrameLayout.LayoutParams params = new
        FrameLayout.LayoutParams(mosquito_width, mosquito_height);

        new FrameLayout.LayoutParams(mosquito_width, mosquito_height);
        params.leftMargin = left;
        params.topMargin = top;
        params.gravity = Gravity.TOP+Gravity.LEFT;

        gamefield.addView(mossi, params);

    }

    /**
     * mossi gets removed and Points will be updated
     */
    private void entferneMossi(View v) {
        quantityMossis++;
        points = points + 10;
        gamefield.removeView(v);
        lblPoints.setText("Points: "+points);
    }

    private void ShowGameOver(){
        lblGameOver.setVisibility(View.VISIBLE);
        int quantityElemente = gamefield.getChildCount();
        for( int i = quantityElemente-1; i >  0; i --){
            if(gamefield.getChildAt(i).getClass().equals(TextView.class)){
               /* gamefield.removeView(
                        gamefield.getChildAt(i)
                );*/
                gamefield.getChildAt(i).setOnClickListener(null);
            }
            handler.postDelayed(() -> {
                setResult(points);
                finish();
            },3000);
            }
        }


    @Override
    public void run() {
        decrementTime();
    }
}