package com.oss.blockwindow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import android.util.Log;


public class GameActivity extends AppCompatActivity {

    Handler feverEndHandler;
    Thread thread = null;
    TextView coin;
    TextView time;
    TextView score;
    ImageView[] imgViewArr = new ImageView[16];
    ImageView[] lifeViewArr = new ImageView[5];
    ImageView[] coinViewArr = new ImageView[1];

    int[] imageId = {R.id.card01, R.id.card02, R.id.card03, R.id.card04, R.id.card05, R.id.card06, R.id.card07, R.id.card08, R.id.card09, R.id.card10, R.id.card11, R.id.card12, R.id.card13, R.id.card14, R.id.card15, R.id.card16};
    int[] imagelifeID = {R.id.life1, R.id.life2, R.id.life3, R.id.life4, R.id.life5};
    int[] imageCoinID = {R.id.coin_img};

    public static final int ran[] = {R.drawable.up_bug1, R.drawable.up_bird1, R.drawable.up_bug2, R.drawable.coin};
    int sc = 0, plusScore = 100;
    int cn = 0, plusCoin = 50;
    int item;
    int lifeCount = 5; // 라이프 개수 변수 추가

    final String TAG_Bug1 = "bug1";
    final String TAG_Bird = "bird";
    final String TAG_Bug2 = "bug2";
    final String TAG_Coin = "coin";

    boolean isInFever = false;
    int feverLevel = 0;
    final String TAG_Empty = "empty";
    int feverDuration = 0;
    int bugCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        item = getIntent().getIntExtra("whichItem",-1);
        if(item == 0) {
            plusScore = 200;
        } if (item == 1) {
            plusCoin = 250;
        }

        coin = findViewById(R.id.coin_tv);
        coin.setText("0");
        feverEndHandler = new Handler();
        time = findViewById(R.id.time_tv);
        score = findViewById(R.id.score_tv);

        for (int i = 0; i < lifeCount; i++) {
            lifeViewArr[i] = (ImageView) findViewById(imagelifeID[i]);
            lifeViewArr[i].setImageResource(R.drawable.life);
        }

        coinViewArr[0] = (ImageView) findViewById(imageCoinID[0]);
        coinViewArr[0].setImageResource(R.drawable.coin);

        for (int i = 0; i < imgViewArr.length; i++) {
            final int position = i;
            imgViewArr[i] = (ImageView) findViewById(imageId[i]);
            imgViewArr[i].setImageResource(R.drawable.off);
            imgViewArr[i].setTag(TAG_Empty);

            imgViewArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInFever) {
                        handleFeverClick(v, position);
                    } else {
                        handleNormalClick(v, position); // Handle normal click
                    }

                    if (((ImageView) v).getTag().toString().equals(TAG_Bug1) ||
                            ((ImageView) v).getTag().toString().equals(TAG_Bird) ||
                            ((ImageView) v).getTag().toString().equals(TAG_Bug2)) {
                        ((ImageView) v).setImageResource(R.drawable.off);
                    }
                }
            });

        }
        time.setText("Time : 20");
        score.setText("Point : 0");

        new Thread(new Timer()).start();
        for (int i = 0; i < imgViewArr.length; i++) {
            new Thread(new ObjectThread(i)).start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            time.setText("Time : " + msg.arg1);
        }
    };

    Handler onHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < 5; i++) {
                int index = (int) (Math.random() * 4);
                if (item == 1) {
                    int flag = 0;
                    flag = (int) (Math.random() * 10);
                    if (flag < 1) {
                        index = 0;
                    } else if (flag < 2) {
                        index = 1;
                    }  else if (flag < 3) {
                        index = 2;
                    } else {
                        index = 3;
                    }
                }
                imgViewArr[msg.arg1].setImageResource(ran[index]);

                if (index == 0) {
                    imgViewArr[msg.arg1].setTag(TAG_Bug1);
                } else if (index == 1) {
                    imgViewArr[msg.arg1].setTag(TAG_Bird);
                } else if (index == 2) {
                    imgViewArr[msg.arg1].setTag(TAG_Bug2);
                } else {
                    imgViewArr[msg.arg1].setTag(TAG_Coin);
                }
            }
        }
    };

    Handler offHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            imgViewArr[msg.arg1].setImageResource(R.drawable.off);
        }
    };

    public class Timer implements Runnable {
        final int TIME = 20;

        @Override
        public void run() {
            for (int i = TIME; i >= 0; i--) {
                Message msg = new Message();
                msg.arg1 = i;
                handler.sendMessage(msg);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.putExtra("score", sc);
            intent.putExtra("coin", cn);
            startActivity(intent);
            finish();
        }
    }

    public class ObjectThread implements Runnable {
        int index;

        ObjectThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message msg1 = new Message();
                    int offTime = new Random().nextInt(2000) + 500;
                    Thread.sleep(offTime);

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int onTime = new Random().nextInt(2000) + 500;
                    Thread.sleep(onTime);
                    Message msg2 = new Message();
                    msg2.arg1 = index;
                    offHandler.sendMessage(msg2);

                    if (isInFever) {
                        feverDuration--;
                        if (bugCount <= 0) {
                            isInFever = false;
                            feverLevel = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    score.setTextColor(getResources().getColor(R.color.white));
                                }
                            });
                        }
                    } else {
                        if (bugCount >= 5) {
                            isInFever = true;
                            feverDuration = 5;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    score.setTextColor(getResources().getColor(R.color.yellow));
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handleNormalClick(View v, int position) {
        if (((ImageView) v).getTag().toString().equals(TAG_Bug1)) {
            score.setText("Point : " + String.valueOf(sc += plusScore));
            bugCount++;
            if (bugCount <= 0) {
                isInFever = false;
                feverDuration = 0;
                score.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (((ImageView) v).getTag().toString().equals(TAG_Bird)) {
            score.setText("Point : " + String.valueOf(sc -= plusScore));
            if (sc < 0) {
                sc = 0;
            }
        } else if (((ImageView) v).getTag().toString().equals(TAG_Bug2)) {
            lifeCount--;
            for (int j = lifeCount; j < lifeViewArr.length; j++) {
                lifeViewArr[j].setImageResource(R.drawable.off);
            }
            if (lifeCount <= 0) {
                Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("score", sc);
                intent.putExtra("coin", 0);
                startActivity(intent);
                finish();
            }
        } else if (((ImageView) v).getTag().toString().equals(TAG_Coin)) {
            coin.setText(String.valueOf(cn += plusCoin));
            ((ImageView) v).setImageResource(R.drawable.off);
        } else if (((ImageView) v).getTag().toString().equals(TAG_Empty)) { // 빈자리 처리 부분 추가
        } else {
        }
        imgViewArr[position].setImageResource(R.drawable.off);
        imgViewArr[position].setTag(TAG_Empty);
    }

    private void handleFeverClick(View v, int position) {
        if (((ImageView) v).getTag().toString().equals(TAG_Bug1)) {
            bugCount++;
            if (bugCount <= 0) {
                isInFever = false;
                feverDuration = 0;
                score.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (((ImageView) v).getTag().toString().equals(TAG_Bird)) {
            score.setText("Point : " + String.valueOf(sc -= plusScore*3));
        } else if (((ImageView) v).getTag().toString().equals(TAG_Bug2)) {
            lifeCount--;
            bugCount = 0;
            for (int j = lifeCount; j < lifeViewArr.length; j++) {
                lifeViewArr[j].setImageResource(R.drawable.off);
            }
            if (lifeCount <= 0) {
                Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("score", sc);
                intent.putExtra("coin", 0);
                startActivity(intent);
                finish();
                score.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (((ImageView) v).getTag().toString().equals(TAG_Coin)) {
            coin.setText(String.valueOf(cn += plusCoin));
            ((ImageView) v).setImageResource(R.drawable.off);
        } else if (((ImageView) v).getTag().toString().equals(TAG_Empty)) {
            lifeCount--;
        } else {
        }
        if (bugCount >= 5 && bugCount < 10) {
            isInFever = true;
            score.setText("Point : " + String.valueOf(sc += plusScore*2));
            score.setTextColor(getResources().getColor(R.color.yellow));
        } else if (bugCount >= 10 && bugCount < 15) {
            isInFever = true;
            score.setText("Point : " + String.valueOf(sc += plusScore*3));
            score.setTextColor(getResources().getColor(R.color.blue));
        } else if (bugCount >= 15) {
            isInFever = true;
            score.setText("Point : " + String.valueOf(sc += plusScore*4));
            score.setTextColor(getResources().getColor(R.color.red));
        }
    }
}
