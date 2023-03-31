package com.ifz2126006.mossi_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Runnable{

    private LinearLayout nameInputDialog;
    public final String LOGTAG = "MainActivity";
    Handler handy;
    final static int DELAY = 1000;
    private Button button;
    private static final int GAMEACTIVITY= 40;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handy = new Handler();
        handy.postDelayed(this, DELAY );
        button = (Button) findViewById(R.id.button);
        nameInputDialog = findViewById(R.id.nameInputDialog);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openactivity2();
            }
        });

    }

    private void openactivity2() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, GAMEACTIVITY);
    }
    @Override
    protected void onActivityResult(int requestId, int result, Intent i) {
        super.onActivityResult(requestId, result, i);

        Log.d("REQUEST ID", String.valueOf(requestId));
        Log.d("RESULT", String.valueOf(result));
        if(result == RESULT_OK){
            score= i.getIntExtra("Score", -1);
            if(score > 0){

            }

        }

    }

    public void saveButtonClicked(){
        TextView editTextname = findViewById(R.id.editTextTextPersonName2);
        String name = editTextname.getText().toString();
        //TODO: Validierung
        writeScore(name,score);
        //TODO: Dialog wieder unsichtbar machen
        nameInputDialog.setVisibility(View.VISIBLE);
        


    }

    private void writeScore(String name, int score) {
        (new Thread(() -> {


            try {
                URL url = new URL("https://green-orca.com/wiss/mossigame/");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                BufferedOutputStream buffy = new BufferedOutputStream(
                        conn.getOutputStream()
                );
                String params = "name=" + URLEncoder.encode(name, "UTF8")
                        + "&score=" + score;

                buffy.write(params.getBytes(StandardCharsets.UTF_8));
                buffy.flush();
                buffy.close();

                InputStreamReader easy = new InputStreamReader(conn.getInputStream());
                BufferedReader bReader = new BufferedReader(easy);
                while ((easy.ready())) {
                    String line = bReader.readLine();
                    Log.d("MAIN", "EASY; " + line);
                }

            } catch (MalformedURLException e) {

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        })).start();
    }
    /**
     * refreshes the systemclock on the display
     */
    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
        TextView txt = findViewById(R.id.textView);
        txt.setText(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));
        handy.postDelayed(this, DELAY);
    }
    public void closeActivity(View v){
        finish();
    }
}