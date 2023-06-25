package com.oss.blockwindow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class ResultActivity extends AppCompatActivity {

    Button bt_replay;
    TextView tv_finalScore;
    TextView tv_finalCoin;
    TextView tv_totalCoin;
    TextView tv_rankId;
    TextView tv_rankScore;
    TextView tv_highScore;
    EditText et_id;
    Button bt_save;

    Button bt_main;

    int score;
    int coin;

    SharedPreferences spf;
    SharedPreferences.Editor editor2;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        bt_replay=findViewById(R.id.btn_replay);
        tv_finalScore=findViewById(R.id.tv_finalScore);
        tv_finalCoin=findViewById(R.id.tv_finalCoin);
        bt_main = findViewById(R.id.bt_main);
        tv_highScore = findViewById(R.id.tv_HighScore);
        tv_totalCoin = findViewById(R.id.tv_totalCoin);


        score = getIntent().getIntExtra("score",-1);
        coin = getIntent().getIntExtra("coin", 0);

        CoinStack coinStack = new CoinStack(this);
        coinStack.setCoin(coinStack.getCoin()+coin);

        HighScoreStack scoreStack = new HighScoreStack(this);
        int temp = scoreStack.getHighScore();
        if(score > temp) {
            scoreStack.setHighscore(score);
        }

        tv_finalScore.setText(String.valueOf(score));
        tv_finalCoin.setText(String.valueOf(coin));
        tv_highScore.setText(String.valueOf(scoreStack.getHighScore()));
        tv_totalCoin.setText(String.valueOf(coinStack.getCoin()));


        bt_replay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}